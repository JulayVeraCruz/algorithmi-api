/*
 * Copyright 2016 Pedro Dias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithmi;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

/**
 *
 * @author Pedro Dias
 */
public class Main {

  public static void main(String[] args) {
    staticFileLocation("/public");

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
      return "{\"username\": \"johndoe\", \"escola\":\"xpto\"}";
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
