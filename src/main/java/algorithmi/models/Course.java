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
public class Course {

    private int _id;
    private String name;
    private int school;
    private String image;

    public Course(String data) {

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
     * apaga um curso com o _id
     *
     * @param _id
     */
    public void deleteCourse(int _id) {
        utils utils = new utils();
        utils.deleteRegist(_id, "tblcourses");
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param _id
     */
    public int updateCourse(int _id) {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("UPDATE tblCourse " + "SET name=" + name + ",school=" + school + ",image=" + image + " where _id=" + _id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update course " + res);
            status = 1;
            stmtt.close();
            connn.close();
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
     * Insere novos registos na tabela
     *
     * @return status
     */
    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            System.out.println("antes ibsert ");

            stmtt.execute("INSERT INTO tblcourses values(" + _id + "," + '"' + name + '"' + "," + school + "," + '"' + image + '"' + ")");

            ResultSet res = stmtt.getResultSet();
            status = 1;//sem erros
            System.out.println(" insert new cursos id" + res.getString(1));

            stmtt.close();
            connn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("SQL ERROR regist " + ex);
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    /**
     * lista os cursos existentes e as escolas a que pertencem
     *
     * @return []json
     */
    public static Gson[] listCourses_WEB() {

        Gson lisOfCourses[] = new Gson[getLastID_Courses()];
        try {
            //executa driver para ligar à base de dados
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //faz ligação à base de dados
            Connection connn = (Connection) DriverManager.getConnection("jdbc:mysql://algoritmi.ipt.pt/algo", "algo", "alg0alg0alg0");

            Statement stmtt = (Statement) connn.createStatement();
            stmtt.execute("SELECT tblcourses.name as Course,tblschools.name as School\n"
                    + "from tblCourses,tblSchools\n"
                    + "where tblCourses.school=tblSchools._id");
//            stmtt.execute("SELECT tblCourses.name as \"Course\",tblSchools.name as \"School\"\n"
//                    + "from tblCourses,tblSchools\n"
//                    + "where tblCourses.school=tblSchools._id ");
            ResultSet res = stmtt.getResultSet();

            while (res.next()) {
                lisOfCourses[res.getRow() - 1].toJson(res);
            }
            stmtt.close();
            connn.close();
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lisOfCourses;
    }

    /**
     * obtem o maximo id utilizado na tabela tblCourses
     *
     * @return int
     */
    public static int getLastID_Courses() {
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

        boolean nameValid = utils.isString(name);//0
        boolean schoolNumberValid = utils.isNumber(school + "");//1
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
