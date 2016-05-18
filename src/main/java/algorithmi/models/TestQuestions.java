/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
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
    private float quotation;

    public TestQuestions(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject testQuestions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(testQuestions.entrySet());
        
        this.questionID = testQuestions.get("questionID").getAsInt();
        this.testID = testQuestions.get("testID").getAsInt();
        this.quotation = testQuestions.get("quotation").getAsFloat();
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
    
    public float getQuotation() {
        return quotation;
    }

    public void setQuotation(float quotation) {
        this.quotation = quotation;
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

//--------------------------------------------------------------------------------------
//------------------------------- Registar TestQuestion --------------------------------
//-------------------------------------------------------------------------------------- 
    
    public int regist() throws Exception {
        int status = 400;
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {
            //executa driver para ligar à base de dados
            String insert = "INSERT INTO tblTestQuestions values(" + questionID + "," + testID + "," + quotation +")";
            String tt = utils.commandMySQLToJson_String(insert);
            System.out.println("Inseridos os seguintes valores: " + tt);
        }
        return status;
    }
    
//--------------------------------------------------------------------------------------
//--------------------------------- Update TestQuestion --------------------------------
//-------------------------------------------------------------------------------------- 

public void updateTestQuestions(int testID, int questionID) throws Exception {

        //executa driver para ligar à base de dados
        String update = "UPDATE tblTestQuestions " + "SET quotation=" + quotation + " where questionID=" + questionID + " AND testID=" + testID + ")";

        String upd = utils.commandMySQLToJson_String(update);

        System.out.println("Alterado a quotação:  " + upd);
    }   
    
    
//--------------------------------------------------------------------------------------
//--------------------------------- Delete TestQuestion --------------------------------
//-------------------------------------------------------------------------------------- 
    
    public static String deleteTestQuestions(int testID, int questionID) throws Exception {
        String delete = "regist not deleted";
        //executa driver para ligar à base de dados
        String delet = "DELETE FROM `tblTestQuestions` where questionID=" + questionID + " and testID=" + testID;
        String tt = utils.commandMySQLToJson_String(delet);
        return tt;
    }    
    
//--------------------------------------------------------------------------------------
//------------------------------- Validar Dados ----------------------------------------
//--------------------------------------------------------------------------------------    
    
    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean testIDValid = utils.isNumber(testID + "", false);//0
        boolean questionIDValid = utils.isNumber(questionID + "", false);//1
        boolean quotationValid = utils.isNumber(quotation + "", false);//2
        

        valid = testIDValid && questionIDValid && quotationValid;
        if (!valid) {
            if (!testIDValid) {
                respostasErro[0] = "TestID Inválido";
            }
            if (!questionIDValid) {
                respostasErro[1] = "QuestionID Inválido";
            }
            if (!quotationValid) {
                respostasErro[2] = "Quotação Inválida";
            }
        }
        return respostasErro;
    }

    
}
