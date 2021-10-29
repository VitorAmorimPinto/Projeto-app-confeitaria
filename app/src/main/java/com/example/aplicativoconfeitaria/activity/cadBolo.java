package com.example.aplicativoconfeitaria.activity;

public class cadBolo {

    String nome, descicao;
    Double preco;


    public cadBolo(String nome, String descicao, Double preco) {
        this.nome = nome;
        this.descicao = descicao;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescicao() {
        return descicao;
    }

    public void setDescicao(String descicao) {
        this.descicao = descicao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
