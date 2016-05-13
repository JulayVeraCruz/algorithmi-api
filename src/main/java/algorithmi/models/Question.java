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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Dias
 */
public class Question {

    private int _id;
    private String title;
    private int category;
    private String description;
    private String image;
    private String algorithm;
    private int difficulty;


    // EXEMPLO DE STRING RECEBIDA [title="olol", category="1", description="olololo", difficulty="1", image="", in="9", out="9"]
    public Question(String data) throws Exception{
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
        this._id = getLastID()+1; //ir buscar o max id da bd + 1
        this.title = Question.get("title").getAsString();
        this.category = Question.get("category").getAsInt();
        this.description = Question.get("description").getAsString();
        this.image = Question.get("image").getAsString();
        this.algorithm = Question.get("algoritmo").getAsString();
        this.difficulty = Question.get("difficulty").getAsInt();
        
        regist(_id, title, category, description, image, algorithm, difficulty);
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

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param _id the _id to set
     */
    public void setId(int _id) {
        this._id = _id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the category
     */
    public int getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the algorithm
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm the algorithm to set
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public static int getLastID() throws Exception{
        utils getid = new utils();
        return getid.getLastID("tblquestions");
    }

     /**
     * apaga um curso com o _id
     *
     * @param _id
     */
    public int deleteCourse(int _id) throws Exception {
        int status = 400;
        utils utils = new utils();
        boolean deleted = utils.deleteRegist(_id, "tblQuestions");
        if (deleted) {
            status = 200;
        }
        return status;
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param _id
     * @return
     */
    public int updateCourse(int id) throws Exception {
        int status = 0;
        String update = "UPDATE tblQuestions SET "
                + "name=" + '"' + _id + '"' + ","
                + "title=" + title + ","
                + "category=" +'"' + category + '"' +  ","
                + "description=" + '"' + description + '"' + ","
                + "image=" + '"' + image + '"' +  " "
                + "algorithm=" + '"' + algorithm + '"' +  " "
                + "difficulty=" + '"' + getDifficulty() + '"' +  " "
                + "where _id=" + id;
        Statement stmtt = utils.connectDatabase();

        stmtt.execute(update);

        //ResultSet res = stmtt.getResultSet();
//        System.out.println("result update Course " + res.rowUpdated());
        stmtt.close();
        return status;
    }
    
    public int regist(int _id, String title, int category, String description, String image, String algorithm, int difficulty) {
        int status = 0;
        try {
           //as credenciais de ligaçao estao agora em utils
            Statement stmtt = utils.connectDatabase();
            
            stmtt.execute("INSERT INTO tblQuestions values(" + this._id + "," +  this.title + "," + this.category + "," +  this.description + "," +  this.image + "," +  this.algorithm + "," +  this.getDifficulty() + ")");
            stmtt.getConnection().close();
            stmtt.close();
            
        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

    /**
     * @return the difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
