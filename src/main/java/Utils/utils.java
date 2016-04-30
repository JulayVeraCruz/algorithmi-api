/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import algorithmi.models.Course;
import java.sql.Connection;
import java.sql.DriverManager;
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

    /**
     * credencias e ligação base de dados
     *
     * @return Statement
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public static Statement connectDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        //faz ligação à base de dados
        Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");
        Statement stmtt = (Statement) connn.createStatement();
        return stmtt;
    }

    /**
     * verifica se a string é composta apenas por algarismos
     *
     * @param data
     * @return bolean
     */
    public static boolean isNumber(String data) {
        return Pattern.matches("[0-9]+", data);
    }
  /**
     * verifica se a string é um float
     *
     * @param data
     * @return bolean
     */
    public static boolean isValidFloat(String data) {
        return Pattern.matches("[0-9.,]+", data);
    }

    public static boolean isFloat(String data) {
        return Pattern.matches("[0-9.,]+", data);
    }

    /**
     * verifica se a string é composta apenas por algarismos e se possui o
     * tamanho (limit) correcto
     *
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
     * verifica se a string tem apenas letras,algarismos e espaço
     *
     * @param data
     * @return bollean
     */
    public static boolean isString(String data) {
        //admite letras,numeros e espaço
        return Pattern.matches("[a-zA-Z0-9 ]+", data);
    }

    /**
     * verifica se a string tem apenas letras,algarismos e espaço e se possui o
     * tamanho (limit) correcto
     *
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

    /**
     * Devolve o último id da tabela nomeTabela na base de dados
     * algoritmi.ipt.pt retorna zero se n existitem dados
     *
     * @param nomeTabela
     * @return int
     */
    public int getLastID(String nomeTabela) {
        int id = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");
            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("select " + nomeTabela + "._id from " + nomeTabela + " order by _id desc limit 1");

            ResultSet res = stmtt.getResultSet();

            while (res.next()) {
                id = Integer.parseInt(res.getString("_id"));
//                System.out.println("id   " + id);
            }
            stmtt.close();
            connn.close();
            return id;

        } catch (SQLException ex) {
            Logger.getLogger(nomeTabela).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * retorna o campo name respectivo do _id de uma tabela
     *
     * @param _id
     * @param tabela
     * @return
     */
    public String getNames(int _id, String tabela) {
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("SELECT name FROM `" + tabela + "` where _id=" + _id);

            ResultSet res = stmtt.getResultSet();

            return res.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Invalid ID or table";
    }

    /**
     * apaga o registo com o _id de uma tabela desde que só possua UM campo
     * (_id) como chave primaria
     *
     * @param _id
     * @param tabela
     * @return
     */
    public String deleteRegist(int _id, String tabela) {
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("DELETE FROM " + tabela + " where _id=" + _id);

            ResultSet res = stmtt.getResultSet();

            return "Deleted: " + res.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "regist not deleted";
    }
}

//ajuda sobre regex
//Pattern.matches("[carateresOUconjunto-ADMITIDO]",dadosAcomparar)
//se nos dados a comparar n existir algo que n esteja em carateresOUconjunto-ADMITIDO devolve falso!
//fonte: http://www.javatpoint.com/java-regex
