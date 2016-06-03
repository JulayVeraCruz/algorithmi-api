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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Dias
 */
public class Students {

    private int id;
    private String name;
    private String user;
    private Date birthDate;
    private String email;
    private int type;
    private String password;
    private String properties;
    private String image;

    public Students(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject user = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(user.entrySet());

        //Associa os dados ao objecto User
        //Se o id for nulo (é um user novo)
        if (user.get("id") == null) {
            this.id = getLastID_Users() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = user.get("id").getAsInt();
        }
        this.name = user.get("name").getAsString();
        this.user = user.get("username").getAsString();
        this.password = user.get("password").getAsString();
        //Converte a imagem recebida em b64 e grava-a 
        this.image = utils.b64ToImage(user.get("image").getAsString(), "user" + id);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);

        Date datBirth;

        //String gg = user.get("dateBirth").getAsString();
        String gg = "12/03/2012";

        String dd = gg.substring(0, 2);
        String mm = gg.substring(3, 5);
        String yyyy = gg.substring(6, 10);

        datBirth = df.parse(yyyy + "-" + mm + "-" + dd);

        this.birthDate = datBirth;
        this.email = user.get("email").getAsString();
        this.type = user.get("type").getAsInt();
        this.type = user.get("type").getAsInt();
    }

    /**
     * Insere novos registos na tabela INSERT INTO tabela Values(?,..) o campo
     * properties é marcado como 'NOK' até que o professor/administrador o
     * aceite
     *
     *
     * @return status
     */
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
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            String bb = df.format(birthDate);

            String insert = "INSERT INTO tblUsers values(" + id + "," + '"' + name + '"' + "," + '"' + bb + '"' + "," + '"' + email + '"' + ", " + type + "," + '"' + image + '"' + "," + '"' + password + '"' + "," + '"' + user + '"' + ",'NOK')";

            return utils.executeIUDCommand(insert);
        }
        return status;
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Listar Professores ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAllTeachers() {

        try {
            String query = "select id, name, birthDate, email,image, properties,username  From tblusers where type=3 ";
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Professores.\"}";
        }
    }

    /**
     * obtem o maximo id utilizado na tabela tblUsers
     *
     * @return int
     */
    public static int getLastID_Users() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblusers");
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    /**
     * faz a validação dos dados com o retorno de um array podendo conter ou nao
     * mensagens de erro
     *
     * @return String[]
     */
    private String[] validateData() {

        String respostasErro[] = new String[7];
        boolean valid = false;

        boolean userValid = utils.isUsernameValid(user);//0
        boolean nameValid = utils.isString(name, true);//1
//        boolean dateValid = utils.isThisDateValid(myString);//2
        boolean emailValid = utils.isEmailValid(email);//3
//        boolean imageValid = utils.isString(image);//4

        boolean passwordValid = utils.isString(password, true);//5
        boolean typeValid = utils.isNumber(Integer.toString(type), false);//6

        valid = nameValid && emailValid && passwordValid && typeValid && userValid;//&& dateValid && imageValid &&;
        if (!valid) {
            {
                if (!userValid) {
                    respostasErro[0] = "Username invalido";
                }
                if (!nameValid) {
                    respostasErro[1] = "Nome invalido";
                }
//                if (!dateValid) {
//                    respostasErro[2] = "Data invalida";
//                }
                if (!emailValid) {
                    respostasErro[3] = "Email invalido";
                }
//                if (!imageValid) {
//                    respostasErro[4] = "path invalido";
//                }
                if (!passwordValid) {
                    respostasErro[5] = "Password invalida";
                }
                if (!typeValid) {
                    respostasErro[6] = "Tipo invalido";
                }
            }
        }
        return respostasErro;
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela user
     *
     * @param id
     */
    public String updateUser(String data) throws Exception {
        int status = 400;

        //Transforma a string das alteraçoes para json
        JsonParser jsonParser = new JsonParser();
        JsonObject userr = (JsonObject) jsonParser.parse(data);
        this.user = userr.get("user").getAsString();

        String query = "UPDATE tblUsers " + "SET username='" + user + "' where id=" + id;
        //executa driver para ligar à base de dados

        System.out.println(query);

        return utils.executeSelectCommand(query).toString().toString();
    }

    /**
     * lista de professores com indicação dos cursos em que estao envolvidos e a
     * sua foto
     *
     * @return Json[]
     */
    public static String listTeacher() throws Exception {

        String query = "select tblusers.`Name`,tblusers.image,tblcourses.`name` from tblusers,tblcourses,tblusercourses where tblusers.`type`=3  and tblusers.id=tblusercourses.userID and tblusercourses.courseID=tblcourses.id ";
        String obj = utils.executeSelectCommand(query).toString();;
        return obj;
    }

    /**
     * lista dos users do tipo students devolvendo o nome,imagem e o curso
     *
     * @return Json[]
     * @throws java.sql.SQLException
     */
    public static String listStudents() {

        try {
            String query = "select * from tblUsers where type = 4";

            String obj = utils.executeSelectCommand(query).toString();

//        String obj = utils.commandMySQLToJson_String(query);
            System.out.println("obj student " + obj);

            return obj;
        } catch (Exception ex) {
            Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"resposta\":\"Erro ao obter Utilizadores.\"}";
        }
    }

    /**
     * apaga um user com o id
     *
     * @param id
     */
    public static int deleteUser(int id) throws Exception {
        int status = 400;
//        utils utils = new utils();

        String obj = utils.executeSelectCommand("DELETE FROM tblusers where id=" + id + "").toString();
        boolean deleted = true;
        if (deleted) {
            status = 200;
        }
        return status;
    }

    //Obtem os dados do utilizador prontos para a view
    public static String getUser(String id) {
        try {
            String query = "select tblusers.*,tblcourses.`name` as 'course' from tblusers,tblcourses,tblusercourses where tblusers.`id`=" + id + "  and tblusers.id=tblusercourses.userID and tblusercourses.courseID=tblcourses.id ";
            String user = utils.executeSelectCommand(query).toString();
            return user;
        } catch (Exception e) {
        }
        return "";
    }

    //obtem os dados do utilizador prontos para o update
    public static String getUserData(String id) {
        try {
            String query = "select * from tblusers where id=" + id;
            JsonArray user = utils.executeSelectCommand(query);
            return user.get(0).getAsJsonObject().toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public void setUser(String user) {
        this.user = user;
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

}
