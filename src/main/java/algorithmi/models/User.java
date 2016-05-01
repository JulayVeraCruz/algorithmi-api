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
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.json;

/**
 *
 * @author Pedro Dias
 */
public class User {

    private int _id;
    private String name;
    private String user;
    private Date birthDate;
    private String email;
    private int type;
    private String password;
    private String image;

    public User(String data) throws ParseException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

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
        //Associa os dados ao objecto User
        this._id = getLastID_Users() + 1; //ir buscar o max id da bd + 1
        this.user = user.get("username").getAsString();
        this.name = user.get("name").getAsString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date datBirth;

        datBirth = df.parse(user.get("birthDate").getAsString());

        this.birthDate = datBirth;
        this.email = user.get("email").getAsString();
        this.password = user.get("password").getAsString();
        this.image = user.get("image").getAsString();
        this.type = user.get("type").getAsInt();//Definir o tipo de utilizador, como é registo, deverá ser do tipo aluno

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

    /**
     * Insere novos registos na tabela INSERT INTO tabela Values(?,..)
     *
     * @return status
     */
    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();
            stmtt.execute("INSERT INTO tblUsers values(" + _id + "," + '"' + name + '"' + "," + '"' + user + '"' + "," + birthDate + "," + '"' + email + '"' + "," + '"' + type + '"' + "," + '"' + password + '"' + "," + image + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println(" insert user nº " + _id);
            status = 1;//
            stmtt.close();
        } catch (Exception ex) {
            System.out.println("exception regist user= " + ex);
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    /**
     * obtem o maximo id utilizado na tabela tblUsers
     *
     * @return int
     */
    public static int getLastID_Users() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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
        boolean nameValid = utils.isString(name);//1
        boolean dateValid = utils.isThisDateValid(birthDate.toString());//2
        boolean emailValid = utils.isEmailValid(email);//3
        boolean imageValid = utils.isString(image);//4
        boolean passwordValid = utils.isString(password);//5
        boolean typeValid = utils.isNumber(Integer.toString(type));//6

        valid = userValid && nameValid && dateValid && emailValid && imageValid && passwordValid && typeValid;
        if (!valid) {
            {
                if (!userValid) {
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
        }
        return respostasErro;
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela user
     *
     * @param _id
     */
    public void updateUser(int _id) {

        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();
            stmtt.execute("UPDATE tblUsers " + "SET username=" + '"' + user + '"'
                    + ",name=" + '"' + name + '"' + ", birthDate=" + birthDate + ",email=" + '"' + email + '"' + ",password=" + '"' + password + '"' + ",image=" + '"' + image + '"' + ",type=" + type + " where _id=" + _id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update user " + res.rowUpdated());

            stmtt.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("sql ex updateuser= " + ex);
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * lista de professores com indicação dos cursos em que estao envolvidos e a
     * sua foto
     *
     * @return Json[]
     */
    public static JsonObject listTeacher() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JsonObject obj = new JsonObject();
        JsonArray header = new JsonArray();
        JsonArray list = new JsonArray();

        try (
                //executa driver para ligar à base de dados
                Statement stmtt = utils.connectDatabase()) {

            stmtt.execute("select tblusers.`name`,tblusers.image,tblcourses.`name` from tblusers,tblcourses,tblusercourses where tblusers.`type`=3  and tblusers._id=tblusercourses.userID and tblusercourses.courseID=tblcourses._id ");

            ResultSet res = stmtt.getResultSet();

            int columnCount = res.getMetaData().getColumnCount();
            ResultSetMetaData metadata = (ResultSetMetaData) res.getMetaData();

            //headers column  name,image,name
            for (int i = 1; i <= columnCount; i++) {
                //header.add(Name);
                //header.add(image);
                //header.add(Course);

                header.add(String.valueOf(metadata.getColumnName(i)));
                obj.add("columndata", header);
            }

            while (res.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(String.valueOf(res.getObject(i)));
                    obj.add("rowdata", list);
                }
            }
        } catch (Exception ex) {
            System.out.println("list teachers error :" + ex);
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    /**
     * lista dos users do tipo students devolvendo o nome,imagem e o curso
     *
     * @return Json[]
     * @throws java.sql.SQLException
     */
    public static JsonObject listStudents() throws SQLException, InstantiationException, ClassNotFoundException, IllegalAccessException {

        JsonObject obj = new JsonObject();
        JsonArray header = new JsonArray();
        JsonArray list = new JsonArray();

        try (
                //executa driver para ligar à base de dados
                Statement stmtt = utils.connectDatabase()) {

            stmtt.execute("select tblUsers.name as Name,tblUsers.image as Image,tblCourses.name as Course from tblUsers,tblCourses,tblUserCourses where tblUsers.type=4  and tblUsers._id=tblUserCourses.userID and tblUserCourses.courseID=tblCourses._id ");

            ResultSet res = stmtt.getResultSet();

            int columnCount = res.getMetaData().getColumnCount();
            ResultSetMetaData metadata = (ResultSetMetaData) res.getMetaData();

            //headers column  name,image,name
            for (int i = 1; i <= columnCount; i++) {
                //header.add(Name);
                //header.add(image);
                //header.add(Course);

                header.add(String.valueOf(metadata.getColumnName(i)));
                obj.add("columndata", header);
            }

            while (res.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    list.add(String.valueOf(res.getObject(i)));
                    obj.add("rowdata", list);
                }
            }
        } catch (Exception ex) {
            System.out.println("list students error :" + ex);
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public int getId_User() {
        return _id;
    }

    public void setId_User(int id_User) {
        this._id = id_User;
    }

    public String getUser() {
        return user;
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
