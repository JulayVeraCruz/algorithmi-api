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
public class Tests {
    private int _id;
    private int matrixTest;
    private float quotation;
    private int student;

    public Tests(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject tests = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(tests.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        this._id = getLastID_Tests()+ 1; //ir buscar o max id da bd + 1
        this.matrixTest = tests.get("matrixTest").getAsInt();
        this.quotation = tests.get("quotation").getAsFloat();
        this.student = tests.get("student").getAsInt();
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getMatrixTest() {
        return matrixTest;
    }

    public void setMatrixTest(int matrixTest) {
        this.matrixTest = matrixTest;
    }

    public float getQuotation() {
        return quotation;
    }

    public void setQuotation(float quotation) {
        this.quotation = quotation;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }
    
    public static int getLastID_Tests() {
        utils getid = new utils();
        return getid.getLastID("tblTests");
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
