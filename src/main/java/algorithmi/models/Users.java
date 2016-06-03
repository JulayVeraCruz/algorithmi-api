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
import spark.Response;

/**
 *
 * @author Pedro Dias
 */
public class Users {

    private int id;
    private String name;
    private String username;
    private String birthDate;
    private String email;
    private int type;
    private String password;
    private String properties;
    private String image;
    private boolean state;

    public boolean getState() {
        return state;
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
        this.id = utils.getLastID("tblUsers") + 1;
        this.image = utils.b64ToImage(this.image, "user" + id);

        //Impede que se registem com admins, so aceita prof e aluno
        if (type <= 2) {
            //No caso de nao ser nenhum, fica como aluno
            type = 4;
        }

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

            String bb = df.format(new Date(birthDate));

            String insert = "INSERT INTO tblUsers values(" + id + "," + '"' + name + '"' + "," + '"' + bb + '"' + "," + '"' + email + '"' + ", " + getType() + "," + '"' + image + '"' + "," + '"' + password + '"' + "," + '"' + username + '"' + ",'',false)";

            return utils.executeIUDCommand(insert);
        }
        return status;
    }

    //Procura o utilizador cujo username ou email correspondam
    public static String exist(String username, Response response) {
        try {
            String query = "select * from tblUsers where (username='" + username + "' or email='" + username + "')";
            JsonArray ja = utils.executeSelectCommand(query);
            //Se os dados de autenticacao estao correctos
            if (ja.size() > 0) {
                System.out.println("1");
                return ja.get(0).getAsJsonObject().toString();
            }

        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return null;
    }

    public JsonArray getMyData(int id) {
        try {
            String query = "select * from tblUsers where id=" + id;
            JsonArray myData = utils.executeSelectCommand(query);
            return myData;
        } catch (Exception ex) {
            // Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("user not found :" + ex);
            //Se nao encontrar o utilizador, devolte 5=sem privilegios
            return null;
        }
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

        boolean userValid = utils.isUsernameValid(username);//0
        boolean nameValid = utils.isString(name, true);//1
//        boolean dateValid = utils.isThisDateValid(myString);//2
        boolean emailValid = utils.isEmailValid(email);//3
//        boolean imageValid = utils.isString(image);//4

        boolean passwordValid = utils.isString(password, true);//5
        boolean typeValid = utils.isNumber(Integer.toString(getType()), false);//6

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
    public int updateUser(String data) throws Exception {
        int status = 400;

        //Transforma a string das alteraçoes para json
        JsonParser jsonParser = new JsonParser();
        JsonObject userr = (JsonObject) jsonParser.parse(data);
        this.username = userr.get("user").getAsString();

        String query = "UPDATE tblUsers " + "SET username='" + username + "' where id=" + id;
        //executa driver para ligar à base de dados

        System.out.println(query);

        return utils.executeIUDCommand(query);
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
            String query = "select tblUsers.*, tblusercourses.courseID as courseId, tblcourses.name as courseName from tblUsers, tblusercourses, tblcourses where type = 4 and userID = tblUsers.id and tblcourses.id = courseID";

            String obj = utils.executeSelectCommand(query).toString();

//        String obj = utils.commandMySQLToJson_String(query);
            System.out.println("obj student " + obj);

            return obj;
        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception" + ex.getMessage());
            return "{\"resposta\":\"Erro ao obter Utilizadores.\"}";
        }
    }

    /**
     * apaga um user com o id
     *
     * @param id
     */
    public static int delete(int id) throws Exception {

        //Apaga a associacoes dos users-cursos
        utils.executeIUDCommand("DELETE FROM tblusercourses where userID=" + id + "");
        //Apaga o utilizador
        return utils.executeIUDCommand("DELETE FROM tblusers where id=" + id + "");

    }

    //Obtem os dados do utilizador prontos para a view
    public static String getUser(String id) {
        try {
            String query = "select tblusers.*,tblcourses.`name` as 'course' from tblusers,tblcourses,tblusercourses where tblusers.`id`=" + id + "  and tblusers.id=tblusercourses.userID and tblusercourses.courseID=tblcourses.id ";
            String user = utils.executeSelectCommand(query).get(0).getAsJsonObject().toString();
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

    public int getID() {
        return id;
    }

    public String getProperties() {
        return properties;
    }

    /**
     * @return the type
     */
    public int getType() {

        return type;

    }

    public String getUsername() {
        return username;
    }

    public String getMyAdminData() {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject myData = (JsonObject) jsonParser.parse(this.toString());
            //Recolhe a lista de professores pendentes
            String querySch = "select * from tblUsers where state=false and type=3";
            JsonArray pendingTeachers = utils.executeSelectCommand(querySch);
            myData.add("pendingUsers", pendingTeachers);

            return myData.toString();
        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public String getMyTeacherData() {
        try {
            /*
             Refazer o select para que apenas os alunos dos cursos a que o prof da aulas sejam listadaos
             No momento recolhe todos
             */
            JsonParser jsonParser = new JsonParser();
            JsonObject myData = (JsonObject) jsonParser.parse(this.toString());
            //Recolhe a lista de alunos pendentes dos cursos a quem dá aulas
            String querySch = "select a.* from tblusers a, tblusercourses b "
                    + "where b.userID = a.id and a.type = 4 and state=0 "
                    + "and b.courseID = any(select b.courseID from tblusers a, tblusercourses b where b.userID =" + id + ")";
            JsonArray pendingUsers = utils.executeSelectCommand(querySch);
            myData.add("pendingUsers", pendingUsers);

            return myData.toString();
        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //Altera o estado de um user (activo/inactivo)
    public String changeState(String state, Response response) {
        try {
            String query = "";
            System.out.println("");
            if (state.equals("true")) {
                query = "UPDATE tblUsers SET state=1 where id=" + id;
            } else {
                query = "UPDATE tblUsers SET state=0 where id=" + id;

            }
            response.status(utils.executeIUDCommand(query));
            return "{\"text\":\"Estado alterado com sucesso.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível alterar o estado do user " + id + ".\"}";
    }

    public String getPassword() {
        return password;
    }

}
