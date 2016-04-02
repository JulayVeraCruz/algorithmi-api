package algorithmi;

import algorithmi.Models.User;
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
        User user = new User();
      return user.getInfo();
    });

    post("/user", (request, response) -> {
      return "Hello World";
    });

    put("/user/:id", (request, response) -> {
      return "Hello World";
    });

    delete("/user/:id", (request, response) -> {
      return "Hello World";
    });

  }
}
