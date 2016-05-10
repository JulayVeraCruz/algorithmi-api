/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;

/**
 *
 * @author Pedro Batista
 */
public class utils {

    /**
     * Faz a ligação a base de dados com as devidas credecias de acesso
     *
     * @return Statment
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static Statement connectDatabase() throws Exception {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        //faz ligação à base de dados
        Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");
//        Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fralgo?zeroDateTimeBehavior=convertToNull", "root", "root");
        Statement stmtt = (Statement) connn.createStatement();
        return stmtt;
    }

    /**
     * Converte em Json Object a query solicitada n sendo apresentados os id
     * necessarios (numTabelas) para uma futura utilizaçao.A query tera de ter
     * nos ultimos campos os id nao sendo apresentados na view
     *
     * @param query
     * @param numTabelas
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static String querysToJson_String(String query) throws Exception {
        JsonObject obj = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Statement stmtt = connectDatabase();

        stmtt.execute(query);

        ResultSet res = stmtt.getResultSet();

        ResultSetMetaData metadata = (ResultSetMetaData) res.getMetaData();

        Gson gson = new Gson();
        obj.add("query", jsonArray);
        int total_rows = metadata.getColumnCount();

        while (res.next()) {
            JsonObject row = new JsonObject();
            jsonArray.add(row);
            for (int i = 1; i <= total_rows; i++) {
//                System.out.println("col name " + metadata.getColumnLabel(i));
                row.add(metadata.getColumnLabel(i), gson.toJsonTree(res.getObject(i)));
            }
        }

        return obj.toString();
    }
    /**
     * Devolve o último id da tabela nomeTabela na base de dados "algo" de
     * algoritmi.ipt.pt retorna zero se n existitem dados
     *
     * @param nomeTabela
     * @return int
     */
    public int getLastID(String nomeTabela) throws Exception {
        int id = 0;

        Statement stmtt = connectDatabase();

        stmtt.execute("select " + nomeTabela + "._id from " + nomeTabela + " order by _id desc limit 1");
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            id = Integer.parseInt(res.getString("_id"));
        }
        return id;
    }

    /**
     * retorna o campo name respectivo do _id de uma tabela
     *
     * @param _id
     * @param tabela
     * @return
     */
    public String getName(int _id, String tabela) throws Exception {

        String name = null;
        Statement stmtt = connectDatabase();

        stmtt.execute("SELECT name FROM `" + tabela + "` where _id=" + _id + "");
//        ResultSet res = connectDatabase(mySQL);//se for apenas um insert o res=null
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            name = res.getString("name");
        }
        return name;
    }

    /**
     * apaga o registo com o _id de uma tabela desde que só possua UM campo
     * (_id) como chave primaria
     *
     * @param _id
     * @param tabela
     * @return
     */
    public boolean deleteRegist(int _id, String tabela) throws Exception {
        boolean deleted = false;
        Statement stmtt = connectDatabase();

        stmtt.execute("DELETE FROM " + tabela + " where _id=" + _id + "");
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            deleted = true;
        }
        stmtt.close();
        return deleted;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }
    
    //---------------------------------------------------------------------------------
    //------------------Expressoes regulares-------------------------------------------
    //---------------------------------------------------------------------------------
    

    /**
     * verifica se a string é composta apenas por algarismos
     *
     * @param data
     * @return bolean
     */
    public static boolean isNumber(String data, boolean canBeNul) {
        if (data == null && canBeNul) {
            return true;
        } else {
            return Pattern.matches("[0-9]+", data);
        }
    }

    /**
     * verifica se e float caso data possa ser null devolve true
     *
     * @param data
     * @param canBeNul
     * @return
     */
    public static boolean isValidFloat(String data, boolean canBeNul) {
        if (data == null && canBeNul) {
            return true;
        } else {
            return Pattern.matches("[0-9.,]+", data);
        }
    }

    public static boolean isFloat(String data, boolean canBeNul) {
        if (data == null && canBeNul) {
            return true;
        } else {
            return Pattern.matches("[0-9.,]+", data);
        }
    }

    /**
     * verifica se a string é composta apenas por algarismos e se possui o
     * tamanho (limit) correcto
     *
     * @param data
     * @param limit
     * @return boolean
     */
    public static boolean isNumberLimited(String data, int limit, boolean canBeNul) {
        if (data == null && canBeNul) {
            return true;
        } else {
            /**
             * [admite numeros] {tamanho min=1, max=limit} caso data seja null
             * devolve false
             */
            return Pattern.matches("[0-9]{1,limit}", data);
        }
    }

    /**
     * verifica se a string tem apenas letras,algarismos e espaço
     *
     * @param data
     * @return bollean
     */
    public static boolean isString(String data, boolean canBeNul) {
        //admite letras,numeros e espaço
        if (data == null && canBeNul) {
            return true;
        } else {
            return Pattern.matches("[a-zA-Z0-9 ]+", data);
        }

    }

    /**
     * verifica se a string tem apenas letras,algarismos e espaço e se possui o
     * tamanho (limit) correcto
     *
     * @param data
     * @param limit
     * @return boolean
     */
    public static boolean isStringLimited(String data, int limit, boolean canBeNul) {
        /**
         * [admite letras,numeros e espaço] {tamanho min=1, max=limit} caso data
         * seja null devolve false
         */
        if (data == null && canBeNul) {
            return true;
        } else {
            return Pattern.matches("[a-zA-Z0-9 ]{1,limit}", data);
        }
    }

    /**
     * verifica se a data tem formato valido o formato admitido é dd/MM/yyyy
     *
     * @param dateToValidate
     * @return boolean
     */
    public static boolean isThisDateValid(String dateToValidate) {
        //validar data formato proposto dd/MM/yyyy 
        //dateFormat="dd/MM/yyyy"

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * verifica se o username tem formato válido apenas admite letras e
     * algarismos
     *
     * @param username
     * @return boolean
     */
    public static boolean isUsernameValid(String username) {

        //ver se tem espaços ou carateres especiais- se tiver devolve falso
        //X+	X occurs once or more times
        //também funciona para valores nulos
        return Pattern.matches("[a-zA-Z0-9]+", username);

    }

    /**
     * verifica se o email tem formato válido
     *
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email);
    }
    
    //---------------------------------------------------------------------------------
    //------------------ Expressoes regulares incompletas -----------------------------
    //---------------------------------------------------------------------------------
    public static boolean isAddressValid(String address) {
        return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", address);
    }
    /**
     *
     * @param image
     * @return boolean
     */
    public static boolean isImageValid(String image) {
        return Pattern.matches("[a-zA-Z0-9]+", image);
    }
}
//ajuda sobre regex
//Pattern.matches("[carateresOUconjunto-ADMITIDO]",dadosAcomparar)
//se nos dados a comparar n existir algo que n esteja em carateresOUconjunto-ADMITIDO devolve falso!
//fonte: http://www.javatpoint.com/java-regex
