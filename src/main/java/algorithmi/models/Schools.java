/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import algorithmi.Main;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author Pedro Batista
 */
public class Schools {

    private int id;
    private String name;
    private int institution;

    public Schools(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject school = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(school.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!

        //Se o id for nulo (é uma escola nova)
        if (school.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = school.get("id").getAsInt();
        }
        this.name = school.get("name").getAsString();
        this.institution = school.get("institution").getAsInt();
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Listar Escolas ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAll(Response response) {
        try {
            String query = "SELECT tblSchools.id,  tblSchools.name, tblInstitutions.id as institutionID, tblInstitutions.name as institutionName FROM tblSchools, tblInstitutions WHERE tblSchools.institution=tblInstitutions.id";
            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter as Escolas.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Instituição ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getSchoolData(Response response, String id) {

        try {
            //Obtem a escola
            String queryIns = "SELECT tblSchools.id,  tblSchools.name, tblInstitutions.id as institutionID, tblInstitutions.name as institutionName FROM tblSchools, tblInstitutions WHERE tblSchools.institution=tblInstitutions.id and tblSchools.id=" + id;
            JsonObject school = utils.executeSelectCommand(queryIns).get(0).getAsJsonObject();
            //Adiciona o array de cursos dessa escola
            JsonArray coursesList = utils.executeSelectCommand("select * from tblCourses where school=" + id);
            school.getAsJsonObject().add("courses", coursesList);
            //Devolve 'OK'
            response.status(200);
            //E uma mensagem
            return school.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter a Escola com o id:" + id + ".\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Registar Escola --------------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {
        try {
            //Obtém o ultimo ID
            this.id = utils.getLastID("tblSchools") + 1;

            boolean existErro = false;
            String[] erros = validateData();
            for (int i = 0; i < erros.length; i++) {
                if (erros[i] == null);
                {
                    existErro = existErro || false;
                }
            }
            if (!existErro) {
                String insert = "INSERT INTO tblSchools values(" + id + "," + '"' + name + '"' + "," + '"' + institution + '"' + ")";
                //Insere, devolve o estado
                response.status(utils.executeIUDCommand(insert));
                // E uma mensagem
                return "{\"text\":\"Escola inserida com sucesso!\"}";
            }

        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir a Escola.\"}";
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

    //Maximo ID da tabela Escolas
    public static int getLastID() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblSchools");
    }

//--------------------------------------------------------------------------------------
//------------------------------- Update Escola ----------------------------------------
//--------------------------------------------------------------------------------------  
    public int updateSchools(int id) throws Exception {
        int status = 0;

        String update = "UPDATE tblSchools SET name=" + name + ",institution=" + institution + " where id=" + id;
        String updated = utils.executeSelectCommand(update).toString();;
        return status;
    }

//--------------------------------------------------------------------------------------
//------------------------------- Apagar Escola ----------------------------------------
//--------------------------------------------------------------------------------------  
    public static int delete(int id) throws Exception {
        String deleted = utils.deleteRegist(id, "tblSchools");
        return utils.executeIUDCommand(deleted);
    }

//--------------------------------------------------------------------------------------
//------------------------------- Validar Dados Escola ---------------------------------
//--------------------------------------------------------------------------------------  
    private String[] validateData() {
        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(name, false);//0
        boolean institutionValid = utils.isNumber(Integer.toString(institution), false);//1

        valid = nameValid && institutionValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[0] = "Nome da Escola inválido";
                }
                if (!institutionValid) {
                    respostasErro[1] = "Instituição inválida";
                }
            }
        }
        return respostasErro;
    }
}
