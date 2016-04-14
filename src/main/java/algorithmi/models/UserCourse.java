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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author FilipeRosa
 */
public class UserCourse {

    private int user;
    private int course;

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
        validateData();
        //Associa os dados ao objecto UserCourse
        this.user = UserCourse.get("_id").getAsInt();; //ir buscar o max id da bd + 1 
        this.course = UserCourse.get("codCourse").getAsInt();

    }

    public void regist() {
        //Insere na BD
    }

    public UserCourse(int user, int curse) {
        this.user = user;
        this.course = curse;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
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

    private void validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        regist();
        /**
         * Senão Devolve um erro (mas dos amigáveis :D)
         */
    }
}
