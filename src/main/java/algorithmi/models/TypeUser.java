
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
public class TypeUser {

    private int _id;
    private String name;

    public TypeUser(String data) {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject UserType = (JsonObject) jsonParser.parse(data);

        //Exibe os dados, em formato json
        System.out.println(UserType.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        validateData(UserType);
        //Associa os dados ao objecto UserType
        this._id = UserType.getAsInt(); //ir buscar o max id da bd + 1 
        this.name = UserType.get("name").getAsString();
//       
    }

    public void regist() {
        //Insere na BD
    }

    public TypeUser(int id_Type, String name) {
        this._id = id_Type;
        this.name = name;
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

    public void setName(String Name) {
        this.name = Name;
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

    private void validateData(JsonObject UserType) {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        boolean valid = false;

        boolean nameValid = utils.isString(UserType.get("name").getAsString());
        boolean idValid = utils.isNumber(UserType.get("_id").getAsString());
       
        valid = nameValid && idValid;
       
        if (valid) {
            // regista na base de dados
            regist();
        } else {
            /**
             * Senão Devolve um erro (mas dos amigáveis :D)
             */
        }
    }
}
