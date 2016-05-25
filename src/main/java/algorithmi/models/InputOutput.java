/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class InputOutput {

    private int id;
    private int question;
    private String input;
    private String output;

    private Connection connect = null;
    PreparedStatement preparedStatement = null;

    public InputOutput(String data) throws Exception {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject QuestionIO = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(QuestionIO.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        validateData();
        //Associa os dados ao objecto Question
        this.id = getLastID() + 1; //ir buscar o max id da bd + 1 
        this.question = QuestionIO.get("title").getAsInt();
        this.input = QuestionIO.get("in").getAsString();
        this.output = QuestionIO.get("out").getAsString();

    }

    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    private void validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
    }

    public static int getLastID() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblQuestions");
    }

    public int regist() {
        int status = 0;
        try {
//as credenciais de ligaçao estao agora em utils
            Statement stmtt = utils.connectDatabase();

//// Load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
//            // DB connection setup 
//            connect = DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt" + "user=algo&password=algo");
//            // PreparedStatements 
            preparedStatement = connect.prepareStatement("insert into user from tblQuestions values (?, ?, ?, ?, ?, ?, ?)");
            // Parameters start with 1

            //ordem segundo a tabela da bd v3.3
            preparedStatement.setString(1, id + "");
            preparedStatement.setString(2, question + "");
            preparedStatement.setString(3, input);
            preparedStatement.setString(4, output);
            status = preparedStatement.executeUpdate();

            if (connect != null) {
                connect.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Users.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

}
