/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import algorithmi.Main;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author David
 */
public class Userlog {
    
    
    
        public static String getAll(Response response) {
        try {
            String query = "SELECT * from tblUserlog";
            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter os Registos.\"}";
        }
    }
}
