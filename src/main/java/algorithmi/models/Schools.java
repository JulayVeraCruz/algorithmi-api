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
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
    //-------------------------- Obter  dados de uma Escola ---------------------------
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
    //------------------------------- Inserir Escola ---------------------------------
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

    //--------------------------------------------------------------------------------------
    //------------------------------- Update a Escola ---------------------------------
    //--------------------------------------------------------------------------------------   
    public String updateSchool(Response response) {
        System.out.println(institution);
        try {

            String update = "UPDATE tblSchools SET name='" + name + "' ,institution=" + institution + " where id=" + id;
            response.status(utils.executeIUDCommand(update));
            return "{\"text\":\"Escola alterada com sucesso!\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível alterar a Escola (" + id + ") porque existem cursos associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível alterar a Escola.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Apagar Escola -----------------------------------
    //--------------------------------------------------------------------------------------
    public static String delete(Response response, int id) {
        try {
            String deleted = utils.deleteRegist(id, "tblSchools");
            response.status(utils.executeIUDCommand(deleted));
            return "{\"text\":\"Escola apagada com sucesso.\"}";

        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível apagar a Escola (" + id + ")  porque existem cursos associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível apagar a Escola.\"}";

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
