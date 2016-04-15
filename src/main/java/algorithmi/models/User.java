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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Pedro Dias
 */
public class User {

    private int _id;
    private String name;
    private String password;
    private String imgB64;
    private Date dateBirth;
    private String email;
    private int type;

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
        validateData();
        //Associa os dados ao objecto User
        this._id = user.get("_id").getAsInt(); //ir buscar o max id da bd + 1 
        this.name = user.get("name").getAsString();
        this.password = user.get("password").getAsString();
        this.imgB64=user.get("imgB64").getAsString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date dt;
        try {
            dt = df.parse(user.get("dateBirth").getAsString());
        } catch (ParseException ex) {
            dt = new Date();
        }
        this.dateBirth = dt;
        this.email = user.get("email").getAsString();
        this.type = user.get("type").getAsInt();//Definir o tipo de utilizador, como é registo, deverá ser do tipo aluno
    }

    public void regist() {
        //Insere na BD
    }


    public int getId_User() {
        return _id;
    }

    public void setId_User(int id_User) {
        this._id = id_User;
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
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
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
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        regist();
         /**Senão Devolve um erro (mas dos
         * amigáveis :D)
         */
    }
}
