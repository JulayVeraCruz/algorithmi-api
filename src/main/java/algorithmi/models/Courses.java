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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author FilipeRosa
 */
public class Courses {

    private int id;
    private String name;
    private int school;
    private String image;

    public Courses(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject course = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(course.entrySet());
        //Associa os dados ao objecto Course
        //Se o id for nulo (é uma institution nova)
        if (course.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = course.get("id").getAsInt();
        }
        this.name = course.get("name").getAsString();
        this.image = utils.b64ToImage(course.get("image").getAsString(), "inst" + id);
        //tenho de ir buscar o id da escola
        this.school = course.get("school").getAsInt();

    }

    //obtem os dados do curso prontos para o update
    public static String getCourseData(String id) {

        try {
            String query = "select * from tblCourses where id=" + id;
            JsonArray user = utils.executeSelectCommand(query);
            return user.get(0).getAsJsonObject().toString();
        } catch (Exception ex) {
            return "";
        }

    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Apagar Instituição -----------------------------------
    //--------------------------------------------------------------------------------------
    public static int deleteCourse(int id) throws Exception {

        String deleted = utils.deleteRegist(id, "tblcourses");
        return utils.executeIUDCommand(deleted);
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela cursos
     *
     * @param id
     * @return
     */
    public int updateCourse() throws Exception {
        String update = "UPDATE tblCourses SET name=" + '"' + name + '"' + ",school=" + school + ",image=" + '"' + image + '"' + " where id=" + id;
        return utils.executeIUDCommand(update);
    }

    /**
     * Insere novos registos na tabela
     *
     * @return status
     * @throws Exception
     */
    public int regist() throws Exception {

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
            String insert = "INSERT INTO tblcourses values(" + id + "," + '"' + name + '"' + "," + school + "," + '"' + image + '"' + ")";
            return utils.executeIUDCommand(insert);
        }
        return status;
    }

    /**
     * lista os cursos existentes e as escolas a que pertencem
     *
     * @return []json
     * @throws java.lang.Exception
     */
    public static String getAll() {
        try {
            String query = "SELECT tblCourses.id,  tblCourses.image, tblCourses.name, tblCourses.school as schoolID, tblSchools.name as schoolName FROM tblSchools, tblCourses WHERE tblSchools.id=tblCourses.school";
            String result = utils.executeSelectCommand(query).toString();
            return result;
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Escolas.\"}";
        }
    }

    /**
     * obtem o maximo id utilizado na tabela tblCourses
     *
     * @return int
     */
    public static int getLastID() throws Exception {
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

}
