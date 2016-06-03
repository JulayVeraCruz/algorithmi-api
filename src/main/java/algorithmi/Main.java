package algorithmi;

import algorithmi.Models.Questions;
import algorithmi.models.Categories;
import algorithmi.models.Courses;
import algorithmi.models.Institutions;
import algorithmi.models.Languages;
import algorithmi.models.Schools;
import algorithmi.models.TypeUser;
import algorithmi.models.UserCourse;
import algorithmi.models.Users;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.SparkBase.externalStaticFileLocation;

/**
 *
 * @author Pedro Dias
 */
public class Main {

    static Users actualUser;
    static Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            //staticFileLocation("/api/public");
            externalStaticFileLocation(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8") + "/../../../algorithmi-web");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        before("/api/*", (request, response) -> {
            System.out.println(request.requestMethod() + "----" + request.url());

            String auth = request.headers("Authorization");
            if (auth != null && auth.startsWith("Basic")) {

                String b64Credentials = auth.substring("Basic".length()).trim();
                String credentials[] = new String(Base64.getDecoder().decode(b64Credentials)).split(":");
                // Obtem os dados do user
                String data = Users.exist(credentials[0], response);
                //se encontrar
                if (data != null) {
                    //Transforma os dados recebidos na class e obtem os dados do utilizador loggado
                    actualUser = gson.fromJson(data, Users.class);
                    //Se a password estiver correcta
                    if (actualUser.getPassword() == null ? credentials[1] != null : !actualUser.getPassword().equals(credentials[1])) {
                        actualUser = null;
                        halt(401, "{\"text\":\"Login inválido. Palavra-passe incorrecta!\"}");
                    }
                    //Se o utilizador estiver inactivo
                    if (!actualUser.getState()) {
                        actualUser = null;
                        halt(401, "{\"text\":\"Lamentamos mas a sua conta não está activa. Se o problema persistir, contacte o administrador!\"}");
                    }
                    System.out.println("EU: " + actualUser.toString());
                } else {
                    halt(401, "{\"text\":\"Login inválido. O username ou e-mail inseridos não estão registados!\"}");
                }
            } else {
                halt(401, "{\"text\":\"Login inválido. Por favor, efectue login ou registe-se para continuar!\"}");

            }

        });
        //----------------------------------------------------------------------------------------
        //---------------------- Se o email/username ja esta registado ----------------------------
        //-----------------------------------------------------------------------------------------
        post("/isUserValid", (request, response) -> {
            try {
                //Obtem o email/username mandado pela view
                String data = new String(request.body().getBytes(), "UTF-8");
                System.out.println(data);
                if (data != null) {
                    //Transforma os dados recebidos na class e obtem os dados do utilizador
                    Users resgistedUser = gson.fromJson(Users.exist(data, response), Users.class);
                    //se não encontrar, o email/username nao esta a ser utilizado
                    if (resgistedUser == null) {
                        response.status(200);
                        return true;
                    }
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.status(200);
            return false;
        });

        //----------------------------------------------------------------------------------------
        //----------------------------------- Os meus dados --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/me", (request, response) -> {
            //se estiver loggado
            if (actualUser == (null)) {
                System.out.println("You shall not pass!");
                //Senao devolve um Forbidden
                response.status(401);
                return "{\"text\":\"Login inválido. Por favor, efectue login para continuar!\"}";
            } else {
                System.out.println("You shall pass : " + actualUser.getUsername());
                //Listar os dados do admin
                if (actualUser.getType() == 2) {
                    return actualUser.getMyAdminData();
                    //Listar os dados do teacher
                } else if (actualUser.getType() == 3) {
                    return actualUser.getMyTeacherData();
                    //Listar os dados do alino
                } else {
                    return actualUser;
                }
            }
        });

        //----------------------------------------------------------------------------------------
        //-------------------------------------- Categories --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/categories", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve as categorias
                return Categories.getAll(response);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });

        get("/api/categories/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Categories.getCategoryData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/categories", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Cria uma nova instituicao
                    Categories newCategory = gson.fromJson(data, Categories.class);
                    //Guarda-a na BD devolve o estado
                    return newCategory.insert(response);
                } catch (Exception ex) {
                    System.out.println("Categories error: ");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Categoria não inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        put("/api/categories/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Categories category = gson.fromJson(data, Categories.class);
                    //Actualiza os dados e devolve estado e a msg
                    return category.updateCategory(response);
                } catch (Exception ex) {
                    System.out.println("Categories error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Categoria não alterada!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        delete("/api/categories/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id enviado pela view
                int id = Integer.parseInt(request.params(":id"));
                try {
                    return Categories.delete(response, id);

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Ocorreu um erro ao apagar a Categoria.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        //----------------------------------------------------------------------------------------
        //-------------------------------------- Courses -----------------------------------------
        //----------------------------------------------------------------------------------------
        get("/courses", (request, response) -> {
            //Lista e devolve os Cursos
            return Courses.getAll(response);

        });

        get("/api/courses/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Courses.getCourseData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/courses", (request, response) -> {
            //Se tiver permissões de admin
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Courses newCourse = gson.fromJson(data, Courses.class);
                    //Guarda-a na BD devolve o estado
                    return newCourse.insert(response);
                } catch (Exception ex) {
                    System.out.println("Courses error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Curso não inserido!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        put("/api/courses/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Courses course = gson.fromJson(data, Courses.class);
                    //Actualiza os dados e devolve estado e a msg
                    return course.updateCourse(response);
                } catch (Exception ex) {
                    System.out.println("Courses error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Curso não alterado!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        delete("/api/courses/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id enviado pela view
                int id = Integer.parseInt(request.params(":id"));
                try {
                    return Courses.delete(response, id);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Ocorreu um erro ao apagar o Curso.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });

        //----------------------------------------------------------------------------------------
        //---------------------------------- highLevelLangs --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/highlevellangs", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve as instituicoes
                return Languages.getAll(response);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });

        get("/api/highlevellangs/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Languages.getLanguageData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/highlevellangs", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Languages newLanguage = gson.fromJson(data, Languages.class);
                    //Guarda-a na BD devolve o estado
                    return newLanguage.insert(response);
                } catch (Exception ex) {
                    System.out.println("Languages error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Linguagem não inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        put("/api/highlevellangs/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Languages language = gson.fromJson(data, Languages.class);
                    //Actualiza os dados e devolve estado e a msg
                    return language.updateInstitution(response);
                } catch (Exception ex) {
                    System.out.println("Languages error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Linguagem não alterada!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        delete("/api/highlevellangs/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id enviado pela view
                int id = Integer.parseInt(request.params(":id"));
                try {
                    return Languages.delete(response, id);

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Ocorreu um erro ao apagar a Linguagem.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ Institutions---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/institutions", (request, response) -> {
            //Lista e devolve as instituicoes
            return Institutions.getAll(response);
        });
        get("/api/institutions/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Institutions.getInstitutionData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/institutions", (request, response) -> {
            //Se tiver permissões de admin
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Institutions newInstitutions = gson.fromJson(data, Institutions.class);
                    //Guarda-a na BD devolve o estado
                    return newInstitutions.insert(response);
                } catch (Exception ex) {
                    System.out.println("Institutions error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Instituição não inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        put("/api/institutions/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Institutions institution = gson.fromJson(data, Institutions.class);
                    //Actualiza os dados e devolve estado e a msg
                    return institution.updateInstitution(response);
                } catch (Exception ex) {
                    System.out.println("Institutions error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Instituição não alterada!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        delete("/api/institutions/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id enviado pela view
                int id = Integer.parseInt(request.params(":id"));
                try {
                    return Institutions.delete(response, id);

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Ocorreu um erro ao apagar a instituição.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ Questions---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/questions", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Questions.getAll(response);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });

        get("/api/questions/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Questions.getQuestionData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/questions", (request, response) -> {
            //Se tiver permissões de professor
            if (isAllowed(3)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    System.out.println("d " + data);
                    //Transforma os dados recebidos na class
                    Questions newQuestion = gson.fromJson(data, Questions.class);
                    //Guarda-a na BD devolve o estado
                    return newQuestion.insert(response);
                } catch (Exception ex) {
                    System.out.println("Questions error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Pergunta não inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });
        //----------------------------------------------------------------------------------------
        //-------------------------------------- Escolas -----------------------------------------
        //----------------------------------------------------------------------------------------
        get("/schools", (request, response) -> {
            //Lista e devolve as escolas
            return Schools.getAll(response);

        });

        get("/api/schools/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Schools.getSchoolData(response, id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        post("/api/schools", (request, response) -> {

            //Se tiver permissões de admin
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Schools newSchool = gson.fromJson(data, Schools.class);
                    //Guarda-a na BD devolve o estado
                    return newSchool.insert(response);
                } catch (Exception ex) {
                    System.out.println("Schools error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Escola não inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });

        put("/api/schools/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Schools school = gson.fromJson(data, Schools.class);
                    //Actualiza os dados e devolve estado e a msg
                    return school.updateSchool(response);
                } catch (Exception ex) {
                    System.out.println("Schools error:");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Escola não alterada!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });

        delete("/api/schools/:id", (request, response) -> {
            //Obtem o id enviado pela view
            int id = Integer.parseInt(request.params(":id"));
            try {
                return Schools.delete(response, id);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Escola não apagada\"}";
            }

        });
//----------------------------------------------------------------------------------------
//------------------------------------ Students ------------------------------------------
//----------------------------------------------------------------------------------------
        //Obtem a lista de todos os estudantes
        get("/api/students", (request, response) -> {
            try {
                return Users.listStudents();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem alunos para mostrar\"}";
            }

        });
        post("/user", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                System.out.println("__" + data);
                //Cria um novo user
                Users newUser = gson.fromJson(data, Users.class);
                newUser.regist();
                //Envia o id do user e do curso para o associar
                UserCourse newUserCourse = new UserCourse(newUser.getID(), data);
                newUserCourse.regist();

                response.status(200);
                return "{\"text\":\"Utilizador inserida com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" institutions error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }
        });
        delete("/api/students/:id", (request, response) -> {
            //Obtem o id enviado pela view

            int id = Integer.parseInt(request.params(":id"));
            try {
                response.status(Users.delete(id));
                return "{\"text\":\"Aluno apagado com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Aluno não apagado\"}";
            }

        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ Teachers---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/teachers", (request, response) -> {
            //Listar Instituições
            return Users.getAllTeachers();
        });
        post("/api/userStatus/:id", (request, response) -> {
            try {
                //Converte o body recebido da view para obter o estado
                String state = new String(request.body().getBytes(), "UTF-8");

                //Obtem o id mandado pela view
                String id = request.params(":id");

                System.out.println(Users.getUser(id));
                Users user = gson.fromJson(Users.getUser(id), Users.class);

                System.out.println("User to update: " + user.toString());
                System.out.println("My type: " + actualUser.getType() + " user type:  " + user.getType());
                System.out.println(actualUser.getType() < user.getType());

                //Se as permissoes do user que vou alterar, forem menores do que as minhas
                if (user.getType() > actualUser.getType()) {
                    //Devolve estado
                    response.status(200);
                    // E uma mensagem
                    return user.changeState(state, response);
                } else {
                    response.status(400);
                    return "{\"text\":\"Não tem permissões para alterar o estado deste user!\"}";
                }

            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                response.status(400);
                return "{\"text\":\"Não foi possível alterar o estado!\"}";

            }

        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ UserType ------------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/userType", (request, response) -> {
            return TypeUser.listTypesOfUser();//lista dos cursos existentes

        });
    }

    private static boolean isAllowed(int role) {
        if (role >= actualUser.getType()) {
            return true;
        }
        return false;
    }
}
