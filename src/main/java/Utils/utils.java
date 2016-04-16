/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;

/**
 *
 * @author Pedro Batista
 */
public class utils {

    public static boolean isNumber(String data) {
        try {
            int d = Integer.parseInt(data);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

        //ou simplesmente
        //return Pattern.matches("[0-9]+", data));
    }

    public static boolean isNumberLimited(String data, int limit) {
        /**
         * [admite numeros] {tamanho min=1, max=limit} 
         * caso data seja null devolve false
         */
        return Pattern.matches("[0-9]{1,limit}", data);
    }

    public static boolean isString(String data) {
        //admite letras,numeros e espaço
        return Pattern.matches("[a-zA-Z0-9 ]+", data);
    }

    public static boolean isStringLimited(String data, int limit) {
        /**
         * [admite letras,numeros e espaço] {tamanho min=1, max=limit} caso data
         * seja null devolve false
         */

        return Pattern.matches("[a-zA-Z0-9 ]{1,limit}", data);
    }

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

    public static boolean isUsernameValid(String username) {

        //ver se tem espaços ou carateres especiais- se tiver devolve falso
        //X+	X occurs once or more times
        //também funciona para valores nulos
        return Pattern.matches("[a-zA-Z0-9]+", username);

    }

    public static boolean isEmailValid(String email) {
        return Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email);
    }

}
//ajuda sobre regex
//Pattern.matches("[carateresOUconjunto-ADMITIDO]",dadosAcomparar)
//se nos dados a comparar n existir algo que n esteja em carateresOUconjunto-ADMITIDO devolve falso!
//fonte: http://www.javatpoint.com/java-regex
