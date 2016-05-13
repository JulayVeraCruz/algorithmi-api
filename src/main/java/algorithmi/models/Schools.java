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
import java.sql.Statement;

/**
 *
 * @author Pedro Batista
 */
public class Schools {
    private int _id;
    private String name;
    private int institution;
    private String image;

    public Schools(String data) throws Exception {
        
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject schools = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(schools.entrySet());
        //Revalidar TUDO, formatos, campos vazios, TUDO!!
        
        this._id = getLastID_Schools()+ 1; //ir buscar o max id da bd + 1
        this.name = schools.get("name").getAsString();
        this.institution = schools.get("institution").getAsInt();
        this.image = schools.get("image").getAsString();
        
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

    public int getInstitution() {
        return institution;
    }

    public void setInstitution(int institution) {
        this.institution = institution;
    }
    
     public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    @Override
    public String toString() {
        Gson gson = new Gson();

        String json = gson.toJson(this);
        System.out.println("json \n"+ json);
        return json;
    }
    
    //Maximo ID da tabela Escolas
    public static int getLastID_Schools() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblSchools");
    }
    
    //Registar
    public int regist() throws Exception {
        int status = 0;
        boolean existErro = false;
        String[] erros = validateData();
        for (int i = 0; i < erros.length; i++) {
            if (erros[i] == null);
            {
                existErro = existErro || false;
            }
        }
        if (!existErro) {
            //executa driver para ligar à base de dados
            Statement stmtt = utils.connectDatabase();
            String query = "INSERT INTO tblSchools values(" + _id + "," + '"' + name + '"' + "," + '"' + institution + '"' + "," + '"' + image + '"' + ")";
            stmtt.execute(query);

            ResultSet res = stmtt.getResultSet();
            while (res.next()) {
                status = 200;
            }
            stmtt.close();
        }
        return status;
    }
    
    //Actualizar Escola
    public int updateSchools(int _id) throws Exception {
        int status = 0;

        Statement stmtt = utils.connectDatabase();
        stmtt.execute("UPDATE tblSchools SET name=" + name + ",institution=" + institution + ",image=" + image + " where _id=" + _id + ")");

        ResultSet res = stmtt.getResultSet();

        System.out.println("result update Schools " + res.rowUpdated());

        stmtt.close();
        return status;
    }
    
    //Apagar Escola
    public int deleteSchools(int _id) throws Exception {
        int status = 400;
        utils utils = new utils();
        boolean deleted = utils.deleteRegist(_id, "tblSchools");
        if (deleted) {
            status = 200;
        }
        return status;
    }
    
    //Validar Dados
    private String[] validateData() {
        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(name,false);//0
        boolean institutionValid = utils.isNumber(Integer.toString(institution),false);//1
        boolean imageValid = utils.isImageValid(image);//2

        valid = nameValid && institutionValid && imageValid;
        if (!valid) {
            {
                if (!nameValid) {
                    respostasErro[0] = "Nome da Escola inválido";
                }
                if (!institutionValid) {
                    respostasErro[1] = "Instituição inválida";
                }
                if (!imageValid) {
                    respostasErro[2] = "Imagem da Escola inválida";
                }
            }
        }
        return respostasErro;
    }
}
