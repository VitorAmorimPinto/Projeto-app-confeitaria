package com.example.aplicativoconfeitaria.model;

import java.io.Serializable;
import com.example.aplicativoconfeitaria.activity.CadastroBoloActivity;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Bolo implements Serializable {
    private String nome, descricao, ingredientes, foto;
    private Double preco;
    private String idBolo;

    public Bolo() {
    }

    public void atualizar(){

        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDataBase();

        DatabaseReference boloRef = database.child("bolos")
                .child( idBolo);

        Map<String, Object> valoresbolo = converterParaMap();

        boloRef.updateChildren( valoresbolo );

    }

    @Exclude
    public Map<String, Object> converterParaMap(){

        HashMap<String, Object> boloMap = new HashMap<>();
        boloMap.put("preço", getPreco() );
        boloMap.put("nome", getNome() );
        boloMap.put("foto", getFoto() );
        boloMap.put("ingredientes", getIngredientes() );
        boloMap.put("descrição", getDescricao());

        return boloMap;

    }

    @Exclude
    public String getIdBolo() {
        return idBolo;
    }
    public void setIdBolo(String idBolo) {
        this.idBolo = idBolo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Double getPreco() { return preco; }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setFot(String foto) { this.foto = foto; }

}
