package com.example.aplicativoconfeitaria.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.Bolo;

import java.text.DecimalFormat;

public class ActivityFinalizarPedido extends AppCompatActivity {

    public TextView tvNomeBolo, tvDescricaoBolo, tvPrecoBolo;
    public String nomeBolo, descricaoBolo, ingredientesBolo, precoBolo;
    public ImageView ivImagemBolo;

    Bolo bolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        tvNomeBolo = findViewById(R.id.textViewNomeBoloFinalizarPedido);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoBoloFinalizarPedido);
        tvPrecoBolo = findViewById(R.id.textViewPrecoFinalizarPedido);
        ivImagemBolo = findViewById(R.id.imageViewFinalizarPedido);

        Bundle dados = getIntent().getExtras();
        bolo = (Bolo) dados.getSerializable("objeto");
        preencheDados(bolo);
    }

    public void preencheDados(Bolo boloParametro){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        nomeBolo = boloParametro.getNome();
        descricaoBolo = boloParametro.getDescricao();
        ingredientesBolo = boloParametro.getIngredientes();
        precoBolo = decimalFormat.format(boloParametro.getPreco());
        Uri url = Uri.parse(bolo.getFoto());

        tvNomeBolo.setText(nomeBolo);
        tvDescricaoBolo.setText(descricaoBolo);
        tvPrecoBolo.setText("Preço: R$" + precoBolo  );

        if(url != null){
            Glide.with(ActivityFinalizarPedido.this)
                    .load(url)
                    .into(ivImagemBolo);
        }else{
            ivImagemBolo.setImageResource(R.drawable.imagem_default);
        }
    }

    public void goToDetalhesItem(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}