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
                System.out.println(credentials);
                actualUser = new Users(Users.exist(credentials[0], credentials[1]));
                System.out.println("EU : " + actualUser.getUsername());

                // String query = "Insert into tblinputoutputs values(3, 1,'','10, 11, 12, 13, 14, 15')";
                // utils.executeIUDCommand(query);
                // query = "Insert into tblinputoutputs values(2, 2,'','5, 4, 3, 2, 1, 0')";
                ///  utils.executeIUDCommand(query);
                //   String query = "Insert into tblcodelangs values(1, 2,' for i = 0 ; i<5 bla bla bla')";
                //  utils.executeIUDCommand(query);
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
        //-------------------------------------- LOGIN -----------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/me", (request, response) -> {
            //Listar Cursos

            return actualUser;
        });

        //----------------------------------------------------------------------------------------
        //-------------------------------------- Categories --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/categories", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Categories.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";
        });

        post("/api/categories", (request, response) -> {
            //Se tiver permiss�es
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
                    return "{\"text\":\"Categoria n�o inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        delete("/api/categories/:id", (request, response) -> {
            //Se tiver permiss�es
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
                    return "{\"text\":\"Categoria n�o apagada.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        //----------------------------------------------------------------------------------------
        //-------------------------------------- Courses -----------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/courses", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Courses.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";
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
                //Obtem os dados da institui��o
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
            //Obtem os dados dessa instituicao e devolve-o � view
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
                return "{\"text\":\"Curso n�o apagado\"}";
            }

        });

        //----------------------------------------------------------------------------------------
        //---------------------------------- highLevelLangs --------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/highlevellangs", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(3)) {
                //Lista e devolve as instituicoes
                return HighLevelLangs.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";
        });
        post("/api/highlevellangs", (request, response) -> {
            //Se tiver permiss�es
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
                    return "{\"text\":\"Linguagem n�o inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        delete("/api/highlevellangs/:id", (request, response) -> {
            //Se tiver permiss�es
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
                    return "{\"text\":\"Linguagem n�o apagada.\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        //----------------------------------------------------------------------------------------
        //------------------------------------ Institutions---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/institutions", (request, response) -> {

            //Se tiver permiss�es
            if (isAllowed(3)) {
                //Lista e devolve as instituicoes
                return Institutions.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";
        });

        post("/api/institutions", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(2)) {
                try {
                    //Converte o body recebido da view
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Cria uma nova instituicao
                    Institutions newInstitutions = new Institutions(data);
                    //guarda-a na BD
                    response.status(newInstitutions.insert());
                    // E uma mensagem
                    return "{\"text\":\"Institui��o inserida com sucesso!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(" institutions error_ " + ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Institui��o n�o inserida!\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });

        put("/api/institutions/:id", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(2)) {
                try {
                    //Obtem o id mandado pela view
                    String id = request.params(":id");
                    String data = new String(request.body().getBytes(), "UTF-8");
                    //Obtem os dados da institui��o
                    Institutions insti = new Institutions(data);
                    System.out.println(insti.toString());
                    //Actualiza os dados
                    //Devolve estado
                    response.status(insti.updateInstitution());
                    // E uma mensagem
                    return "{\"text\":\"Institui��o alterada com sucesso!\"}";
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
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        get("/api/institutions/:id", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(2)) {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                //Obtem os dados dessa instituicao e devolve-o � view
                return Institutions.getInstitutionData(id);
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });

        delete("/api/institutions/:id", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(2)) {
                //Obtem o id enviado pela view
                int id = Integer.parseInt(request.params(":id"));
                try {
                    response.status(Institutions.delete(id));
                    return "{\"text\":\"Institui��o benfica ole!\"}";

                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    //Devolve 'NOK'
                    response.status(400);
                    return "{\"text\":\"Instituicao n�o apagado\"}";
                }
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";

        });
        get("/api/questions", (request, response) -> {
            //Se tiver permiss�es
            if (isAllowed(3)) {
                //Lista e devolve os Cursos
                return Questions.getAll();
            }
            //Senao devolve um Forbidden
            response.status(401);
            return "{\"text\":\"N�o tem permiss�es para executar esta tarefa!\"}";
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
        get("/api/schools", (request, response) -> {
            //Listar Institui��es
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
                return "{\"text\":\"Institui��o apagada com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Instituicao n�o apagado\"}";
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
        post("/api/students", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                System.out.println("__" + data);
                //Cria um novo user
                Users newUser = new Users(data);
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
                return "{\"text\":\"Aluno n�o apagado\"}";
            }

        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ Teachers---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/teachers", (request, response) -> {
            //Listar Institui��es
            return Users.getAllTeachers();
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
