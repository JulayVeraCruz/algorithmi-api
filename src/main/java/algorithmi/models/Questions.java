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

import Utils.utils;
import algorithmi.models.InputOutput;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Statement;

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

    // EXEMPLO DE STRING RECEBIDA [title="olol", category="1", description="olololo", difficulty="1", image="", in="9", out="9"]
    public Questions(String data) throws Exception {
        System.out.println("Quest " + data);
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject question = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(question.entrySet());

        //Associa os dados ao objecto Question
        //Se o id for nulo (é uma institution nova)
        if (question.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = question.get("id").getAsInt();
        }
        this.title = question.get("title").getAsString();
        this.category = question.get("category").getAsInt();
        this.description = question.get("description").getAsString();
        this.image = utils.b64ToImage(question.get("image").getAsString(), "inst" + id);
        this.algorithm = question.get("algorithm").getAsString();
        this.difficulty = question.get("difficulty").getAsInt();
        this.ios = (JsonArray) jsonParser.parse(question.get("ios").getAsString());
        //http://stackoverflow.com/questions/34120882/gson-jelement-getasstring-vs-jelement-tostring

        //regist(id, title, category, description, image, algorithm, difficulty);
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

    private void validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
    }

    public static int getLastID() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblquestions");
    }

    /**
     * apaga um curso com o id
     *
     * @param id
     */
    public int deleteCourse(int id) throws Exception {
        int status = 400;
        utils utils = new utils();
        /*        boolean deleted = utils.deleteRegist(id, "tblQuestions");
         if (deleted) {
         status = 200;
         }*/
        return status;
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
        Statement stmtt = utils.connectDatabase();

        stmtt.execute(update);

        //ResultSet res = stmtt.getResultSet();
//        System.out.println("result update Course " + res.rowUpdated());
        stmtt.close();
        return status;
    }

    public static String getAll() {
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

            return questionsList.toString();
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Instituições.\"}";
        }
    }

    public int insert(String data) throws Exception {
        int status = 0;
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject question = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(question.entrySet());

        String insert = "Insert into tblquestions values(" + id + ", '" + title + "'," + category + ",'" + description + "','" + image + "','" + algorithm + "'," + difficulty + ")";
        status = utils.executeIUDCommand(insert);
        //Por cada io enviado da view
        for (JsonElement o : ios) {
            //Atribui o id da question
            o.getAsJsonObject().addProperty("questionID", id);
            //Cria uma nova entrada na tabela de IOS
            InputOutput newIO = new InputOutput(o.getAsJsonObject().toString());
            newIO.insert();
        }
        return status;
    }

}
