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
import java.sql.SQLException;
import java.sql.Statement;
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

    private Connection connect = null;
    PreparedStatement preparedStatement = null;

    // EXEMPLO DE STRING RECEBIDA [title="olol", category="1", description="olololo", difficulty="1", image="", in="9", out="9"]
    public Question(String data) {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject Question = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(Question.entrySet());
        /**
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         */
        regist();
        validateData();
        //Associa os dados ao objecto Question
        this._id = getLastID() + 1; //ir buscar o max id da bd + 1 
        this.title = Question.get("title").getAsString();
        this.category = Question.get("category").getAsInt();
        this.description = Question.get("description").getAsString();
        this.image = Question.get("image").getAsString();
        this.algorithm = Question.get("algoritmo").getAsString();
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

    public static int getLastID() {
        utils getid = new utils();
        return getid.getLastID("tblquestions");
    }


        public int regist() {
        int status = 0;
        try {
            //executa driver para ligar � base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz liga��o � base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");
            //inicia uma declaracao
            Statement stmtt = (Statement) connn.createStatement();
            //a declaracao executa o comando mysql
            stmtt.execute("INSERT INTO tblQuestions values(" + _id + "," + '"' + title + '"' + "," + category + "," + '"' + description + '"' + "," + image + "," + algorithm + "," + difficulty + ")");
            status = 1;//sem erros
            //fecha a declaracao
            stmtt.close();
            //fecha a ligacao
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(algorithmi.models.Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("SQL ERROR regist " + ex);
            Logger.getLogger(algorithmi.models.Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

}
