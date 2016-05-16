/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
import static Utils.utils.connectDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private Date date;
    private String name;
    private Date startingTime;
    private Date finishingTime;
    private int teacher;
    private int course;
    
    public MatrixTests(String data) throws Exception {

        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject matrixTests = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(matrixTests.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        
        this._id = getLastID_MatrixTests() + 1; //ir buscar o max id da bd + 1
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        Date dt;
        try {
            dt = df.parse(matrixTests.get("date").getAsString());
        } catch (ParseException ex) {
            dt = new Date();
        }
        this.date = dt;
        this.name = matrixTests.get("name").getAsString();
        //StartingTime da MatrixTest
        DateFormat st = new SimpleDateFormat("HH:mm:ss");
        st.setLenient(false);
        Date startTime;
        try {
            startTime = st.parse(matrixTests.get("startingTime").getAsString());
        } catch (ParseException ex) {
            startTime = new Date();
        }
        this.startingTime = startTime;  
        //FinishingTime da MatrixTest
        DateFormat ft = new SimpleDateFormat("HH:mm:ss");
        ft.setLenient(false);
        Date finishTime;
        try {
            finishTime = ft.parse(matrixTests.get("finishingTime").getAsString());
        } catch (ParseException ex) {
            finishTime = new Date();
        }
        this.finishingTime = finishTime;
        this.teacher = matrixTests.get("teacher").getAsInt();
        this.course = matrixTests.get("course").getAsInt();
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
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }
    
    //Buscar o Last ID da Tabela MatrixTests
    public static int getLastID_MatrixTests() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblMatrixTests");
    }

    //Registar a MatrixTest
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

            Statement stmtt = connectDatabase();

            stmtt.execute("INSERT INTO tblMatrixTests values(" + _id + "," + '"' + date + '"' + "," + name + "," + '"' + startingTime + '"' + "," + finishingTime + '"' + "," + teacher + '"' + "," + course + '"' + ")");
            ResultSet res = stmtt.getResultSet();
            while (res.next()) {
                status = 200;
            }
            System.out.println(" insert matrixTest nº " + _id);

            stmtt.getConnection().close();
            stmtt.close();
        }
        return status;
    }

    //Update MatrixTests
    public int updateMatrixTests(int _id) throws Exception {
        int status = 0;

        Statement stmtt = utils.connectDatabase();
        stmtt.execute("UPDATE tblMatrixTests SET date=" + date + ",name=" + name + ",startingTime=" + startingTime + ",finishingTime=" + finishingTime + ",teacher=" + teacher + ",course=" + course +" where _id=" + _id + ")");

        ResultSet res = stmtt.getResultSet();

        System.out.println("result update MatrixTests " + res.rowUpdated());

        stmtt.close();
        return status;
    } 
    
    //Apagar MatrixTests
    public int deleteMatrixTests(int _id) throws Exception {
        int status = 400;
        utils utils = new utils();
        boolean deleted = utils.deleteRegist(_id, "tblMatrixTests");
        if (deleted) {
            status = 200;
        }
        return status;
    }

    //Listar MatrixTests
    public static String listMatrixTests_WEB() throws Exception {
        //FALTA FAZER O SELECT
        String query = "SELECT tblInstitutions.`name` as Institutions,tblInstitutions.`address` as Institutions,tblSchools where tblCourses.school=tblSchools._id";
        String obj = utils.querysToJson_String(query);
        System.out.println("list matrixTests  " + obj);
        return obj;
    }
    
    //Validar dados
    private String[] validateData() {

        String respostasErro[] = new String[6];
        boolean valid = false;
        
        boolean dateValid = utils.isThisDateValid(date + "");//0
        boolean nameValid = utils.isString(name,false);//1     
        boolean startingTimeValid = utils.isThisHourValid(startingTime + "");//2
        boolean finishingTimeValid = utils.isThisHourValid(finishingTime + "");//3
        boolean teacherValid = utils.isNumber(Integer.toString(teacher),false);//4
        boolean courseValid = utils.isNumber(Integer.toString(course),false);//5

        valid = dateValid && nameValid && startingTimeValid && finishingTimeValid && teacherValid && courseValid;
        if (!valid) {
            if (!dateValid) {
                respostasErro[0] = "Data da MatrixTest inválido";
            }
            if (!nameValid) {
                respostasErro[1] = "Nome da MatrixTest inválido";
            }
            if (!startingTimeValid) {
                respostasErro[2] = "StartingTime da MatrixTest inválido";
            }
            if (!finishingTimeValid) {
                respostasErro[3] = "FinishingTime da MatrixTest inválido";
            }
            if (!teacherValid) {
                respostasErro[4] = "Teacher da MatrixTest inválido";
            }
            if (!courseValid) {
                respostasErro[5] = "Curso da MatrixTest inválido";
            }
        }
        return respostasErro;
    }

}