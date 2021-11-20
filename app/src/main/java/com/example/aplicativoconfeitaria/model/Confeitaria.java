package com.example.aplicativoconfeitaria.model;

public class Confeitaria {
    private String endereco,nome,telefone;

    public Confeitaria() {
    }

    public Confeitaria(String endereco, String nome, String telefone) {
        this.endereco = endereco;
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
