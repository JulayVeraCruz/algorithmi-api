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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        this._id = getLastID_Tests()+ 1; //ir buscar o max id da bd + 1
        this.matrixTest = tests.get("matrixTest").getAsInt();
        this.quotation = tests.get("quotation").getAsFloat();
        this.student = tests.get("student").getAsInt();
        
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {

            regist();
        }
    
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
    
    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

    public int updateTests(int _id) {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("UPDATE tblTests " + "SET matrixTest=" + matrixTest +  ",quotation=" + quotation +",student=" + student + " where _id=" + _id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update tests " + res);
            status = 1;
            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            System.out.println("antes insert ");

            stmtt.execute("INSERT INTO tblTests values(" + _id + "," + '"' + matrixTest + '"' + "," + quotation + "," + '"' + student + '"' + ")");

            ResultSet res = stmtt.getResultSet();
            status = 1;//sem erros
            System.out.println(" insert new tests id" + res.getString(1));

            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("SQL ERROR regist " + ex);
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
    
    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean matrixTestValid = utils.isNumber(Integer.toString(matrixTest));//0
        boolean quotationValid = utils.isFloat(Float.toString(quotation));//1
        boolean studentValid = utils.isNumber(Integer.toString(matrixTest));//2

        valid = matrixTestValid && quotationValid && studentValid; //&& imageValid;
        if (!valid) {
            if (!matrixTestValid) {
                respostasErro[0] = "Nome invalido";
            }
            if (!quotationValid) {
                respostasErro[1] = "Escola invalida";
            }
            if (!studentValid) {
                respostasErro[2] = "path invalido";
            }

        }

        return respostasErro;
    }
    
}
