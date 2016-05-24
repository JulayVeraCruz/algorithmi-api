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
    
        this.numberOfQuestions = details.get("numberOfQuestions").getAsInt();
        this.matrixTestID = details.get("matrixTestID").getAsInt();
        this.categoryID = details.get("categoryID").getAsInt();      
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
 
//--------------------------------------------------------------------------------
//--------------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

//--------------------------------------------------------------------------------------
//------------------------------- Registar Detalhe -------------------------------------
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
            String insert = "INSERT INTO tblDetails values(" + numberOfQuestions + "," + matrixTestID + "," + categoryID +")";
            String tt = utils.executeSelectCommand(insert).toString();
            System.out.println("Inseridos os seguintes valores: " + tt);

        }
        return status;
    }
    
//--------------------------------------------------------------------------------------
//--------------------------------- Update Detalhes ------------------------------------
//-------------------------------------------------------------------------------------- 

public void updateDetails(int matrixTestID, int categoryID) throws Exception {

        //executa driver para ligar à base de dados
        String update = "UPDATE tblDetails " + "SET numberOfQuestions=" + numberOfQuestions + " where matrixTestID=" + matrixTestID + " AND categoryID=" + categoryID + ")";
        String upd = utils.executeSelectCommand(update).toString();
        System.out.println("Alterado o número de questões  " + upd);
    }   
    
    
//--------------------------------------------------------------------------------------
//--------------------------------- Delete Detalhes ------------------------------------
//-------------------------------------------------------------------------------------- 
    
    public static String deleteDetails(int matrixTestID, int categoryID) throws Exception {
        String delete = "regist not deleted";
        //executa driver para ligar à base de dados
        String delet = "DELETE FROM `tblDetails` where matrixTestID=" + matrixTestID + " and categoryID=" + categoryID;
        String tt = utils.executeSelectCommand(delet).toString();
        return tt;
    }    
    
//--------------------------------------------------------------------------------------
//------------------------------- Validar Dados ----------------------------------------
//--------------------------------------------------------------------------------------    
    
    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean numberOfQuestionsValid = utils.isNumber(numberOfQuestions + "", false);//0
        boolean matrixTestIDValid = utils.isNumber(matrixTestID + "", false);//1
        boolean categoryIDValid = utils.isNumber(categoryID + "", false);//2
        

        valid = numberOfQuestionsValid && matrixTestIDValid && categoryIDValid;
        if (!valid) {
            if (!numberOfQuestionsValid) {
                respostasErro[0] = "Número de Questões Inválida";
            }
            if (!matrixTestIDValid) {
                respostasErro[1] = "MatrixTeste Inválida";
            }
            if (!categoryIDValid) {
                respostasErro[2] = "Categoria Inválida";
            }
        }
        return respostasErro;
    }
}
