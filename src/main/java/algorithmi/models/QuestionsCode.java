/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Response;

/**
 *
 * @author Cris
 */
public class QuestionsCode {

    private int question;
    private int language;
    private String fileName;

    public QuestionsCode(int question, int language, String fileName) {
        this.question = question;
        this.language = language;
        this.fileName = fileName;
    }

    //--------------------------------------------------------------------------------------
    //------------------------------- Inserir Pergunta ---------------------------------
    //--------------------------------------------------------------------------------------  
    public String insert(Response response) {

        try {

            String insert = "Insert into tblcodelangs values(" + question + "," + language + ",'" + fileName + "')";
            System.out.println(insert);
            response.status(utils.executeIUDCommand(insert));
            // E uma mensagem
            return "{\"text\":\"Codigo inserido com sucesso!\"}";

        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(algorithmi.models.Institutions.class.getName()).log(Level.SEVERE, null, ex);
            response.status(400);
            return "{\"text\":\"Esta pergunta já tem código na linguagem escolhida, apague-a antes de inserir uma nova.\"}";
        } catch (Exception ex) {
            Logger.getLogger(algorithmi.models.Institutions.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.status(400);
        return "{\"text\":\"Não foi possível inserir o código.\"}";
    }
}
