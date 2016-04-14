/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author David
 */
public class QuestionIO {

    private int _id;
    private int question;
    private String in;
    private String out;

    public QuestionIO(String data) {
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
        this._id = 123; //ir buscar o max id da bd + 1 
        this.question = QuestionIO.get("titulo").getAsInt();
        this.in = QuestionIO.get("in").getAsString();
        this.out = QuestionIO.get("out").getAsString();

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

}
