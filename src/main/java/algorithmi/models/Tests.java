
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
    private float quotation;
    private int student;
    private int matrixTest;

    public Tests(String data) throws Exception {

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
    }

    public Tests(String data, int id) throws Exception {

        JsonParser jsonParser = new JsonParser();
        JsonObject tests = (JsonObject) jsonParser.parse(data);
        System.out.println(tests.entrySet());
        
        this._id = id; 
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
   
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }
    
    //Vai buscar oLast ID da tabela Testes
    public static int getLastID_Tests() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tbltests");
    }

//--------------------------------------------------------------------------------------
//------------------------------------ Registar Teste ----------------------------------
//-------------------------------------------------------------------------------------- 
    
    public int regist() throws Exception {

        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        int status = 400;
        if (!existErro) {
            String insert = "INSERT INTO tblTests values(" + _id + "," + '"' + quotation + '"' + "," + student + "," + '"' + matrixTest + '"' + ")";
            String tt = utils.commandMySQLToJson_String(insert);
            System.out.println(" Registo Teste nº " + _id);
        }
        return status;
    }
    
    //public int regist() throws Exception {

        //boolean existErro = false;
        //String[] erros = validateData();
        //for (int i = 0; i < erros.length; i++) {
            //if (erros[i] == null);
            //{
               //existErro = existErro || false;
            //}
        //}
        //int status = 400;
        //if (!existErro) {

            //Statement stmtt = connectDatabase();

            //stmtt.execute("INSERT INTO tblTests values(" + _id + "," + '"' + quotation + '"' + "," + student + "," + '"' + matrixTest + '"' + ")");
            //ResultSet res = stmtt.getResultSet();
            //while (res.next()) {
                //status = 200;
            //}
            //System.out.println(" insert Test nº " + _id);

            //stmtt.getConnection().close();
            //stmtt.close();
        //}
        //return status;
    //}
    
//--------------------------------------------------------------------------------------
//-------------------------------------- Update Teste ----------------------------------
//-------------------------------------------------------------------------------------- 
    
    public int updateTests(int _id) throws Exception{
        int status = 0;
   
            String update = "UPDATE tblTests SET quotation=" + quotation + ",student=" + student + ",matrixTest=" + matrixTest + " where _id=" + _id;
            String updated = utils.commandMySQLToJson_String(update);
            return status;
    }
    //public int updateTests(int _id) {
        //int status = 0;
        //try {
            //executa driver para ligar à base de dados
            //Statement stmtt = utils.connectDatabase();

            //stmtt.execute("UPDATE tblTests SET quotation=" + quotation + ",student=" + student + ",matrixTest=" + matrixTest + " where _id=" + _id + ")");

            //ResultSet res = stmtt.getResultSet();

            //System.out.println("result update Test " + res);
            //status = 1;
            //stmtt.close();
        //} catch (Exception ex) {
            //Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        //}
        //return status;
    //}
    
//--------------------------------------------------------------------------------------
//------------------------------------ Apagar Teste ------------------------------------
//-------------------------------------------------------------------------------------- 
    
    public String deleteTests(int _id) throws Exception {
        int status = 400;
        String deleted = utils.deleteRegist(_id, "tblTests");
        String del=utils.commandMySQLToJson_String(deleted);
        return del;
    }
    
    //public int deleteTests(int _id) throws Exception {
        //int status = 400;
        //utils utils = new utils();
        //boolean deleted = utils.deleteRegist(_id, "tblTests");
        //if (deleted) {
            //status = 200;
        //}
        //return status;
    //}

//--------------------------------------------------------------------------------------
//------------------------------------ Listar Teste ------------------------------------
//-------------------------------------------------------------------------------------- 
    
    public static String listTests_WEB() throws Exception {
        //FALTA FAZER O SELECT
        String query = "SELECT tblInstitutions.`name` as Institutions,tblInstitutions.`address` as Institutions,tblSchools where tblCourses.school=tblSchools._id";
        String teste = utils.commandMySQLToJson_String(query);
        return teste;
    }
    
    //public static String listTests_WEB() throws Exception {
        //FALTA FAZER O SELECT
        //String query = "SELECT tblInstitutions.`name` as Institutions,tblInstitutions.`address` as Institutions,tblSchools where tblCourses.school=tblSchools._id";
        //String obj = utils.querysToJson_String(query);
        //System.out.println("list tests  " + obj);
        //return obj;
    //}
    
//--------------------------------------------------------------------------------------
//--------------------------------- Validar Dados Teste --------------------------------
//-------------------------------------------------------------------------------------- 
    
    private String[] validateData() {
        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean matrixTestValid = utils.isNumber(Integer.toString(matrixTest),false);//0
        boolean quotationValid = utils.isValidFloat(quotation + "",false);//1
        boolean studentValid = utils.isNumber(Integer.toString(student),false);//2

        valid = matrixTestValid && quotationValid && studentValid;
        if (!valid) {
            if (!matrixTestValid) {
                respostasErro[0] = "MatrixTest do Teste inválido";
            }
            if (!quotationValid) {
                respostasErro[1] = "Quotação do Teste inválido";
            }
            if (!studentValid) {
                respostasErro[2] = "Estudante inválido";
            }
        }
        return respostasErro;
    }  
}