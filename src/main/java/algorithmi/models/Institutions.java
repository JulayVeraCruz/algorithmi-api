/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Pedro Batista
 */
public class Institutions {

    private int id;
    private String name;
    private String address;
    private String image;

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Instituicao ---------------------------------
    //--------------------------------------------------------------------------------------  
    public int insert() throws Exception {

        this.id = utils.getLastID("tblInstitutions") + 1;
        //converte a imagem em b64 para ficheiro e guarda o nome
        this.image = utils.b64ToImage(image, "inst" + id);

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
        //converte a imagem em b64 para ficheiro e guarda o nome
        this.image = utils.b64ToImage(image, "inst" + id);
        String update = "UPDATE tblInstitutions SET name='" + name + "',address='" + address + "',image='" + image + "' where id=" + id;
        return utils.executeIUDCommand(update);
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Instituição ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getInstitutionData(String id) {

        try {
            //Obtem a instituicao
            String queryIns = "select * from tblInstitutions where id=" + id;
            JsonObject institution = utils.executeSelectCommand(queryIns).get(0).getAsJsonObject();
            //Obtem as escolas da instituicao
            String querySch = "select * from tblSchools where institution=" + id;
            JsonArray schools = utils.executeSelectCommand(querySch);
            //Por cada escola obtem os cursos
            for (JsonElement school : schools) {
                //Obtem o id da escola
                int schoolID = school.getAsJsonObject().get("id").getAsInt();
                //Adiciona o array de io dessa pergunta
                JsonArray coursesList = utils.executeSelectCommand("select * from tblCourses where school=" + schoolID);
                school.getAsJsonObject().add("courses", coursesList);
            }
            institution.getAsJsonObject().add("schools", schools);
            return institution.toString();
        } catch (Exception ex) {
            return "";
        }
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

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
