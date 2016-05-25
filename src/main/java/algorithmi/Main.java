package algorithmi;

import algorithmi.models.Courses;
import algorithmi.models.Institutions;
import algorithmi.models.Schools;
import algorithmi.models.Users;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

                //      System.out.println(aux[0]);
                //     System.out.println(aux[1]);
//
                //  System.out.println(request.headers("Authorization"));
                // System.out.println(request.headers("Accept"));
                // System.out.println(request.headers("Content-Type"));
            }
        });
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
                Users.deleteUser(9);
                return Users.listStudents();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem alunos para mostrar\"}";
            }

        });
        get("/api/students/:id", (request, response) -> {

            try {
                //Obtem o id mandado pela view
                String id = request.params(":id");

                //Obtem os dados desse utilizador
                //Devolve-o à view
                return Users.getUser(id);

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem alunos para mostrar\"}";
            }

        });

        put("/api/students/:id", (request, response) -> {

            try {
                //Obtem o id mandado pela view
                String id = request.params(":id");

                System.out.println("PUT" + Users.getUserData(id));
                //Obtem os dados desse utilizador
                Users user = new Users(Users.getUserData(id));
                System.out.println("user " + user);
                //Faz as devidas alteraçoes
                user.updateUser(java.net.URLDecoder.decode(request.body(), "UTF-8"));

                return user;

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem alunos para mostrar\"}";
            }

        });

        post("api/students", (request, response) -> {
            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject user = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + user.entrySet());

                Users newUser = new Users(data);
                System.out.println(newUser);
                return newUser.regist();//devolve um inteiro-> status

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"User nao inserido\"}";
            }
        });

        //----------------------------------------------------------------------------------------
        //------------------------------------ Teachers---------------------------------------
        //----------------------------------------------------------------------------------------
        get("/api/teachers", (request, response) -> {
            //Listar Instituições
            return Users.getAllTeachers();

        });

    }
}
