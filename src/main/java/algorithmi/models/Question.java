/*
 * Copyright 2016 Pedro Dias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithmi.Models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Pedro Dias
 */
public class Question {

    private int ID_Pergunta;
    private String Titulo;
    private String Categoria;
    private String Descricao;
    private byte Imagem;
    private byte Algoritmo;

    public Question(String data) {
        //Transforma a string recebida pelo pedido http para json
        JsonParser jsonParser = new JsonParser();
        JsonObject question = (JsonObject) jsonParser.parse(data);
        //Exibe os dados, em formato json
        System.out.println(question.entrySet());
        /**
         *
         * Revalidar TUDO, formatos, campos vazios, TUDO!!
         *
         */
        validateData();
        //Associa os dados ao objecto Question
        this.ID_Pergunta = 123; //ir buscar o max id da bd + 1 
        this.Titulo = Question.get("titulo").getAsString();
        this.Categoria = Question.get("categoria").getAsString();
        this.Descricao = Question.get("descricao").getAsString();
        this.Imagem = Question.get("imagem");
        this.Algoritmo = Question.get("algoritmo");
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

    private void validateData() {
        /**
         * Se estiver tudo OK, inserer na BD,
         */
        regist();
        /**
         * Senão Devolve um erro (mas dos amigáveis :D)
         */
    }
    public void regist() {
        //Insere na BD
    }

    /**
     * @return the ID_Pergunta
     */
    public int getID_Pergunta() {
        return ID_Pergunta;
    }

    /**
     * @param ID_Pergunta the ID_Pergunta to set
     */
    public void setID_Pergunta(int ID_Pergunta) {
        this.ID_Pergunta = ID_Pergunta;
    }

    /**
     * @return the Titulo
     */
    public String getTitulo() {
        return Titulo;
    }

    /**
     * @param Titulo the Titulo to set
     */
    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    /**
     * @return the Categoria
     */
    public String getCategoria() {
        return Categoria;
    }

    /**
     * @param Categoria the Categoria to set
     */
    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    /**
     * @return the Descricao
     */
    public String getDescricao() {
        return Descricao;
    }

    /**
     * @param Descricao the Descricao to set
     */
    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    /**
     * @return the Imagem
     */
    public byte getImagem() {
        return Imagem;
    }

    /**
     * @param Imagem the Imagem to set
     */
    public void setImagem(byte Imagem) {
        this.Imagem = Imagem;
    }

    /**
     * @return the Algoritmo
     */
    public byte getAlgoritmo() {
        return Algoritmo;
    }

    /**
     * @param Algoritmo the Algoritmo to set
     */
    public void setAlgoritmo(byte Algoritmo) {
        this.Algoritmo = Algoritmo;
    }

}
