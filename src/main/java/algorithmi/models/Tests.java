
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

        this._id = getLastID_Tests() + 1; //ir buscar o max id da bd + 1
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

    /**
     * obtem o maximo id utilizado na tabela tbltests
     *
     * @return int
     */
    public static int getLastID_Tests() {
        utils getid = new utils();
        return getid.getLastID("tblTests");
    }

    /**
     * apaga um teste com o _id
     *
     * @param _id
     */
    public void deleteTest(int _id) {
        utils utils = new utils();
        utils.deleteRegist(_id, "tbltests");
    }

    /**
     * introduz um novo teste
     *
     * @return
     */
    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();

            stmtt.execute("INSERT INTO tbltests values()");

            ResultSet res = stmtt.getResultSet();
            status = 1;//sem erros
            System.out.println(" tests" + res.getString(1));

            stmtt.close();
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

    /**
     * para actualizar/alterar os dados de um registo na tabela tests
     *
     * @param _id
     * @return
     */
    public int updateTest(int _id) {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();

            stmtt.execute("UPDATE tbltests " + "SET matrixTest=" + matrixTest + ",quotation=" + quotation + ",student=" + student + " where _id=" + _id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update course " + res);
            status = 1;
            stmtt.close();
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    private String[] validateData() {
        String respostasErro[] = new String[1];
        boolean valid = false;

        boolean validQuotation = utils.isValidFloat(quotation + "");

        valid = validQuotation;
        if (!valid) {
            if (!validQuotation) {
                respostasErro[0] = "Nota invalida";
            }
        }
        return respostasErro;
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
}
