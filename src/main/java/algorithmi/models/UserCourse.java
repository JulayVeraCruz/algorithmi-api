/*
 * Copyright 2016 FilipeRosa.
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

import Utils.Utils;
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
 * @author FilipeRosa
 */
public class UserCourse {

    private int userID;
    private int courseID;

    public UserCourse(String data) {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject UserCourse = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(UserCourse.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        this.userID = UserCourse.get("userID").getAsInt();

        this.courseID = UserCourse.get("courseID").getAsInt();
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
     * para actualizar/alterar os dados de um registo na tabela alteração de
     * curso de um user curso atruibuido por engano ou substituicao
     *
     * @param _id
     */
    public void updateCourseOf_User(int user_id, int oldCourse_id) {

        try {
            //executa driver para ligar à base de dados
            Statement stmtt = Utils.connectDatabase();
            
            stmtt.execute("UPDATE tblUsersCourse " + "SET courseID=" + courseID + " where userID=" + userID + " AND courseID=" + oldCourse_id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update user's course  " + res);

            stmtt.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UserCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UserCourse.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Insere novos registos na tabela
     *
     * @return status
     */
    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = Utils.connectDatabase();
            
            stmtt.execute("INSERT INTO tblCourses values(" + userID + "," + courseID + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result insert user's courses " + res);

            stmtt.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    /**
     * faz a validação dos dados com o retorno de um array podendo conter ou nao
     * mensagens de erro
     *
     * @return String[]
     */
    private String[] validateData() {

        String respostasErro[] = new String[2];
        boolean valid = false;

        boolean userIdNumberValid = Utils.isNumber(userID + "");//0
        boolean courseIdNumberValid = Utils.isNumber(courseID + "");//1

        valid = userIdNumberValid && courseIdNumberValid;
        if (!valid) {
            if (!userIdNumberValid) {
                respostasErro[0] = "User Invalid";
            }
            if (!courseIdNumberValid) {
                respostasErro[1] = "Course Invalid";
            }
        }

        return respostasErro;
    }

    /**
     * apaga o registo da tabela UsersCurse
     *
     * @param userID
     * @param courseID
     * @return
     */
    public String deleteRegist(int userID, int courseID) {
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = Utils.connectDatabase();
            
            stmtt.execute("DELETE FROM `tblUsersCourses` where userID=" + userID + " and courseID=" + courseID);

            ResultSet res = stmtt.getResultSet();

            return "Deleted: " + res.toString();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "regist not deleted";
    }

    public UserCourse(int user, int curse) {
        this.userID = user;
        this.courseID = curse;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
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

}
