/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Cris
 */
public class SQL {

    public final Connection con;
    public final Statement st;

    public SQL(Connection con, Statement st) {
        this.con = con;
        this.st = st;
    }

    public void close() throws SQLException {
        this.st.close();
        this.con.close();
    }
}
