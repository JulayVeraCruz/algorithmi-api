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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Statement;

/**
 *
 * @author Pedro Dias
 */
public class Question {

    private int id;
    private String title;
    private int category;
    private String description;
    private String image;
    private String algorithm;
    private int difficulty;

    // EXEMPLO DE STRING RECEBIDA [title="olol", category="1", description="olololo", difficulty="1", image="", in="9", out="9"]
    public Question(String data) throws Exception {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject Question = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(Question.entrySet());
        /**
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         */

        //validateData();
        //Associa os dados ao objecto Question
        this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        this.title = Question.get("title").getAsString();
        this.category = Question.get("category").getAsInt();
        this.description = Question.get("description").getAsString();
        this.image = Question.get("image").getAsString();
        this.algorithm = Question.get("algoritmo").getAsString();
        this.difficulty = Question.get("difficulty").getAsInt();
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

    public int regist(int id, String title, int category, String description, String image, String algorithm, int difficulty) throws Exception {
        int status = 0;

        //as credenciais de ligaçao estao agora em utils
        Statement stmtt = utils.connectDatabase();
        stmtt.execute("INSERT INTO tblquestions VALUES(" + this.id + "," + this.title + "," + this.category + "," + this.description + "," + this.image + "," + this.algorithm + "," + this.difficulty + ")");
        stmtt.getConnection().close();
        stmtt.close();

        return status;
    }

    public int regist() throws Exception {
        int status = 0;

        //as credenciais de ligaçao estao agora em utils
        Statement stmtt = utils.connectDatabase();
        stmtt.execute("INSERT INTO tblquestions VALUES(" + id + "," + title + "," + category + "," + description + "," + image + "," + algorithm + "," + difficulty + ")");
        stmtt.getConnection().close();
        stmtt.close();

        return status;
    }

}
