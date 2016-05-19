/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Pedro Batista
 */
public class Institutions {

    private int _id;
    private String name;
    private String address;
    private String image;

    public Institutions(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(institutions.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
       
        this._id = getLastID_Institutions() + 1; //ir buscar o max id da bd + 1
        this.name = institutions.get("name").getAsString();
        this.address = institutions.get("address").getAsString();
        this.image = institutions.get("image").getAsString();
    }
    
    public Institutions(String data, int id) throws Exception {

        JsonParser jsonParser = new JsonParser();
        JsonObject institutions = (JsonObject) jsonParser.parse(data);
        System.out.println(institutions.entrySet());
       
        this._id = id;
        this.name = institutions.get("name").getAsString();
        this.address = institutions.get("address").getAsString();
        this.image = institutions.get("image").getAsString();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

    //Maximo ID da tabela Institutions
    public static int getLastID_Institutions() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblInstitutions");
    }

//--------------------------------------------------------------------------------------
//------------------------------- Registar Instituicao ---------------------------------
//--------------------------------------------------------------------------------------  
   
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
            String insert = "INSERT INTO tblInstitutions values(" + _id + "," + '"' + name + '"' + "," + '"' + address + '"' + "," + '"' + image + '"' + ")";
            String tt = utils.commandMySQLToJson_String(insert);
            System.out.println(" Inserir Instituição nº " + _id);
        }
        return status;
    }
    
    //public int regist() throws Exception {
        //int status = 0;
        //boolean existErro = false;
        //String[] erros = validateData();
       //for (int i = 0; i < erros.length; i++) {
            //if (erros[i] == null);
            //{
               //existErro = existErro || false;
            //}
        //}
        //if (!existErro) {
            //executa driver para ligar à base de dados
            //Statement stmtt = utils.connectDatabase();
            //String query = "INSERT INTO tblInstitutions values(" + _id + "," + '"' + name + '"' + "," + '"' + address + '"' + "," + '"' + image + '"' + ")";
            //stmtt.execute(query);

            //ResultSet res = stmtt.getResultSet();
            //while (res.next()) {
                //status = 200;
            //}
            //stmtt.close();
        //}
        //return status;
    //}
//--------------------------------------------------------------------------------------
//------------------------------- Update a Instituição ---------------------------------
//--------------------------------------------------------------------------------------   
    
    public int updateInstitutions(int _id) throws Exception {
        int status = 0;
        String update = "UPDATE tblInstitutions SET name=" + name + ",address=" + address + ",image=" + image + " where _id=" + _id;
        String updated = utils.commandMySQLToJson_String(update);
        return status;
    }
    
    //public int updateInstitutions(int _id) throws Exception {
        //int status = 0;

        //Statement stmtt = utils.connectDatabase();
        //stmtt.execute("UPDATE tblInstitutions SET name=" + name + ",address=" + address + ",image=" + image + " where _id=" + _id + ")");

        //ResultSet res = stmtt.getResultSet();

       //System.out.println("result update Institutions " + res.rowUpdated());

        //stmtt.close();
        //return status;
    //}
    
//--------------------------------------------------------------------------------------
//------------------------------- Apagar Instituição -----------------------------------
//--------------------------------------------------------------------------------------

    public String deleteInstitutions(int _id) throws Exception {
        int status = 400;
        String deleted = utils.deleteRegist(_id, "tblInstitutions");
        String del=utils.commandMySQLToJson_String(deleted);
        return del;
    }
    
    //public int deleteInstitutions(int _id) throws Exception {
        //int status = 400;
        //utils utils = new utils();
        //boolean deleted = utils.deleteRegist(_id, "tblInstitutions");
        //if (deleted) {
            //status = 200;
        //}
        //return status;
    //}
    
//--------------------------------------------------------------------------------------
//------------------------------- Listar Instituições ----------------------------------
//--------------------------------------------------------------------------------------

    public static String listInstitutions_WEB() throws Exception {
        String query = "SELECT * FROM tblInstitutions";
        String teste = utils.commandMySQLToJson_String(query);
        return teste;
    }
    
    //public static String listInstitutions_WEB() throws Exception {
        //FALTA FAZER O SELECT
        //String query = "SELECT tblInstitutions.`name` as Institutions,tblInstitutions.`address` as Institutions,tblSchools where tblCourses.school=tblSchools._id";
        //String obj = utils.querysToJson_String(query);
        //System.out.println("list institutions  " + obj);
        //return obj;
    //}

//--------------------------------------------------------------------------------------
//------------------------------- Validar Dados ----------------------------------------
//--------------------------------------------------------------------------------------

    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(name, true);//1
        boolean addressValid = utils.isAddressValid(address);//2
        boolean imageValid = utils.isImageValid(image);//3

        valid = nameValid && addressValid && imageValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[1] = "Nome da Instituição inválido";
                }
                if (!addressValid) {
                    respostasErro[2] = "Morada da Instituição inválida";
                }
                if (!imageValid) {
                    respostasErro[3] = "Imagem da Instituição inválida";
                }
            }
        }
        return respostasErro;
    }   
}
