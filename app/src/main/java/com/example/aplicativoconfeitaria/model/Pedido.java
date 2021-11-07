package com.example.aplicativoconfeitaria.model;

import java.util.Date;

public class Pedido {
    private String id, idUsuario, idBolo, status, localEntrega, observacao, dataEntrega, dataRealizacao;

    private Double valorTotal;

    public Pedido() {
    }

    public String getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(String dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdBolo() {
        return idBolo;
    }

    public void setIdBolo(String idBolo) {
        this.idBolo = idBolo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
