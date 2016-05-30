/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Pedro Batista
 */
public class Institutions {

    private int id;
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

        //Se o id for nulo (é uma institution nova)
        if (institutions.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = institutions.get("id").getAsInt();
        }
        this.name = institutions.get("name").getAsString();
        this.address = institutions.get("address").getAsString();
        //Converte a imagem recebida em b64 e grava-a 
        this.image = utils.b64ToImage(institutions.get("image").getAsString(), "inst" + id);

    }

    //obtem os dados da instituicao prontos para o update
    public static String getInstitutionData(String id) {

        try {
            String query = "select * from tblInstitutions where id=" + id;
            JsonArray user = utils.executeSelectCommand(query);
            return user.get(0).getAsJsonObject().toString();
        } catch (Exception ex) {
            return "";
        }

    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    //Maximo ID da tabela Institutions
    public static int getLastID() throws Exception {
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

            String insert = "INSERT INTO tblInstitutions values(" + id + "," + '"' + name + '"' + "," + '"' + address + '"' + "," + '"' + image + '"' + ")";

            return utils.executeIUDCommand(insert);

        }
        return status;
    }

//--------------------------------------------------------------------------------------
//------------------------------- Update a Instituição ---------------------------------
//--------------------------------------------------------------------------------------   
    public int updateInstitution() throws Exception {

        String update = "UPDATE tblInstitutions SET name='" + name + "',address='" + address + "',image='" + image + "' where id=" + id;
        return utils.executeIUDCommand(update);
    }

//--------------------------------------------------------------------------------------
//------------------------------- Apagar Instituição -----------------------------------
//--------------------------------------------------------------------------------------
    public static int delete(int id) throws Exception {

        String deleted = utils.deleteRegist(id, "tblInstitutions");
        return utils.executeIUDCommand(deleted);
    }

//--------------------------------------------------------------------------------------
//------------------------------- Listar Instituições ----------------------------------
//--------------------------------------------------------------------------------------
    public static String getAll() {
        try {
            String query = "SELECT * FROM tblInstitutions";
            String result = utils.executeSelectCommand(query).toString();
            return result;
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Instituições.\"}";
        }
    }

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
                    respostasErro[0] = "Nome da Instituição inválido";
                }
                if (!addressValid) {
                    respostasErro[1] = "Morada da Instituição inválida";
                }
                if (!imageValid) {
                    respostasErro[2] = "Imagem da Instituição inválida";
                }
            }
        }
        return respostasErro;
    }
}
