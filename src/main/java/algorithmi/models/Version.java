/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.SQL;
import static Utils.utils.connectDatabase;
import java.sql.ResultSet;

/**
 *
 * @author David
 */
public class Version {

    int ver;

    public String getVersion() throws Exception {

        String name = null;
        SQL sql = connectDatabase();

        sql.st.execute("SELECT ver FROM tblVersion");
//        ResultSet res = connectDatabase(mySQL);//se for apenas um insert o res=null
        ResultSet res = sql.st.getResultSet();

        while (res.next()) {
            name = res.getString("ver");
        }

        sql.close();
        return name;
    }

}
