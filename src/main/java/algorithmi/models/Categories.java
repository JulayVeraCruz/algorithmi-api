package algorithmi.models;

import Utils.utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author David
 */
public class Categories {

    private int id;
    private String description;

    public Categories(String data) throws Exception {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject category = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(category.entrySet());

        //Associa os dados ao objecto Question
        //Se o id for nulo (é uma categoria nova)
        if (category.get("id") == null) {
            this.id = getLastID() + 1; //ir buscar o max id da bd + 1
        } else {
            this.id = category.get("id").getAsInt();
        }
        this.description = category.get("description").getAsString();

    }

    //Maximo ID da tabela Categorisas
    public static int getLastID() throws Exception {
        utils getid = new utils();
        return getid.getLastID("tblCategories");
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

    //--------------------------------------------------------------------------------------
//------------------------------- Validar Dados ----------------------------------------
//--------------------------------------------------------------------------------------
    private String[] validateData() {

        String respostasErro[] = new String[3];
        boolean valid = false;
        boolean nameValid = utils.isString(description, true);//1
        valid = nameValid;
        if (!valid) {
            if (!nameValid) {
                respostasErro[0] = "Nome da categoria inválido";
            }
        }
        return respostasErro;
    }

    /**
     * lista as categorias existentes
     *
     * @return []json
     * @throws java.lang.Exception
     */
    public static String getAll() {
        try {
            String query = "SELECT * from tblCategories";
            return utils.executeSelectCommand(query).toString();
        } catch (Exception ex) {
            System.out.println(ex);
            return "{\"resposta\":\"Erro ao obter categorias.\"}";
        }
    }

    public static int delete(int id) throws Exception {

        String deleted = utils.deleteRegist(id, "tblCategories");
        return utils.executeIUDCommand(deleted);
    }

    public int updateCategory() throws Exception {

        String update = "UPDATE tblCategories SET description=" + description + "where id=" + id;
        return utils.executeIUDCommand(update);
    }

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
            String insert = "INSERT INTO tblCategories values(" + id + ",'" + description + "')";
            System.out.println(insert);
            return utils.executeIUDCommand(insert);

        }
        return status;
    }

}
