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
import static Utils.utils.connectDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author FilipeRosa
 */
public class Course {

    private int _id;
    private String name;
    private int school;
    private String image;

    public Course(String data) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject Course = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(Course.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */

        //Associa os dados ao objecto Course
        this._id = getLastID_Courses() + 1; //ir buscar o max id da bd + 1
        this.name = Course.get("name").getAsString();
        this.image = Course.get("image").getAsString();
        //tenho de ir buscar o id da escola
        this.school = Course.get("school").getAsInt();

    }

    /**
     * apaga um curso com o _id
     *
     * @param _id
     */
    public void deleteCourse(int _id) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        utils utils = new utils();
        utils.deleteRegist(_id, "tblcourses");
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param _id
     * @return
     */
    public int updateCourse(int _id) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        int status = 0;

        Statement stmtt = utils.connectDatabase();
        stmtt.execute("UPDATE tblCourse SET name=" + name + ",school=" + school + ",image=" + image + " where _id=" + _id + ")");

        ResultSet res = stmtt.getResultSet();

        System.out.println("result update Course " + res.rowUpdated());

        stmtt.close();
        return status;
    }

    /**
     * Insere novos registos na tabela
     *
     * @return status
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.sql.SQLException
     */
    public int regist() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        int status = 400;
        if (!existErro) {

            Statement stmtt = connectDatabase();

            stmtt.execute("INSERT INTO tblcourses values(" + _id + "," + '"' + name + '"' + "," + school + "," + '"' + image + '"' + ")");
            ResultSet res = stmtt.getResultSet();
            while (res.next()) {
                status = 200;
            }
            System.out.println(" insert course nº " + _id);

            stmtt.getConnection().close();
            stmtt.close();
        }
        return status;
    }

    /**
     * lista os cursos existentes e as escolas a que pertencem
     *
     * @return []json
     */
    public static JsonObject listCourses_WEB() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String query = "SELECT tblcourses.`name` as Course,tblschools.`name` as School from tblCourses,tblSchools where tblCourses.school=tblSchools._id";
        JsonObject obj = new JsonObject();
        obj = utils.querysToJson(query);

        return obj;

    }

    /**
     * obtem o maximo id utilizado na tabela tblCourses
     *
     * @return int
     */
    public static int getLastID_Courses() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        utils getid = new utils();
        return getid.getLastID("tblcourses");
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

        String respostasErro[] = new String[3];
        boolean valid = false;

        boolean nameValid = utils.isString(name, true);//0
        boolean schoolNumberValid = utils.isNumber(school + "", false);//1
//        boolean imageValid = utils.isString(image);//2

        valid = nameValid && schoolNumberValid; //&& imageValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome invalido";
            }
            if (!schoolNumberValid) {
                respostasErro[1] = "Escola invalida";
            }
//            if (!imageValid) {
//                respostasErro[2] = "path invalido";
//            }

        }

        return respostasErro;
    }

    public Course(int CodCurse, String name, int scholl) {
        this._id = _id;
        this.name = name;
        this.school = scholl;
        this.image = image;
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

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
