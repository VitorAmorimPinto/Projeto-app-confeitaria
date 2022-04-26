package com.example.aplicativoconfeitaria.model;

import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Confeitaria {
    private String endereco,nome,telefone;

    public Confeitaria() {
    }

    public Confeitaria(String endereco, String nome, String telefone) {
        this.endereco = endereco;
        this.nome = nome;
        this.telefone = telefone;
    }
    public Boolean salvarEnderecoConfeitaria(String idPesquisa){
        try {
            DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria").child(idPesquisa);
            firebase.setValue(this);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public Boolean salvarConfeitaria(String idConfeitaria){
        try{
            DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria").child(idConfeitaria);
            firebase.setValue(this);

            return true;
        }catch (Exception ex){
            return false;
        }
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
