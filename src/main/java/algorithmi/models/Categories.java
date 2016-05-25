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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Categories {

    private int id;
    private String description;

    public Categories(String data) {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject Categories = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(Categories.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        validateData();
        //Associa os dados ao objecto Question
        this.id = Categories.get("id").getAsInt(); //ir buscar o max id da bd + 1 
        this.description = Categories.get("description").getAsString();

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

    /**
     * apaga um curso com o id
     *
     * @param id
     */
    public int deleteCourse(int id) throws Exception {
        int status = 400;
        utils utils = new utils();
        /*        boolean deleted = utils.deleteRegist(id, "tblCategories");
         if (deleted) {
         status = 200;
         }*/
        return status;
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param id
     * @return
     */
    public int updateCourse(int id) throws Exception {
        int status = 0;
        String update = "UPDATE tblCategories SET "
                + "name=" + '"' + id + '"' + ","
                + "description=" + description + ","
                + "where id=" + id;
        Statement stmtt = utils.connectDatabase();

        stmtt.execute(update);

        //ResultSet res = stmtt.getResultSet();
//        System.out.println("result update Course " + res.rowUpdated());
        stmtt.close();
        return status;
    }

    public int regist() {
        int status = 0;
        try {
            //as credenciais de ligaçao estao agora em utils
            Statement stmtt = utils.connectDatabase();

            stmtt.execute("INSERT INTO tblCategories values(" + id + "," + description + ")");
            stmtt.getConnection().close();
            stmtt.close();

        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Users.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

}
