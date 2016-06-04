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
package algorithmi.Models;

import Utils.SQL;
import Utils.utils;
import algorithmi.Main;
import algorithmi.models.InputOutput;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author Pedro Dias
 */
public class Questions {

    private int id;
    private String title;
    private int category;
    private String description;
    private String image;
    private String algorithm;
    private int difficulty;
    private JsonArray ios;

    //--------------------------------------------------------------------------------------
    //------------------------------- Listar Perguntas ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAll(Response response) {
        try {
            String queryQuestions = "select tblquestions.*,tblcategories.description as categoryName  from tblquestions join tblcategories on tblquestions.category=tblcategories.id ";

            JsonArray questionsList = utils.executeSelectCommand(queryQuestions);

            //Por cada pergunta recolhe a lista de IO e as resolucoes nas devidas linguagens
            for (JsonElement o : questionsList) {
                //Obtem o id da question
                int questionID = o.getAsJsonObject().get("id").getAsInt();
                //Adiciona o array de io dessa pergunta
                JsonArray IOList = utils.executeSelectCommand("select * from tblinputoutputs where question=" + questionID);
                o.getAsJsonObject().add("IOs", IOList);
                //Adiciona o array de resolucoes nas devidas linguagens
                JsonArray languagesList = utils.executeSelectCommand("select * from tblcodelangs where questionID=" + questionID);
                o.getAsJsonObject().add("languages", languagesList);
            }

            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return questionsList.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter as Perguntas.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Instituição ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getQuestionData(Response response, String id) {

        try {
            //Obtem a instituicao
            String queryIns = "select tblquestions.*,tblcategories.description as categoryName  from tblquestions join tblcategories on tblquestions.category=tblcategories.id where tblquestions.id=" + id;
            JsonObject question = utils.executeSelectCommand(queryIns).get(0).getAsJsonObject();
            //Obtem os ios da pergunta
            //Adiciona o array de io dessa pergunta
            JsonArray IOList = utils.executeSelectCommand("select * from tblinputoutputs where question=" + id);
            question.getAsJsonObject().add("IOs", IOList);
            //Adiciona o array de codigos dessa pergunta
            JsonArray codeList = utils.executeSelectCommand("select * from tblcodelangs where questionID=" + id);
            question.getAsJsonObject().add("Codes", codeList);
            //Devolve 'OK'
            response.status(200);
            //E uma mensagem
            return question.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter a Pergunta com o id:" + id + ".\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Pergunta ---------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {

        System.out.println(ios);

        try {
            //Obtém o ultimo ID
            this.id = utils.getLastID("tblquestions") + 1;

            //converte a imagem em b64 para ficheiro e guarda o nome
            this.image = utils.b64ToImage(image, "quest" + id);

            String insert = "Insert into tblquestions values(" + id + ", '" + title + "'," + category + ",'" + description + "','" + image + "','" + algorithm + "'," + difficulty + ")";
            response.status(utils.executeIUDCommand(insert));
            //Por cada io enviado da view
            for (JsonElement o : ios) {
                //Atribui o id da question
                o.getAsJsonObject().addProperty("questionID", id);
                //Cria uma nova entrada na tabela de IOS
                InputOutput newIO = new InputOutput(o.getAsJsonObject().toString());
                newIO.insert(response);
            }
            // E uma mensagem
            return "{\"text\":\"Pergunta inserida com sucesso!\"}";

        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir a Pergunta.\"}";
    }

    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    private String[] validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        return null;
    }

    /**
     * apaga um curso com o id
     *
     * @param id
     */
    public static String delete(Response response, int id) {
        try {
            String deleted = utils.deleteRegist(id, "tblquestions");
            String deleteIO = "Delete from tblinputoutputs where question=" + id;
            utils.executeIUDCommand(deleteIO);
            response.status(utils.executeIUDCommand(deleted));
            return "{\"text\":\"Curso apagado com sucesso.\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível apagar o Curso porque possuí alunos/professores associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível apagar o Curso.\"}";
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param id
     * @return
     */
    public int updateCourse(int id) throws Exception {
        int status = 0;
        String update = "UPDATE tblQuestions SET "
                + "name=" + '"' + id + '"' + ","
                + "title=" + title + ","
                + "category=" + '"' + category + '"' + ","
                + "description=" + '"' + description + '"' + ","
                + "image=" + '"' + image + '"' + " "
                + "algorithm=" + '"' + algorithm + '"' + " "
                + "difficulty=" + '"' + difficulty + '"' + " "
                + "where id=" + id;
        SQL sql = utils.connectDatabase();

        sql.st.execute(update);

        //ResultSet res = stmtt.getResultSet();
//        System.out.println("result update Course " + res.rowUpdated());
        sql.close();
        return status;
    }

}
