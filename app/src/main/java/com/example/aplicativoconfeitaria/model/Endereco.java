package com.example.aplicativoconfeitaria.model;

import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Endereco {
    private String idUsuario;
    private String numero;
    private String bairro;
    private String cep;
    private String logradouro;
    private String localidade;
    private String uf;
    private String complemento;

    public Endereco() {
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Boolean salvarEndereco(){
        try {
            DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase();
            firebase.child("enderecos").child(this.idUsuario).setValue(this);
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "idUsuario='" + idUsuario + '\'' +
                ", numero=" + numero +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                ", complemento='" + complemento + '\'' +
                '}';
    }
}
