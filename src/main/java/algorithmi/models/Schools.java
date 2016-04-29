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
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {

        this._id = getLastID_Schools()+ 1; //ir buscar o max id da bd + 1
        this.name = schools.get("name").getAsString();
        this.institution = schools.get("institution").getAsInt();
        this.image = schools.get("image").getAsString();
        
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
        utils getid = new utils();
        return getid.getLastID("tblSchools");
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

    private String[] validateData() {
        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean nameValid = utils.isString(name);//0
        boolean institutionValid = utils.isNumber(Integer.toString(institution));//1
        boolean imageValid = utils.isString(image);//2

        valid = nameValid && institutionValid && imageValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[0] = "Nome invalido";
                }
                if (!institutionValid) {
                    respostasErro[1] = "Institution invalida";
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
            stmtt.execute("INSERT INTO tblSchools values(" + _id + "," + name + "," + institution + "," + image + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result insert schools " + res);

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
    
    public void updateInstitution(int _id){
    
         try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("UPDATE tblInstitutions "+"SET name=" + name + ", institution=" + institution + ",image=" + image + " where _id=" + _id +")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update school " + res);

            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
