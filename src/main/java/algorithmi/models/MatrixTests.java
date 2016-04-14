/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Pedro Batista
 */
public class MatrixTests {
    private int _id;
    private String name;
    private int teacher;
    private int course;
    private Date date;
    private Date startingTime;
    private Date finishingTime;

    public MatrixTests(String data) {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject matrixTests = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(matrixTests.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        validateData();
        
        //TEM DE IR BUSCAR O ULTIMO ID E ACRESCENTAR UM
        this._id = 123;
        this.name = matrixTests.get("name").getAsString();
        this.teacher = matrixTests.get("teacher").getAsInt();
        this.course = matrixTests.get("course").getAsInt();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date dt;
        try {
            dt = df.parse(matrixTests.get("date").getAsString());
        } catch (ParseException ex) {
            dt = new Date();
        }
        this.date = dt;
        this.startingTime = startingTime;
        this.finishingTime = finishingTime;
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

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getFinishingTime() {
        return finishingTime;
    }

    public void setFinishingTime(Date finishingTime) {
        this.finishingTime = finishingTime;
    }
    
   public void regist() {
        //Insere na BD
    }
    
    // converts a java object to JSON format,
    // and returned as JSON formatted string
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }

    private void validateData() {
        //Se estiver tudo OK, inserer na BD
        regist();
    }

}

