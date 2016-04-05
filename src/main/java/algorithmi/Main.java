package algorithmi;

import algorithmi.models.User;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static spark.Spark.delete;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 *
 * @author Pedro Dias
 */
public class Main {

    public static void main(String[] args) {
        try {
            //staticFileLocation("/public");
            externalStaticFileLocation(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8") + "/../../../algorithmi-web");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        get("/quiz/:id", (request, response) -> {
            return "Hello World";
        });

        post("/quiz", (request, response) -> {
            return "Hello World";
        });

        put("/quiz/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/quiz/:id", (request, response) -> {
            return "Hello World";
        });

        get("/question/:id", (request, response) -> {
            return "Hello World";
        });

        post("/question", (request, response) -> {
            return "Hello World";
        });

        put("/question/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/question/:id", (request, response) -> {
            return "Hello World";
        });

        get("/user/:id", (request, response) -> {
        //http://127.0.0.1:4567/#students/56
            User userExample = new User("{'id_User':'621','name':'João Santos','password':'13456','dateBirth':'12/03/1988','email':'joaoSantos@hotmail.com','type':'3'}");
            return userExample.toString();
        });

        post("/user", (request, response) -> {
            //http://127.0.0.1:4567/#login
            //separador registar
            
            try {
                User newUser;
                newUser = new User(java.net.URLDecoder.decode(request.body(), "UTF-8"));
                newUser.regist();
            System.out.println(newUser.toString());
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Devolve OK
            response.status(200);
            //E os dados do novo utilizador
            return "{\"resposta\":\"Utilizador inserido com sucesso\"}";
        });

        put("/user/:id", (request, response) -> {
            return "Hello World";
        });

        delete("/user/:id", (request, response) -> {
            return "Hello World";
        });

    }
}
