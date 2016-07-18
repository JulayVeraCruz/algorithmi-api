/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author David
 */
public class Version {

    private int id;
    private String hash;
    private Date date;
    private String fileName;

    public Version(String hash, Date date, String fileName) {
        this.hash = hash;
        this.date = date;
        this.fileName = fileName;
    }

    public String insert(Response response) {

        try {
            //Obtém o ultimo ID
            this.id = utils.getLastID("tblversion") + 1;
            //Obtem a data actual
            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            //insere na bd
            String insert = "INSERT INTO tblversion values(" + id + "," + '"' + hash + '"' + "," + '"' + fileName + '"' + ",CAST('" + modifiedDate + "' AS DATETIME))";
            //Insere, devolve o estado
            response.status(utils.executeIUDCommand(insert));
            // E uma mensagem
            return "{\"text\":\"Versão inserida com sucesso!\"}";

        } catch (Exception ex) {
            Logger.getLogger(Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir a Versão.\"}";
    }

    public static String getAll() {

        try {

            String query = "select * From tblversion";
            System.out.println(utils.executeSelectCommand(query).toString());
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter Versões.\"}";
        }
    }

}
