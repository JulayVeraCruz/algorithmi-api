/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import algorithmi.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author David
 */
public class Languages {

    private int id;
    private String description;

    //--------------------------------------------------------------------------------------
    //------------------------------- Listar Linguagens ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAll(Response response) {
        try {
            String query = "SELECT * FROM tblHighLevelLangs";
            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter as Linguagens.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Linguagem ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getLanguageData(Response response, String id) {

        try {
            //Obtem a instituicao
            String queryLang = "select * from tblHighLevelLangs where id=" + id;
            JsonObject language = utils.executeSelectCommand(queryLang).get(0).getAsJsonObject();

            //Devolve 'OK'
            response.status(200);
            //E uma mensagem
            return language.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível obter a Linguagem com o id:" + id + ".\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Linguagem ---------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {

        try {
            //Obtém o ultimo ID
            this.id = utils.getLastID("tblHighLevelLangs") + 1;

            boolean existErro = false;
            String[] erros = validateData();
            for (int i = 0; i < erros.length; i++) {
                if (erros[i] == null);
                {
                    existErro = existErro || false;
                }
            }
            if (!existErro) {
                String insert = "INSERT INTO tblHighLevelLangs values(" + id + ",'" + description + "')";
                //Insere, devolve o estado
                response.status(utils.executeIUDCommand(insert));
                // E uma mensagem
                return "{\"text\":\"Linguagem inserida com sucesso!\"}";
            }

        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir a Linguagem.\"}";
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Update a Linguagem ---------------------------------
    //--------------------------------------------------------------------------------------   
    public String updateInstitution(Response response) {
        try {

            String update = "UPDATE tblHighLevelLangs SET description='" + description + "' where id=" + id;
            response.status(utils.executeIUDCommand(update));
            return "{\"text\":\"Linguagem alterada com sucesso!\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível editar a Linguagem  (" + id + ") porque existem dados associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Não foi possível alterar a Linguagem.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Apagar Linguagem -----------------------------------
    //--------------------------------------------------------------------------------------
    public static String delete(Response response, int id) {
        try {

            String deleted = utils.deleteRegist(id, "tblHighLevelLangs");
            response.status(utils.executeIUDCommand(deleted));
            return "{\"text\":\"Linguagem apagada com sucesso.\"}";

        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"Não é possível apagar a Linguagem  (" + id + ") porque existem dados associados.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível apagar a Linguagem.\"}";

    }

    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(description, true);//1
        valid = nameValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome da linguagem inválido";
            }
        }
        return respostasErro;
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

}
