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
public class InputOutput {

    private int id;
    private int question;
    private String input;
    private String output;

    public InputOutput(String data) throws Exception {
        System.out.println("IO " + data);
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject questionIO = (JsonObject) jsonParser.parse(data);

        validateData();
        //Associa os dados ao objecto Question
        //Se o id for nulo (é uma institution nova)
        if (questionIO.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = questionIO.get("id").getAsInt();
        }
        this.question = questionIO.get("questionID").getAsInt();
        this.input = questionIO.get("I").getAsString();
        this.output = questionIO.get("O").getAsString();

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
        return getid.getLastID("tblinputoutputs");
    }

    public int insert() {
        String insert = "INSERT INTO tblinputoutputs values(" + id + "," + '"' + question + '"' + "," + '"' + input + '"' + "," + '"' + output + '"' + ")";

        return utils.executeIUDCommand(insert);
    }

}
