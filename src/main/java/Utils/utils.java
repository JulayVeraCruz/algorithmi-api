/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import algorithmi.models.Course;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

/**
 *
 * @author Pedro Batista
 */
public class utils {

    private Connection connect = null;
    PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    /**
     * verifica se a string é composta apenas por algarismos
     * @param data
     * @return bolean
     */
    public static boolean isNumber(String data) {
            return Pattern.matches("[0-9]+", data);
    }

    /**
     * verifica se a string é composta apenas por algarismos e 
     * se possui o tamanho (limit) correcto 
     * @param data
     * @param limit
     * @return boolean
     */
    public static boolean isNumberLimited(String data, int limit) {
        /**
         * [admite numeros] {tamanho min=1, max=limit} caso data seja null
         * devolve false
         */
        return Pattern.matches("[0-9]{1,limit}", data);
    }

    /**
     * verifica se a string tem apenas 
     * letras,algarismos e espaço
     * @param data
     * @return bollean
     */
    public static boolean isString(String data) {
        //admite letras,numeros e espaço
        return Pattern.matches("[a-zA-Z0-9 ]+", data);
    }

    /**
     *verifica se a string tem apenas 
     * letras,algarismos e espaço e 
     * se possui o tamanho (limit) correcto 
     * @param data
     * @param limit
     * @return boolean
     */
    public static boolean isStringLimited(String data, int limit) {
        /**
         * [admite letras,numeros e espaço] {tamanho min=1, max=limit} caso data
         * seja null devolve false
         */

        return Pattern.matches("[a-zA-Z0-9 ]{1,limit}", data);
    }

    /**
     * verifica se a data tem formato valido
     * o formato admitido é dd/MM/yyyy
     * @param dateToValidate
     * @return boolean
     */
    public static boolean isThisDateValid(String dateToValidate) {
        //validar data formato proposto dd/MM/yyyy 
        //dateFormat="dd/MM/yyyy"
        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
     * verifica se o username tem formato válido
     * apenas admite letras e algarismos
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
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email);
    }

    /**
     * Devolve o último id da tabela nomeTabela
     * na base de dados algoritmi.ipt.pt
     * retorna zero se n existitem dados 
     * @param nomeTabela
     * @return int
     */
    public int getLastID(String nomeTabela) {
        try {
            // This will load the MySQL driver, each DB has its own driver
            // Load the MySQL driver, each DB has its own driver
            // Class.forName("com.mysql.jdbc.Driver");
            // DB connection setup 
            connect = DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt" + "user=algo&password=algo");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            preparedStatement = connect
                    .prepareStatement("select " + nomeTabela + "._id from" + nomeTabela + " order by _id desc limit 1");
            resultSet = preparedStatement.executeQuery();
            return (int) resultSet.getObject(1);

        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
//ajuda sobre regex
//Pattern.matches("[carateresOUconjunto-ADMITIDO]",dadosAcomparar)
//se nos dados a comparar n existir algo que n esteja em carateresOUconjunto-ADMITIDO devolve falso!
//fonte: http://www.javatpoint.com/java-regex
