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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

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
    //------------------------------- Listar Institui��es ----------------------------------
    //--------------------------------------------------------------------------------------
    public static String getAll(Response response) {
        try {
            String query = "SELECT * FROM tblInstitutions";
            //Devolve 'Ok'
            response.status(200);
            //E a lista de instituicoes
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"N�o foi poss�vel obter as Institui��es.\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //-------------------------- Obter  dados de uma Institui��o ---------------------------
    //--------------------------------------------------------------------------------------   
    public static String getInstitutionData(Response response, String id) {

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
                //Adiciona o array de cursos dessa escola
                JsonArray coursesList = utils.executeSelectCommand("select * from tblCourses where school=" + schoolID);
                school.getAsJsonObject().add("courses", coursesList);
            }
            institution.getAsJsonObject().add("schools", schools);
            //Devolve 'OK'
            response.status(200);
            //E uma mensagem
            return institution.toString();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"N�o foi poss�vel obter a Institui��es com o id:" + id + ".\"}";
        }
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Instituicao ---------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {

        try {
            //Obt�m o ultimo ID
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
            if (!existErro) {
                String insert = "INSERT INTO tblInstitutions values(" + id + "," + '"' + name + '"' + "," + '"' + address + '"' + "," + '"' + image + '"' + ")";
                //Insere, devolve o estado
                response.status(utils.executeIUDCommand(insert));
                // E uma mensagem
                return "{\"text\":\"Institui��o inserida com sucesso!\"}";
            }

        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"N�o foi poss�vel inserir a Institui��o.\"}";
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Update a Institui��o ---------------------------------
    //--------------------------------------------------------------------------------------   
    public String updateInstitution(Response response) {
        try {
            //converte a imagem em b64 para ficheiro e guarda o nome
            this.image = utils.b64ToImage(image, "inst" + id);
            String update = "UPDATE tblInstitutions SET name='" + name + "',address='" + address + "',image='" + image + "' where id=" + id;
            response.status(utils.executeIUDCommand(update));
            return "{\"text\":\"Institui��o alterada com sucesso!\"}";
        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"N�o � poss�vel editar a Institui��o  (" + id + ") porque existem escolas associadas.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"N�o foi poss�vel alterar a Institui��o.\"}";
        }
    }

//--------------------------------------------------------------------------------------
    //------------------------------- Apagar Institui��o -----------------------------------
    //--------------------------------------------------------------------------------------
    public static String delete(Response response, int id) {
        try {
            //Verifica se a institui��o tem escolas associadas, se tiver devolve um erro e n�o a apaga
            String query = "SELECT * from tblSchools WHERE institution=" + id;
            System.out.println(query);

            String deleted = utils.deleteRegist(id, "tblInstitutions");
            response.status(utils.executeIUDCommand(deleted));
            return "{\"text\":\"Institui��o apagada com sucesso.\"}";

        } catch (MySQLIntegrityConstraintViolationException ex) {
            response.status(400);
            return "{\"text\":\"N�o � poss�vel apagar a Institui��o  (" + id + ") porque existem escolas associadas.\"}";
        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"N�o foi poss�vel apagar a institui�ao.\"}";

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
                    respostasErro[0] = "Nome da Institui��o inv�lido";
                }
                if (!addressValid) {
                    respostasErro[1] = "Morada da Institui��o inv�lida";
                }
                if (!imageValid) {
                    respostasErro[2] = "Imagem da Institui��o inv�lida";
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
