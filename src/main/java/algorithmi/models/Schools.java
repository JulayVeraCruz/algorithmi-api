/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Pedro Batista
 */
public class Schools {
    private int _id;
    private String name;
    private int institution;
    private String image;

    public Schools(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject schools = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(schools.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        
        this._id = getLastID_Schools()+ 1; //ir buscar o max id da bd + 1
        this.name = schools.get("name").getAsString();
        this.institution = schools.get("institution").getAsInt();
        this.image = schools.get("image").getAsString();
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

    public int getInstitution() {
        return institution;
    }

    public void setInstitution(int institution) {
        this.institution = institution;
    }
    
     public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public static int getLastID_Schools() {
        Utils getid = new Utils();
        return getid.getLastID("tblSchools");
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
