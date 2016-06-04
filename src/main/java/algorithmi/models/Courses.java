/*
 * Copyright 2016 FilipeRosa.
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
package algorithmi.models;

import Utils.utils;
import algorithmi.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author FilipeRosa
 */
public class Courses {

    private int id;
    private String name;
    private int school;
    private String image;

    //--------------------------------------------------------------------------------------
    //------------------------------- Listar Cursos ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAll(Response response) {
        try {
            String query = "select tblCourses.*,tblSchools.name as schoolName,tblSchools.institution from tblCourses join tblSchools on tblSchools.id=tblCourses.school";
            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter os cursos.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Instituição ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getCourseData(Response response, String id) {

        try {
            //Obtem a instituicao
            String queryCourse = "select tblCourses.*,tblSchools.institution from tblCourses join tblSchools on tblSchools.id=tblCourses.school where tblCourses.id=" + id;
            JsonObject institution = utils.executeSelectCommand(queryCourse).get(0).getAsJsonObject();
            //Devolve 'OK'
            response.status(200);
            //E uma mensagem
            return institution.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter o curso com o id:" + id + ".\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Curso ---------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {

        try {
            //Obtém o ultimo ID
            this.id = utils.getLastID("tblcourses") + 1;

            //converte a imagem em b64 para ficheiro e guarda o nome
            this.image = utils.b64ToImage(image, "course" + id);

            boolean existErro = false;
            String[] erros = validateData();
            for (int i = 0; i < erros.length; i++) {
                if (erros[i] == null);
                {
                    existErro = existErro || false;
                }
            }
            if (!existErro) {
                String insert = "INSERT INTO tblcourses values(" + id + "," + '"' + name + '"' + "," + school + "," + '"' + image + '"' + ")";
                //Insere, devolve o estado
                response.status(utils.executeIUDCommand(insert));
                // E uma mensagem
                return "{\"text\":\"Curso inserido com sucesso!\"}";
            }

        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir o Curso.\"}";
    }

    //--------------------------------------------------------------------------------------
    //------------------------------------ Update Curso ------------------------------------
    //--------------------------------------------------------------------------------------   
    public String updateCourse(Response response) {
        try {
            //converte a imagem em b64 para ficheiro e guarda o nome
            this.image = utils.b64ToImage(image, "course" + id);
            String update = "UPDATE tblCourses SET name=" + '"' + name + '"' + ",school=" + school + ",image=" + '"' + image + '"' + " where id=" + id;
            response.status(utils.executeIUDCommand(update));
            return "{\"text\":\"Curso alterado com sucesso!\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não é possível alterar o Curso porque possuí alunos/professores associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível alterar o Curso.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //---------------------------------- Apagar Curso --------------------------------------
    //--------------------------------------------------------------------------------------
    public static String delete(Response response, int id) {
        try {
            String deleted = utils.deleteRegist(id, "tblcourses");
            response.status(utils.executeIUDCommand(deleted));
            return "{\"text\":\"Curso apagado com sucesso.\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível apagar o Curso porque possuí alunos/professores associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível apagar o Curso.\"}";

    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    /**
     * faz a validação dos dados com o retorno de um array podendo conter ou nao
     * mensagens de erro
     *
     * @return String[]
     */
    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean nameValid = utils.isString(name, true);//0
        boolean schoolNumberValid = utils.isNumber(school + "", false);//1
//        boolean imageValid = utils.isString(image);//2

        valid = nameValid && schoolNumberValid; //&& imageValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome invalido";
            }
            if (!schoolNumberValid) {
                respostasErro[1] = "Escola invalida";
            }
//            if (!imageValid) {
//                respostasErro[2] = "path invalido";
//            }

        }

        return respostasErro;
    }

}
