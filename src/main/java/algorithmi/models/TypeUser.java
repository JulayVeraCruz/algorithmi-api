
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
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FilipeRosa
 */
public class TypeUser {

    private int _id;
    private String name;

    public TypeUser(String data) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

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
        this._id = getLastID_UserTypes() + 1; //ir buscar o max id da bd + 1 
        this.name = UserType.get("name").getAsString();

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
     * Insere novos registos na tabela
     *
     * @return status
     */
    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();

            stmtt.execute("INSERT INTO tblUserTypes values(" + _id + "," + '"' + name + '"' + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result insert Typeuser " + res);

            stmtt.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("insert sql error typeuser: "+ex);
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    /**
     * obtem o maximo id utilizado na tabela tblUserTypes
     *
     * @return int
     */
    public static int getLastID_UserTypes() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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

        boolean idValid = utils.isNumber(_id + "");//0
        boolean nameValid = utils.isString(name);//1

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
     * @param _id
     */
    public void updateTypeUser(int _id) {

        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();

            stmtt.execute("UPDATE tblUserTypes " + "SET name=" + '"' + name + '"' + " where _id=" + _id + ")");

            ResultSet res = stmtt.getResultSet();

            System.out.println("result update usertype " + res);

            stmtt.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("error sql typeuser update; "+ex);
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TypeUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
 * lista os varios tipos de utilizadores existentes
 * @return JsonObject
 */
    public static JsonObject listTypesOfUser(){
        JsonObject obj = new JsonObject();
        JsonArray header = new JsonArray();
        JsonArray list = new JsonArray();

        try (
                //executa driver para ligar à base de dados
                Statement stmtt = utils.connectDatabase()) {

            stmtt.execute("select tblusertypes.'name' as Name from tblusertypes ");

            ResultSet res = stmtt.getResultSet();

            int columnCount = res.getMetaData().getColumnCount();
            ResultSetMetaData metadata = (ResultSetMetaData) res.getMetaData();

            //headers column  name,image,name
            for (int i = 1; i <= columnCount; i++) {
                //header.add(Name);

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

}
