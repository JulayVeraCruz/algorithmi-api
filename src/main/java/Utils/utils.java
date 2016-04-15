/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    }

    public static boolean isString(String data) {

        return true;
    }

    public boolean isThisDateValid(String dateToValidate, String dateFromat) {
        //validar data formato proposto dd/MM/yyyy 
        //dateFormat="dd/MM/yyyy"
        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
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

}
