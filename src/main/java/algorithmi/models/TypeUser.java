
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

    private int id;
    private String name;

    public TypeUser(String data) throws Exception {

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
        //Associa os dados ao objecto UserType
        this.id = getLastID_UserTypes() + 1; //ir buscar o max id da bd + 1 
        this.name = UserType.get("name").getAsString();

    }

    public TypeUser(String data, int id) throws Exception {

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
        //Associa os dados ao objecto UserType
        this.id = id; //ir buscar o max id da bd + 1 
        this.name = UserType.get("name").getAsString();

    }

    /**
     * Insere novos registos na tabela
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
            String insert = "INSERT INTO tblUserTypes values(" + id + "," + '"' + name + '"' + ")";

            String tt = utils.executeSelectCommand(insert).toString();
        }
        return status;
    }

    /**
     * obtem o maximo id utilizado na tabela tblUserTypes
     *
     * @return int
     */
    public static int getLastID_UserTypes() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblusertypes");
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
        String respostasErro[] = new String[2];
        boolean valid;

        boolean idValid = utils.isNumber(id + "", false);//0
        boolean nameValid = utils.isString(name, true);//1

        valid = nameValid && idValid;

        if (!valid) {
            if (!idValid) {
                respostasErro[0] = "ID invalido";
            }
            if (!nameValid) {
                respostasErro[1] = "Name invalido";
            }
        }
        return respostasErro;
    }

    /**
     * para actualizar/alterar os dados de um registo na tabela usertypes
     *
     * @param id
     */
    public int updateTypeUser(int id) throws Exception {
        int status = 0;

        //executa driver para ligar à base de dados
        String update = "UPDATE tblUserTypes " + "SET name=" + '"' + name + '"' + " where id=" + id + ")";
        String updat = utils.executeSelectCommand(update).toString();

        return status;

    }

    /**
     * lista os varios tipos de utilizadores existentes
     *
     * @return JsonObject
     */
    public static String listTypesOfUser() throws Exception {
        String query = "select tblusertypes.'name' as Name from tblusertypes ";
        String obj = utils.executeSelectCommand(query).toString();

        return obj;
    }

    /**
     * apaga um tipo de user com o id
     *
     * @param id
     */
    public int deleteType(int id) throws Exception {
        int status = 400;
//        utils utils = new utils();
        String deleted = utils.deleteRegist(id, "tblusertypes");

        return status;
    }

    public TypeUser(int id_Type, String name) {
        this.id = id_Type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

}
