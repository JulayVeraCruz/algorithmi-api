/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Pedro Batista
 */
public class TestQuestions {
    private int testID;
    private int questionID;

    public TestQuestions(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject testQuestions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(testQuestions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        this.testID = testQuestions.get("testID").getAsInt();
        this.questionID = testQuestions.get("questionID").getAsInt();
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }
    
    public void regist() {
        //Insere na BD
    }
    
    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

    private void validateData() {
        //Se estiver tudo OK, inserer na BD
        regist();
    }
}
