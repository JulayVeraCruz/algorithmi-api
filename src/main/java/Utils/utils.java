/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

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
    
    public static boolean isString(String data){
        
        return true;
    }
}
