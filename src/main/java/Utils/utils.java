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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 *
 * @author Pedro Batista
 */
public class utils {

    /**
     * qualquer interacção de insert,update,delete ou select na base de dados
     * utilizando o @param comandoMySQL retornando o sucesso da operação ou o
     * resultado da query excepto no caso particular da consulta do ultimo id de
     * uma tabela
     *
     * @return string json
     * @throws Exception
     */
    public static JsonArray executeSelectCommand(String comandoMySQL) throws Exception {
        JsonArray obj = new JsonArray();

        Statement stmtt = connectDatabase();

        Gson gson = new Gson();

        System.out.println("State :" + stmtt.execute(comandoMySQL));
        ResultSet res = stmtt.getResultSet();
        ResultSetMetaData metadata = (ResultSetMetaData) res.getMetaData();
        int total_rows = metadata.getColumnCount();
        if (!(res == null)) {
            while (res.next()) {
                JsonObject row = new JsonObject();
                obj.add(row);
                for (int i = 1; i <= total_rows; i++) {
//                System.out.println("col name " + metadata.getColumnLabel(i));
                    row.add(metadata.getColumnLabel(i), gson.toJsonTree(res.getObject(i)));
                }
            }
        } else {
            JsonObject row = new JsonObject();
            obj.add(row);
            row.add(null, null);
        }

        stmtt.close();
        return obj;
    }

    public static int executeIUDCommand(String comandoMySQL) throws Exception {

        Statement stmtt = connectDatabase();

        //1 == ok, 0== NOK
        int status = stmtt.executeUpdate(comandoMySQL);
        switch (status) {
            case 0:
                System.out.println(comandoMySQL + " fail");
                status = 400; //NOK
                break;
            case 1:
                System.out.println(comandoMySQL + " success");
                status = 200; //OK
                break;
        }
        stmtt.close();
        return status;
    }

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
     * Devolve o último id da tabela nomeTabela na base de dados "algo" de
     * algoritmi.ipt.pt retorna zero se n existitem dados
     *
     * @param nomeTabela
     * @return int
     */
    public static int getLastID(String nomeTabela) throws Exception {
        int id = 0;

        Statement stmtt = connectDatabase();

        stmtt.execute("select " + nomeTabela + ".id from " + nomeTabela + " order by id desc limit 1");
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            id = Integer.parseInt(res.getString("id"));
        }
        return id;
    }

    /**
     * retorna o campo name respectivo do id de uma tabela
     *
     * @param id
     * @param tabela
     * @return
     */
    public String getName(int id, String tabela) throws Exception {

        String name = null;
        Statement stmtt = connectDatabase();

        stmtt.execute("SELECT name FROM `" + tabela + "` where id=" + id + "");
//        ResultSet res = connectDatabase(mySQL);//se for apenas um insert o res=null
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            name = res.getString("name");
        }
        return name;
    }

    /**
     * apaga o registo com o id de uma tabela desde que só possua UM campo (id)
     * como chave primaria
     *
     * @param id
     * @param tabela
     * @return
     */
    public static String deleteRegist(int id, String tabela) throws Exception {
        String delete = "DELETE FROM " + tabela + " where id=" + id + "";
        return delete;
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

    //Expressão Regular Horas - Atributo: StartingTime e FinishingTime
    public static boolean isThisHourValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
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

    //Expressão Regular Username - Atributo: Username
    public static boolean isUsernameValid(String username) {
        //ver se tem espaços ou carateres especiais- se tiver devolve falso
        //X+	X occurs once or more times
        //também funciona para valores nulos
        return Pattern.matches("[a-zA-Z0-9]+", username);

    }

    //Expressão Regular Email - Atributo: Email
    public static boolean isEmailValid(String email) {
        return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email);
    }

    //Expressão Regular Address - Atributo: Address
    public static boolean isAddressValid(String address) {
        return Pattern.matches("[a-zA-Z0-9/-º.,]+", address);
    }
    //---------------------------------------------------------------------------------
    //------------------ Expressoes regulares incompletas -----------------------------
    //---------------------------------------------------------------------------------

    //Expressão Regular Imagem - Atributo: Image
    public static boolean isImageValid(String image) {
        return Pattern.matches("[a-zA-Z0-9]+", image);
    }

    public static String b64ToImage(String imageDataString, String table) {
        try {
            String imageName = table + getRandomHexString(10);
            String base64Image = imageDataString.split(",")[1];

            // Convert the image code to bytes.
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            File imageFile = new File("../algorithmi-web/images/" + imageName + ".jpeg");

            ImageIO.write(bufferedImage, "jpeg", imageFile);
            return imageName + ".jpeg";
        } catch (Exception ex) {
            System.out.println("Image error" + ex.getMessage());
            Logger
                    .getLogger(utils.class
                            .getName()).log(Level.SEVERE, null, ex);

            return "noInstitution.jpeg";
        }
    }

    private static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
}

//ajuda sobre regex
//Pattern.matches("[carateresOUconjunto-ADMITIDO]",dadosAcomparar)
//se nos dados a comparar n existir algo que n esteja em carateresOUconjunto-ADMITIDO devolve falso!
//fonte: http://www.javatpoint.com/java-regex

