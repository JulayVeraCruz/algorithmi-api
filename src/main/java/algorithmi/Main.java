package algorithmi;

import algorithmi.Models.Question;
import algorithmi.models.Course;
import algorithmi.models.Institutions;
import algorithmi.models.MatrixTests;
import algorithmi.models.Schools;
import algorithmi.models.Tests;
import algorithmi.models.TypeUser;
import algorithmi.models.User;
import algorithmi.models.UserCourse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.sun.xml.internal.messaging.saaj.util.Base64;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.before;

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
             //   String aux[] = Base64.base64Decode(request.headers("Authorization").split(" ")[1]).split(":");

             //   System.out.println(aux[0]);
             //   System.out.println(aux[1]);

                // halt(401, "You are not welcome here");
            }
        });

//----------------------------------------------------------------------------------------
//-------------------------------------- Categories --------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/categories/:id", (request, response) -> {
            return "Hello World";
        });
        post("/api/categories/new", (request, response) -> {
            return "Hello World";
        });

        put("/api/categories/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/api/categories/:id", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//--------------------------------------- CodeLangs --------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/codelang/:id", (request, response) -> {
            return "Hello World";
        });
        post("/api/codelang", (request, response) -> {
            return "Hello World";
        });

        put("/api/codelang/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/api/codelang/:id", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Courses -------------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/courses", (request, response) -> {
            
            String data = null;
            try {
//                response.status(200);
//                response.type("api/courses");
                return Course.listCourses_WEB();//lista dos cursos existentes
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem cursos para mostrar\"}";
            }

        });
        post("/api/course/new", (request, response) -> {//criar novo curso

            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("course.entrySet " + course.entrySet());

                Course newCourse = new Course(data);

                int registStatus = newCourse.regist(); //devolve um inteiro-> status

                return "{\"resposta\":\"Curso inserido\"}";

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(" course error_ " + ex);
                return "{\"resposta\":\"Curso nao inserido\"}";
            }
        });
        put("/api/course/:id", (request, response) -> {//alterar curso
            int status = 400;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                Course courseAlter = new Course(data, id);

                status = courseAlter.updateCourse(id);
                return "{\"resposta\":\"Utilizador actualizado\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Utilizador não actualizado\"}";
        });

        delete("/api/course/:id", (request, response) -> {
            int status = 400;
            boolean deleted = false;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + course.entrySet());

                Course courseDel = new Course(data, id);

                return  courseDel.deleteCourse(id);
               
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Curso não apagado\"}";
        });

//----------------------------------------------------------------------------------------
//-------------------------------------- Details -----------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/details/", (request, response) -> {
            return "Hello World";
        });
        post("/api/details", (request, response) -> {
            return "Hello World";
        });

        put("/api/details/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/details", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//---------------------------------- highLevelLangs --------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/highLevelLangs/", (request, response) -> {
            return "Hello World";
        });
        post("/api/highLevelLangs", (request, response) -> {
            return "Hello World";
        });

        put("/api/highLevelLangs/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/highLevelLangs", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ inputOutput ---------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/inputOutput/", (request, response) -> {
            return "Hello World";
        });
        post("/api/inputOutput", (request, response) -> {
            return "Hello World";
        });

        put("/api/inputOutput/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/inputOutput", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Institutions---------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/institution/", (request, response) -> {
            
            try {
                //Listar Instituições
                return Institutions.listInstitutions_WEB();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem Instituições para mostrar\"}";
            }
            
        });

        post("/api/institution/new", (request, response) -> {

            String data;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject institut = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println(institut.entrySet());
                Institutions newInstitu = new Institutions(data);

                return newInstitu.regist();//devolve um inteiro-> status

                //Exibe o paramentro "name" do objecto json
                //System.out.println(course.get("name"));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "Hello World";
        });

        put("/api/institution/:id", (request, response) -> {

            return "Hello World";
        });

        delete("/api/institution/:id", (request, response) -> {
            return "Hello World";
        });


//----------------------------------------------------------------------------------------
//------------------------------------ MatrixTests ---------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/matrixTests/", (request, response) -> {
            
            try {
                //Listar MatrixTests
                return MatrixTests.listMatrixTests_WEB();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem MatrixTests para mostrar\"}";
            }
            
        });
        post("/api/matrixTests", (request, response) -> {
            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject matrix = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("course.entrySet " + matrix.entrySet());

                MatrixTests newMatrix = new MatrixTests(data);

                return newMatrix.regist();//devolve um inteiro-> status

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Matriz nao inserida\"}";
            }
        });

        put("/api/matrixTests/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/matrixTests", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Questions -----------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/question/:id", (request, response) -> {

            //  Question questionExample = new Question("{'id_Pergunta':'1','titulo':'XPTO','categoria':'decisao','descricao':'xpto decide','imagem':'','algoritmo':''}");
            //  return questionExample.toString();
            return "Hello World";
        });

        post("/api/question/new", (request, response) -> {
            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject question = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                //QUANDO SE CRIA UMA PERGUNTA, A STRING VEM PARAR AO question.entrySet()
                System.out.println(data+"teste");
                Question questio = new Question(data);
                
                //return questio.regist(0, data, Integer.parseInt(data), data, data, data, Integer.parseInt(data));
                  return questio.regist();
                //Exibe o paramentro "name" do objecto json
                //System.out.println(course.get("name"));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Question nao inserida\"}";
            }
        });

        put("/api/question/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/api/question/:id", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Schools -------------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/school/", (request, response) -> {
            
            try {
                //Listar Instituições
                return Schools.listSchools_WEB();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem Escolas para mostrar\"}";
            }
            
        });
        post("/api/school/new", (request, response) -> {

            String data;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject school = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println(school.entrySet());
                Schools schooll = new Schools(data);

                return schooll.regist();

                //Exibe o paramentro "name" do objecto json
                //System.out.println(course.get("name"));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Escola nao inserida\"}";
            }

        });

        put("/api/school/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/api/school/:id", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ TestQuestions -------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/testQuestions/", (request, response) -> {
            return "Hello World";
        });
        post("/api/testQuestions", (request, response) -> {
            return "Hello World";
        });

        put("/api/testQuestions/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/testQuestions", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//---------------------------------------- Tests -----------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/tests/", (request, response) -> {
            
            try {
                //Listar MatrixTests
                return Tests.listTests_WEB();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem Tests para mostrar\"}";
            }
            
        });
        post("/api/tests", (request, response) -> {

            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("course.entrySet " + course.entrySet());

                Tests newTest = new Tests(data);

                return newTest.regist();//devolve um inteiro-> status

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Teste nao inserido\"}";
            }

        });

        put("/api/tests/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/tests", (request, response) -> {
            return "Hello World";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ UserType ------------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/userType/", (request, response) -> {
            try {
                return TypeUser.listTypesOfUser();//lista dos cursos existentes
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Sem cursos para mostrar\"}";
            }

        });
        post("/api/userType", (request, response) -> {
            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("course.entrySet " + course.entrySet());

                TypeUser newtype = new TypeUser(data);

                return newtype.regist();//devolve um inteiro-> status

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Type nao inserido\"}";
            }
        });

        put("/api/userType/", (request, response) -> {
            int status = 400;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject typeUser = (JsonObject) jsonParser.parse(data);

                TypeUser typeUse = new TypeUser(data, id);

                status = typeUse.updateTypeUser(id);
                return "{\"resposta\":\"Utilizador actualizado\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Utilizador não actualizado\"}";
        });

        delete("/api/userType", (request, response) -> {
            int status = 400;
            boolean deleted = false;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + course.entrySet());

                TypeUser typeUse = new TypeUser(data, id);

                status = typeUse.deleteType(id);
                if (status == 200) {
                    return "{\"resposta\":\"Tipo apagado\"}";
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Tipo não apagado\"}";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Users ---------------------------------------------
//----------------------------------------------------------------------------------------
        
        //Obtem um utilizador pelo seu id (id_User)
        get("/api/user/:id", (request, response) -> {
//            try {
//                //http://127.0.0.1:4567/#students/56
//                User userExample = new User("{'_id':'16845','name':'Fábio Cruz','password':'123456','dateBirth':'08/03/1998','email':'aluno1548@ipt.pt','type':'3','imgB64':'data:image/jpeg;base64,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'}");
//                System.out.println(userExample.toString());
//            } catch (Exception ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return userExample.toString();
            return "Hello World";
        });

        post("/api/user", (request, response) -> {
            //http://127.0.0.1:4567/#login
            //separador registar
            String data = null;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject user = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + user.entrySet());

                User newUser = new User(data);
                return newUser.regist();
                //Exibe o paramentro "name" do objecto json
                //System.out.println(course.get("name"));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"Utilizador nao inserido\"}";
            }
        });

        put("/api/user/:id", (request, response) -> {
            int status = 400;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject user = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + user.entrySet());

                User userAlter = new User(data, id);

//                status = userAlter.updateUser(id, false);//n se quer alterar o tipo de course
                return "{\"resposta\":\"Utilizador actualizado\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Utilizador não actualizado\"}";
        });

        delete("/api/user/:id", (request, response) -> {
            int status = 400;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject user = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + user.entrySet());

                User userDel = new User(data, id);

                status = userDel.deleteUser(id);//n se quer alterar o tipo de course
                return "{\"resposta\":\"Utilizador actualizado\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Utilizador não actualizado\"}";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Teachers ------------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/teachers", (request, response) -> {

            try {
                return User.listTeacher();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Sem professores para mostrar\"}";
        });
        post("/api/teacher", (request, response) -> {
            return "Hello World";
        });

        put("/api/teacher/:id", (request, response) -> {
            int status = 400;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject userprof = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("userprof.entrySet " + userprof.entrySet());

                User userAlter = new User(data, id);

//                status = userAlter.updateUser(id, false);//n se quer alterar o tipo de user
                return "{\"resposta\":\"Professor actualizado\"}";
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"Professor não actualizado\"}";
        });

        delete("/api/teacher/:id", (request, response) -> {
            int status = 400;
            boolean deleted = false;
            String data = null;
            int id = Integer.parseInt(request.params(":id"));
            try {
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");

                //Objecto Jason para aceder aos parametros via Java
                JsonParser jsonParser = new JsonParser();
                JsonObject course = (JsonObject) jsonParser.parse(data);

                //Exibe os dados, em formato json
                System.out.println("user.entrySet " + course.entrySet());

                User userDel = new User(data, id);

                status = userDel.deleteUser(id);
                if (status == 200) {
                    return "{\"resposta\":\"User apagado\"}";
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "{\"resposta\":\"User não apagado\"}";
        });

//----------------------------------------------------------------------------------------
//------------------------------------ Students ------------------------------------------
//----------------------------------------------------------------------------------------
        
        //Obtem a lista de todos os estudantes
        get("/api/students", (request, response) -> {

            try {
                User.deleteUser(9);
                return User.listStudents();
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

                User newUser = new User(data);

                return newUser.regist();//devolve um inteiro-> status

            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"User nao inserido\"}";
            }
        });

//----------------------------------------------------------------------------------------
//------------------------------------ UserCourse ----------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/userCourses/", (request, response) -> {
            int id = Integer.parseInt(request.params(":_id"));
            try {
                return UserCourse.coursesOfUser(id);//lista dos cursos existentes
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "{\"resposta\":\"User sem cursos para mostrar\"}";
            }
        });
        post("/api/userCourses", (request, response) -> {
//              String data = null;
//            try {
//                //JSon Puro (Raw)
//                data = java.net.URLDecoder.decode(request.body(), "UTF-8");
//
//                //Objecto Jason para aceder aos parametros via Java
//                JsonParser jsonParser = new JsonParser();
//                JsonObject course = (JsonObject) jsonParser.parse(data);
//
//                //Exibe os dados, em formato json
//                System.out.println("course.entrySet " + course.entrySet());
//
//                UserCourse newUseCourse = new UserCourse(data);
//
//                return newUseCourse.regist();//devolve um inteiro-> status
//
//            } catch (Exception ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                return "{\"resposta\":\"Curso nao inserido\"}";
//            }
            return "Hello World";
        });

        put("/api/userCourses/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/userCourses", (request, response) -> {
            try {
                UserCourse.deleteRegist(Integer.parseInt(request.params(":userID")), Integer.parseInt(request.params(":courseID")));
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "Hello World";

        });

//----------------------------------------------------------------------------------------
//------------------------------------ Versão --------------------------------------------
//----------------------------------------------------------------------------------------
        
        get("/api/versao", (request, response) -> {
            return "Hello World";
        });

        post("/api/versao", (request, response) -> {

            String data;
            try {
                //JSon Puro (Raw)
                data = java.net.URLDecoder.decode(request.body(), "UTF-8");
                System.out.println(data + "");
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return "isto trabalha";
            }

            return "Hello World";
        });
        put("/api/versao/", (request, response) -> {
            return "Hello World";
        });

        delete("/api/versao", (request, response) -> {
            return "Hello World";
        });

    }
}
