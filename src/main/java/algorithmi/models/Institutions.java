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
 * @author Pedro Batista
 */
public class Institutions {
    private int _id;
    private String name;
    private String address;
    private String image;

    public Institutions(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(institutions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        //TEM DE IR BUSCAR O ULTIMO ID E ACRESCENTAR UM
        this._id = 123;
        this.name = institutions.get("name").getAsString();
        this.address = institutions.get("address").getAsString();
        this.image = institutions.get("image").getAsString();
    }

    
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public void regist() {
        //Insere na BD
    }
    
    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

    private void validateData() {
        //Se estiver tudo OK, inserer na BD
        regist();
    }

    
  
}

