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
    
    private Connection connect = null;
    PreparedStatement preparedStatement = null;

    public Institutions(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(institutions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        this._id = getLastID_Institutions()+ 1; //ir buscar o max id da bd + 1
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
        System.out.println("json \n"+ json);
        return json;
    }

    //Maximo ID da tabela Institutions
    public static int getLastID_Institutions() {
        utils getid = new utils();
        return getid.getLastID("tblInstitutions");
    }
    
    
    private void validateData() {
        //Se estiver tudo OK, inserer na BD
        insert();
    }

    public int insert() {
        int status = 0;
        try {
            // Load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // DB connection setup 
            connect = DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt" + "user=algo&password=algo");
            // PreparedStatements 
            preparedStatement = connect.prepareStatement("insert into user values (?, ?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setString(1, _id + "");
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, image);
            status = preparedStatement.executeUpdate();

            if (connect != null) {
                connect.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }
  
}

