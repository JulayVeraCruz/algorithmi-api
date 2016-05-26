package algorithmi;

import algorithmi.models.Courses;
import algorithmi.models.Institutions;
import algorithmi.models.Schools;
import algorithmi.models.TypeUser;
import algorithmi.models.UserCourse;
import algorithmi.models.Users;
import com.sun.xml.internal.messaging.saaj.util.Base64;
import java.net.URLDecoder;
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

    static int userType = 5;

    public static void main(String[] args) {
        try {
            //staticFileLocation("/api/public");
            externalStaticFileLocation(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8") + "/../../../algorithmi-web");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        before("/api/*", (request, response) -> {
            System.out.println(request.url());
            System.out.println(request.requestMethod());
            if (request.headers("Authorization") != null) {
                String aux[] = Base64.base64Decode(request.headers("Authorization").split(" ")[1]).split(":");
                //Obtem o tipo de utilizador
                userType = Users.exist(aux[0], aux[1]);

            }
        });
        //----------------------------------------------------------------------------------------
        //-------------------------------------- LOGIN -----------------------------------------
        //----------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------
//-------------------------------------- Courses -----------------------------------------
//----------------------------------------------------------------------------------------
        get("/api/courses", (request, response) -> {
            //Listar Cursos
            System.out.println("course : " + Courses.getAll());
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
//------------------------------------ Institutions---------------------------------------
//----------------------------------------------------------------------------------------
        get("/api/institutions", (request, response) -> {
            //Listar Instituições
            return Institutions.getAll();

        });

        post("/api/institutions", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                //Cria uma nova instituicao
                Institutions newInstitutions = new Institutions(data);
                //guarda-a na BD
                response.status(newInstitutions.regist());
                // E uma mensagem
                return "{\"text\":\"Instituição inserida com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" institutions error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }
        });

        put("/api/institutions/:id", (request, response) -> {
            try {
                //Obtem o id mandado pela view
                String id = request.params(":id");
                String data = new String(request.body().getBytes(), "UTF-8");
                //Obtem os dados da instituição
                Institutions insti = new Institutions(data);
                System.out.println(insti.toString());
                //Actualiza os dados
                //Devolve estado
                response.status(insti.updateInstitution());
                // E uma mensagem
                return "{\"text\":\"Instituição alterada com sucesso!\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" institutions error_ " + ex);
                //Devolve 'NOK'
                response.status(400);
                return response;
            }

        });
        get("/api/institutions/:id", (request, response) -> {
            //Obtem o id mandado pela view
            String id = request.params(":id");

            //Obtem os dados dessa instituicao e devolve-o à view
            return Institutions.getInstitutionData(id);

        });

        delete("/api/institutions/:id", (request, response) -> {
            //Obtem o id enviado pela view

            int id = Integer.parseInt(request.params(":id"));
            try {
                response.status(Institutions.deleteInstitution(id));
                return "{\"text\":\"Instituição apagada com sucesso!\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                //Devolve 'NOK'
                response.status(400);
                return "{\"text\":\"Instituicao não apagado\"}";
            }

        });

//----------------------------------------------------------------------------------------
//------------------------------------ Institutions---------------------------------------
//----------------------------------------------------------------------------------------
        get("/api/schools", (request, response) -> {
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
        post("/api/students", (request, response) -> {

            try {
                //Converte o body recebido da view
                String data = new String(request.body().getBytes(), "UTF-8");
                //Cria uma nova instituicao
                Users newUser = new Users(data);

                UserCourse newUserCourse = new UserCourse(newUser.getID(), Integer.parseInt(newUser.getProperties()));

                newUser.regist();

                newUserCourse.regist();
                response.status(200);
                //response.status(newUser.regist());
                //guarda-a na BD
                // response.status(newSchool.insert());
                // E uma mensagem
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

//----------------------------------------------------------------------------------------
//------------------------------------ UserType ------------------------------------------
//----------------------------------------------------------------------------------------
        get("/api/userType", (request, response) -> {
            return TypeUser.listTypesOfUser();//lista dos cursos existentes

        });
    }
}
