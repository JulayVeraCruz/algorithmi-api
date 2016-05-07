/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import static algorithmi.models.User.getLastID_Users;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Batista
 */
public class Institutions {

    private int _id;
    private String name;
    private String address;
    private String image;

    public Institutions(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(institutions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
       
        this._id = getLastID_Institutions() + 1; //ir buscar o max id da bd + 1
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

    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    //Maximo ID da tabela Institutions
    public static int getLastID_Institutions() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblInstitutions");
    }

    public int regist() throws Exception {
        int status = 0;
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {

            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();
            String query = "INSERT INTO tblInstitutions values(" + _id + "," + '"' + name + '"' + "," + '"' + address + '"' + "," + '"' + image + '"' + ")";
            stmtt.execute(query);

            ResultSet res = stmtt.getResultSet();
            while (res.next()) {
                status = 200;
            }
            stmtt.close();
        }
        return status;
    }

    private String[] validateData() {

        String respostasErro[] = new String[7];
        boolean valid = false;
        boolean nameValid = utils.isString(name, true);//1
        boolean addressValid = utils.isAddressValid(address);//2
        boolean imageValid = utils.isImageValid(image);//3

        valid = nameValid && addressValid && imageValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[1] = "Nome invalido";
                }
                if (!addressValid) {
                    respostasErro[2] = "Address invalido";
                }
                if (!imageValid) {
                    respostasErro[3] = "Image invalido";
                }
            }
        }
        return respostasErro;
    }
}
