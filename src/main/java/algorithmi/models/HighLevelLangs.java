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

/**
 *
 * @author David
 */
public class HighLevelLangs {

    private int id;
    private String description;

    public HighLevelLangs(String data) throws Exception {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject HighLevelLangs = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(HighLevelLangs.entrySet());

        //Associa os dados ao objecto Question
        //Se o id for nulo (é uma institution nova)
        if (HighLevelLangs.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = HighLevelLangs.get("id").getAsInt();
        }
        this.description = HighLevelLangs.get("description").getAsString();

    }

    //Maximo ID da tabela tblHighLevelLangs
    public static int getLastID() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblHighLevelLangs");
    }

    public static String getAll() {
        try {
            String query = "SELECT * FROM tblHighLevelLangs";
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Instituições.\"}";
        }
    }

    public int regist() throws Exception {
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        int status = 400;
        if (!existErro) {
            String insert = "INSERT INTO tblHighLevelLangs values(" + id + ",'" + description + "')";
            System.out.println(insert);
            return utils.executeIUDCommand(insert);

        }
        return status;
    }

    public static int delete(int id) throws Exception {

        String deleted = utils.deleteRegist(id, "tblHighLevelLangs");
        return utils.executeIUDCommand(deleted);
    }

    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(description, true);//1
        valid = nameValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome da linguagem inválido";
            }
        }
        return respostasErro;
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

}
