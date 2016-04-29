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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public Institutions(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(institutions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        
        this._id = getLastID_Institutions()+ 1; //ir buscar o max id da bd + 1
        this.name = institutions.get("name").getAsString();
        this.address = institutions.get("address").getAsString();
        this.image = institutions.get("image").getAsString();
       
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {
   
        regist();
            
        }
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
    
    
    private String[] validateData() {
        
        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean nameValid = utils.isString(name);//0
        boolean addressValid = utils.isThisDateValid(address);//1
        boolean imageValid = utils.isString(image);//2

        valid = nameValid && addressValid && imageValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[0] = "Nome invalido";
                }
                if (!addressValid) {
                    respostasErro[1] = "Address invalida";
                }
                if (!imageValid) {
                    respostasErro[2] = "path invalido";
                }
            }
        }
        return respostasErro;
    }

    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            System.out.println("antes insert ");
            
            stmtt.execute("INSERT INTO tblInstitutions values(" + _id + "," + '"' + name + '"' + "," + address + "," + '"' + image + '"' + ")");

            ResultSet res = stmtt.getResultSet();
            status = 1;//sem erros
            System.out.println("insert new institution id" + res.getString(1));

            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("SQL ERROR regist " + ex);
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
    
    public int updateInstitution(int _id){
            int status = 0;
         try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("UPDATE tblInstitutions "+"SET name=" + name + ", address=" + address + ",image=" + image + " where _id=" + _id +")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update institution " + res);
            status = 1;
            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
  
}

