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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        this._id = getLastID_MatrixTests() + 1; //ir buscar o max id da bd + 1
        this.name = matrixTests.get("name").getAsString();
        this.teacher = matrixTests.get("teacher").getAsInt();
        this.course = matrixTests.get("course").getAsInt();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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

    public static int getLastID_MatrixTests() {
        utils getid = new utils();
        return getid.getLastID("tblmatrixtests");
    }

    public int regist() {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();

          stmtt.execute("INSERT INTO tblmatrixtests values(" + _id + "," + '"' + name + '"' + "," + teacher + "," + course + "," + '"' + date + '"' + "," + '"' + startingTime + '"'+"," + '"' +finishingTime+ '"' + ")");
//            stmtt.execute("INSERT INTO tblmatrixtests values(1," +'"testname"'+",1,1," + '"2016-29-04"' + "," + '"17:00"'+"," + '"19:00"'+")");

            ResultSet res = stmtt.getResultSet();
            status = 1;//sem erros
            System.out.println(" insert new cursos id" + res.getString(1));

            stmtt.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

     /**
     * apaga um teste com o _id
     *
     * @param _id
     */
    public void deleteTest(int _id) {
        utils utils = new utils();
        utils.deleteRegist(_id, "tblmatrixtests");
    }

     /**
     * para actualizar/alterar os dados de um registo na tabela tests
     *
     * @param _id
     */
    public int updateTest(int _id) {
        int status = 0;
        try {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();ResultSet res = stmtt.getResultSet();
        stmtt.execute("UPDATE tblmatrixtests values(_id=" + _id + ",name=" + '"' + name + '"' + ",teacher=" + teacher + ",course=" + course + ",date=" + '"' + date + '"' + ",startingTime=" + '"' + startingTime + '"'+",finishingTime=" + '"' +finishingTime+ '"' + " where _id=" + _id + ")");

            System.out.println("result update matrix " + res);
            status = 1;
            stmtt.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n" + json);
        return json;
    }

  /**
     * faz a validação dos dados com o retorno de um array podendo conter ou nao
     * mensagens de erro
     *
     * @return String[]
     */
    private String[] validateData() {

        String respostasErro[] = new String[4];
        boolean valid = false;

        boolean nameValid = utils.isString(name);//0
        boolean dateValid = utils.isThisDateValid(date + "");//1
//        boolean startTimeValid = utils.isString(time);//2
//        boolean finishTimeValid = utils.isString(time);//3

        valid = nameValid && dateValid; //&& imageValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome invalido";
            }
            if (!dateValid) {
                respostasErro[1] = "Data invalida";
            }
//            if (!startTimeValid) {
//                respostasErro[2] = "startTime invalido";
//            }
//            if (!finishTimeValid) {
//                respostasErro[3] = "finishTime invalido";
//            }

        }

        return respostasErro;
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

}


