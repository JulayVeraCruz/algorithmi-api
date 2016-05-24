/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmi.models;

import Utils.utils;
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

    private int id;
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

        this.id = getLastID_MatrixTests() + 1; //ir buscar o max id da bd + 1
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

    public MatrixTests(String data, int id) throws Exception {

        JsonParser jsonParser = new JsonParser();
        JsonObject matrixTests = (JsonObject) jsonParser.parse(data);
        System.out.println(matrixTests.entrySet());

        this.id = id;
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
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

//--------------------------------------------------------------------------------------
//------------------------------- Registar MatrixTest ----------------------------------
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
            String insert = "INSERT INTO tblMatrixTests values(" + id + "," + '"' + date + '"' + "," + name + "," + '"' + startingTime + '"' + "," + finishingTime + "," + '"' + teacher + '"' + "," + course + '"' + ")";
            String tt = utils.executeSelectCommand(insert).toString();;
            System.out.println(" Inserido a MatrixTest nº " + id);
        }
        return status;
    }

    //public int regist() throws Exception {
        //boolean existErro = false;
    //String[] erros = validateData();
    //for (int i = 0; i < erros.length; i++) {
    //if (erros[i] == null);
    //{
    //existErro = existErro || false;
    //}
    //}
    //int status = 400;
    //if (!existErro) {
            //Statement stmtt = connectDatabase();
            //stmtt.execute("INSERT INTO tblMatrixTests values(" + id + "," + '"' + date + '"' + "," + name + "," + '"' + startingTime + '"' + "," + finishingTime + '"' + "," + teacher + '"' + "," + course + '"' + ")");
    //ResultSet res = stmtt.getResultSet();
    //while (res.next()) {
    //status = 200;
    //}
    //System.out.println(" insert matrixTest nº " + id);
            //stmtt.getConnection().close();
    //stmtt.close();
    //}
    //return status;
    //}
//--------------------------------------------------------------------------------------
//--------------------------------- Update MatrixTest ----------------------------------
//--------------------------------------------------------------------------------------
    public int updateMatrixTests(int id) throws Exception {
        int status = 0;

        String update = "UPDATE tblMatrixTests SET date=" + date + ",name=" + name + ",startingTime=" + startingTime + ",finishingTime=" + finishingTime + ",teacher=" + teacher + ",course=" + course + " where id=" + id;
        String updated = utils.executeSelectCommand(update).toString();;
        return status;
    }

    //public int updateMatrixTests(int id) throws Exception {
    //int status = 0;
        //Statement stmtt = utils.connectDatabase();
    //stmtt.execute("UPDATE tblMatrixTests SET date=" + date + ",name=" + name + ",startingTime=" + startingTime + ",finishingTime=" + finishingTime + ",teacher=" + teacher + ",course=" + course +" where id=" + id + ")");
        //ResultSet res = stmtt.getResultSet();
        //System.out.println("result update MatrixTests " + res.rowUpdated());
        //stmtt.close();
    //return status;
    //} 
//--------------------------------------------------------------------------------------
//--------------------------------- Apagar MatrixTest ----------------------------------
//--------------------------------------------------------------------------------------
    public String deleteMatrixTests(int id) throws Exception {
        int status = 400;
        String deleted = utils.deleteRegist(id, "tblMatrixTests");
        String del = utils.executeSelectCommand(deleted).toString();;
        return del;
    }

    //public int deleteMatrixTests(int id) throws Exception {
    //int status = 400;
    //utils utils = new utils();
    //boolean deleted = utils.deleteRegist(id, "tblMatrixTests");
    //if (deleted) {
    //status = 200;
    //}
    //return status;
    //}
//--------------------------------------------------------------------------------------
//--------------------------------- Listar MatrixTest ----------------------------------
//--------------------------------------------------------------------------------------
    public static String listMatrixTests_WEB() throws Exception {
        //String query = "SELECT * FROM tblMatrixTests";

        //NÃO ESTÁ BEM FEITO
        String query = "SELECT tblMatrixTests.`name` as MatrixTests,tblUser.`type` as User,tblCourses.`name` as Courses FROM tblMatrixTests, tblUsers where tblMatrixTests.teacher=tblUser.id AND tblMatrixTests.course=tblCourse.id";
        String teste = utils.executeSelectCommand(query).toString();;
        return teste;
    }

    //public static String listMatrixTests_WEB() throws Exception {
    //FALTA FAZER O SELECT
    //String query = "SELECT tblInstitutions.`name` as Institutions,tblInstitutions.`address` as Institutions,tblSchools where tblCourses.school=tblSchools.id";
    //String obj = utils.querysToJson_String(query);
    //System.out.println("list matrixTests  " + obj);
    //return obj;
    //}
//--------------------------------------------------------------------------------------
//------------------------------- Validar Dados MatrixTest -----------------------------
//--------------------------------------------------------------------------------------
    private String[] validateData() {

        String respostasErro[] = new String[6];
        boolean valid = false;

        boolean dateValid = utils.isThisDateValid(date + "");//0
        boolean nameValid = utils.isString(name, false);//1     
        boolean startingTimeValid = utils.isThisHourValid(startingTime + "");//2
        boolean finishingTimeValid = utils.isThisHourValid(finishingTime + "");//3
        boolean teacherValid = utils.isNumber(Integer.toString(teacher), false);//4
        boolean courseValid = utils.isNumber(Integer.toString(course), false);//5

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
