package com.example.aplicativoconfeitaria.model;

public class ItemPedido {
    private String idBolo;
    private String nomeBolo;
    private int quantidade;
    private Double preco;
    private String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdBolo() {
        return idBolo;
    }

    public void setIdBolo(String idBolo) {
        this.idBolo = idBolo;
    }

    public String getNomeBolo() {
        return nomeBolo;
    }

    public void setNomeBolo(String nomeBolo) {
        this.nomeBolo = nomeBolo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
