package algorithmi;

import algorithmi.Models.Questions;
import algorithmi.models.Categories;
import algorithmi.models.Courses;
import algorithmi.models.HighLevelLangs;
import algorithmi.models.Institutions;
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

                //String query = "select * tblUsers ";
                // System.out.println(Utils.utils.executeSelectCommand(query));
                String b64Credentials = auth.substring("Basic".length()).trim();
                String credentials[] = new String(Base64.getDecoder().decode(b64Credentials)).split(":");
                // System.out.println(credentials);

                String data = Users.exist(credentials[0], credentials[1]);
                //Transforma os dados recebidos na class e obtem os dados do utilizador loggado
                actualUser = gson.fromJson(data, Users.class);

                System.out.println("EU: " + actualUser.toString());

                ///  utils.executeIUDCommand(query);
                //  String query = "update tblusers set state=0 where id=0 or id=14 or id=15 or id=16 or id=17";
                // utils.executeIUDCommand(query);
                //  query = "Insert into tblcodelangs values(1, 3,' for i = 0 ; i<5 ble ble ble')";
                //  utils.executeIUDCommand(query);
                // query = "Insert into tblcodelangs values(2, 3,' for i = 5 ; i<0 bla bla bla')";
                //  utils.executeIUDCommand(query);
                //   query = "Insert into tblcodelangs values(2, 1,' for i = 5 ; i<0 ble ble ble')";
                ///  utils.executeIUDCommand(query);
                //    String query = "select * from tblquestions join tblinputoutputs on tblquestions.id=tblinputoutputs.question";
                //   String query = "select * from  tblcodelangs";
                //String result = utils.executeSelectCommand(query).toString();
                //  System.out.println(result);
            }
        });
        //----------------------------------------------------------------------------------------
        //----------------------------------- Os meus dados --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/me", (request, response) -> {
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
        });

        //----------------------------------------------------------------------------------------
        //-------------------------------------- Categories --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/categories", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Categories.getAll();
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
                    Categories newCategory = new Categories(data);
                    //guarda-a na BD
                    response.status(newCategory.regist());
                    // E uma mensagem
                    return "{\"text\":\"Categoria inserida com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Categories error_ " + ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Categoria não inserida!\"}";
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
                    response.status(Categories.delete(id));
                    return "{\"text\":\"Categoria  apagada com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Categoria não apagada.\"}";
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
            return Courses.getAll();

        });
        post("/api/courses", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                //Cria uma nova instituicao
                Courses newCourse = new Courses(data);
                //guarda-a na BD
                response.status(newCourse.regist());
                // E uma mensagem
                return "{\"text\":\"Curso inserido com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" Curso error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }
        });
        put("/api/courses/:id", (request, response) -> {
            try {
                String data = new String(request.body().getBytes(), "UTF-8");
                //Obtem os dados da instituição
                Courses course = new Courses(data);
                System.out.println(course.toString());
                //Actualiza os dados
                //Devolve estado
                response.status(course.updateCourse());
                // E uma mensagem
                return "{\"text\":\"Curso alteradao com sucesso!\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" Curso error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }

        });
        get("/api/courses/:id", (request, response) -> {
            //Obtem o id mandado pela view
            String id = request.params(":id");
            //Obtem os dados dessa instituicao e devolve-o à view
            return Courses.getCourseData(id);

        });
        delete("/api/courses/:id", (request, response) -> {
            //Obtem o id enviado pela view

            int id = Integer.parseInt(request.params(":id"));
            try {
                response.status(Courses.deleteCourse(id));
                return "{\"text\":\"Curso apagado com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Curso não apagado\"}";
            }

        });

        //----------------------------------------------------------------------------------------
        //---------------------------------- highLevelLangs --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/highlevellangs", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve as instituicoes
                return HighLevelLangs.getAll();
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
                    //Cria uma nova instituicao
                    HighLevelLangs newLanguage = new HighLevelLangs(data);
                    //guarda-a na BD
                    response.status(newLanguage.regist());
                    // E uma mensagem
                    return "{\"text\":\"Linguagem inserida com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Language error_ " + ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Linguagem não inserida!\"}";
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
                    response.status(HighLevelLangs.delete(id));
                    return "{\"text\":\"Linguagem  apagada com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Linguagem não apagada.\"}";
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
            return Institutions.getAll();

        });

        post("/api/institutions", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Institutions newInstitutions = gson.fromJson(data, Institutions.class);
                    //guarda-a na BD
                    response.status(newInstitutions.insert());
                    // E uma mensagem
                    return "{\"text\":\"Instituição inserida com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(" institutions error_ " + ex);
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
                    String id = request.params(":id");
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Transforma os dados recebidos na class
                    Institutions institution = gson.fromJson(data, Institutions.class);
                    //Actualiza os dados
                    //Devolve estado
                    response.status(institution.updateInstitution());
                    // E uma mensagem
                    return "{\"text\":\"Instituição alterada com sucesso!\"}";
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(" institutions error_ " + ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return response;
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });
        get("/api/institutions/:id", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o à view
                return Institutions.getInstitutionData(id);
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
                    response.status(Institutions.delete(id));
                    return "{\"text\":\"Instituição benfica ole!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Instituicao não apagado\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";

        });
        get("/api/questions", (request, response) -> {
            //Se tiver permissões
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Questions.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"Não tem permissões para executar esta tarefa!\"}";
        });
        post("/api/questions", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                //Cria uma nova instituicao
                Questions newQuestion = new Questions(data);
                // System.out.println(newQuestion);
                //guarda-a na BD
                response.status(newQuestion.insert(data));
                // E uma mensagem
                return "{\"text\":\"Pergunta inserida com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" Questions error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }
        });
//----------------------------------------------------------------------------------------
//------------------------------------ Institutions---------------------------------------
//----------------------------------------------------------------------------------------
        get("/schools", (request, response) -> {
            //Listar Instituições
            return Schools.getAll();

        });
        post("/api/schools", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                //Cria uma nova instituicao
                Schools newSchool = new Schools(data);
                System.out.println(newSchool);
                //guarda-a na BD
                response.status(newSchool.insert());
                // E uma mensagem
                return "{\"text\":\"Escola inserida com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" institutions error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }
        });
        delete("/api/schools/:id", (request, response) -> {
            //Obtem o id enviado pela view

            int id = Integer.parseInt(request.params(":id"));
            try {
                response.status(Schools.delete(id));
                return "{\"text\":\"Instituição apagada com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Instituicao não apagado\"}";
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
                    response.status(user.changeState(state));
                    // E uma mensagem
                    return "{\"text\":\"Estado alterado com sucesso!\"}";
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
