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

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author FilipeRosa
 */
public class UserCourse {

    private int userID;
    private int courseID;

    public UserCourse(int userID, String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject userCourse = (JsonObject) jsonParser.parse(data);

        this.userID = userID;
        this.courseID = userCourse.get("course").getAsInt();
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela alteração de
     * curso de um user curso atruibuido por engano ou substituicao
     *
     * @param id
     */
    public void updateCourseOf_User(int userid, int oldCourseid) throws Exception {

        //executa driver para ligar à base de dados
        String update = "UPDATE tblusercourses " + "SET courseID=" + courseID + " where userID=" + userID + " AND courseID=" + oldCourseid + ")";

        String upd = utils.executeSelectCommand(update).toString();

        System.out.println("result update user's course  " + upd);

    }

    /**
     * Insere novos registos na tabela
     *
     * @return status
     */
    public int regist() throws Exception {
        int status = 400;
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
            String insert = "INSERT INTO tblusercourses values(" + userID + "," + courseID + ")";

            return utils.executeIUDCommand(insert);

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

        boolean userIdNumberValid = utils.isNumber(userID + "", false);//0
        boolean courseIdNumberValid = utils.isNumber(courseID + "", false);//1

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
    public static String deleteRegist(int userID, int courseID) throws Exception {
        String delete = "regist not deleted";
        //executa driver para ligar à base de dados
        String delet = "DELETE FROM `tbluserscourses` where userID=" + userID + " and courseID=" + courseID;
        String tt = utils.executeSelectCommand(delet).toString();
        return tt;
    }

    /**
     * lista os cursos a que o user idUser esta ligado
     *
     * @return JsonString
     */
    public static String coursesOfUser(int idUser) throws Exception {

        String query = "SELECT tblcourses.`name` FROM tblusers,tblcourses,tblusercourses where tblusercourses.`userID`=" + Integer.toString(idUser) + " and tblusers.`id`=tblusercourses.`userID` and tblcourses.`id`=tblusercourses.`courseID`";

        String obj = utils.executeSelectCommand(query).toString();

        return obj;

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
