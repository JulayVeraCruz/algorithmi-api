/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi;
;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Dias
 */
public class testsql {
  
  //https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html

  public static void main(String[] args) {
    try {
        //executa driver para ligar à base de dados
      Class.forName("com.mysql.jdbc.Driver").newInstance();
        //faz ligação à base de dados
      Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");
      
      Statement stmtt = (Statement) connn.createStatement();
      stmtt.execute("SELECT * FROM `tblusers`");

      ResultSet res = stmtt.getResultSet();

      while (res.next()) {
        String name = res.getString("name");
        System.out.println(name);
      }

      stmtt.close();
      connn.close();
    } catch (Exception ex) {
      Logger.getLogger(testsql.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
