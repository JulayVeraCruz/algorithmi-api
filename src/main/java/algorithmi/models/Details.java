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
public class Details {
    private int matrixTestID;
    private int categoryID;
    private int numberOfQuestions;

    public Details(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject details = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(details.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        this.matrixTestID = details.get("matrixTestID").getAsInt();
        this.categoryID = details.get("categoryID").getAsInt();
        this.numberOfQuestions = details.get("numberOfQuestions").getAsInt();
    }

    public int getMatrixTestID() {
        return matrixTestID;
    }

    public void setMatrixTestID(int matrixTestID) {
        this.matrixTestID = matrixTestID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
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
