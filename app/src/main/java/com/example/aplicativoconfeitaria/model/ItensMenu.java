package com.example.aplicativoconfeitaria.model;

public class ItensMenu {
    private String item1,item2;
    private int imagem;

    public ItensMenu(String item1, String item2, int imagem) {
        this.item1 = item1;
        this.item2 = item2;
        this.imagem = imagem;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
