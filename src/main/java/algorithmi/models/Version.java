/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import static Utils.utils.connectDatabase;
import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author David
 */
public class Version {
    int ver;
    
        public String getVersion() throws Exception {

        String name = null;
        Statement stmtt = connectDatabase();

        stmtt.execute("SELECT ver FROM tblVersion");
//        ResultSet res = connectDatabase(mySQL);//se for apenas um insert o res=null
        ResultSet res = stmtt.getResultSet();

        while (res.next()) {
            name = res.getString("ver");
        }
        return name;
    }
    
}
