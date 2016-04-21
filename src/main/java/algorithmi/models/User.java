/*
 * Copyright 2016 Pedro Dias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Dias
 */
public class User {

    private int _id;
    private String username;
    private String name;
    private Date birthDate;
    private String email;
    private String image;
    private String password;
    private int type;

    private Connection connect = null;
    PreparedStatement preparedStatement = null;

    public User(String data) {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject user = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(user.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {
            //Associa os dados ao objecto User
            this._id = getLastID_Users() + 1; //ir buscar o max id da bd + 1 
            this.username = user.get("username").getAsString();
            this.name = user.get("name").getAsString();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            Date dt;
            try {
                dt = df.parse(user.get("birthDate").getAsString());
            } catch (ParseException ex) {
                dt = new Date();
            }
            this.birthDate = dt;
            this.email = user.get("email").getAsString();
            this.password = user.get("password").getAsString();
            this.image = user.get("image").getAsString();
            this.type = user.get("type").getAsInt();//Definir o tipo de utilizador, como é registo, deverá ser do tipo aluno
        }
    }

    public int getId_User() {
        return _id;
    }

    public void setId_User(int id_User) {
        this._id = id_User;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateBirth() {
        return birthDate;
    }

    public void setDateBirth(Date dateBirth) {
        this.birthDate = dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * obtem o maximo id utilizado na tabela tblUsers
     *
     * @return int
     */
    public static int getLastID_Users() {
        utils getid = new utils();
        return getid.getLastID("tblUsers");
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

    private String[] validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        String respostasErro[] = new String[7];
        boolean valid = false;

        boolean usernameValid = utils.isUsernameValid(username);//0
        boolean nameValid = utils.isString(name);//1
        boolean dateValid = utils.isThisDateValid(birthDate.toString());//2
        boolean emailValid = utils.isEmailValid(email);//3
        boolean imageValid = utils.isString(image);//4
        boolean passwordValid = utils.isString(password);//5
        boolean typeValid = utils.isNumber(Integer.toString(type));//6

        valid = usernameValid && nameValid && dateValid && emailValid && imageValid && passwordValid && typeValid;
        if (valid) {
            //se for tudo validado regista
            regist();
        } else /**
         * Senão Devolve um erro (mas dos amigáveis :D)
         */
        {
            if (!usernameValid) {
                respostasErro[0] = "Username invalido";
            }
            if (!nameValid) {
                respostasErro[1] = "Nome invalido";
            }
            if (!dateValid) {
                respostasErro[2] = "Data invalida";
            }
            if (!emailValid) {
                respostasErro[3] = "Email invalido";
            }
            if (!imageValid) {
                respostasErro[4] = "path invalido";
            }
            if (!passwordValid) {
                respostasErro[5] = "Password invalida";
            }
            if (!typeValid) {
                respostasErro[6] = "Tipo invalido";
            }
        }

        return respostasErro;
    }

    public int regist() {
        int status = 0;
        try {
            // Load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // DB connection setup 
            connect = DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt" + "user=algo&password=algo");
            // PreparedStatements 
            preparedStatement = connect.prepareStatement("insert into user values (?, ?, ?, ?, ?, ?, ?, ? )");
            // Parameters start with 1

            //ordem segundo a tabela da bd v3.2
            preparedStatement.setString(1, _id + "");
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, birthDate.toString());
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, image);
            preparedStatement.setString(7, password);
            preparedStatement.setString(8, type + "");
            status = preparedStatement.executeUpdate();

            if (connect != null) {
                connect.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

}
