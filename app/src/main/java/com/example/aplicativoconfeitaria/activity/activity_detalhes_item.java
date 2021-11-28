package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.text.DecimalFormat;

public class activity_detalhes_item extends AppCompatActivity {

    public TextView tvNomeBolo, tvPrecoBolo, tvDescricaoBolo, tvIngedientesBolo;
    public String nomeBolo, descricaoBolo, ingredientesBolo, precoBolo;
    public ImageView ivImagemBolo;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    public Bolo bolo;
    public Button btnComprar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        tvNomeBolo = findViewById(R.id.textViewNomeDetalheItem);
        tvPrecoBolo = findViewById(R.id.textViewPrecoDetalheItem);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoDetalheItem);
        tvIngedientesBolo = findViewById(R.id.textViewIngredientesDetalheItem);
        ivImagemBolo = findViewById(R.id.imageViewImagemDetalheItem);
        btnComprar = findViewById(R.id.buttonComprarBoloDetalhesItem);

        Bundle dados = getIntent().getExtras();
        bolo = (Bolo) dados.getSerializable("objetoBolo");

        if(!UsuarioLogado()){
            btnComprar.setVisibility(View.GONE);
        }

        recuperarBolo();
    }


    public void recuperarBolo(){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        nomeBolo = bolo.getNome();
        descricaoBolo = bolo.getDescricao();
        ingredientesBolo = bolo.getIngredientes();
        precoBolo = decimalFormat.format(bolo.getPreco());
        Uri url = Uri.parse(bolo.getFoto());

        tvNomeBolo.setText(nomeBolo);
        tvPrecoBolo.setText("R$ " + precoBolo);
        tvDescricaoBolo.setText(descricaoBolo);
        tvIngedientesBolo.setText(ingredientesBolo);

        if(url != null){
            Glide.with(activity_detalhes_item.this)
                    .load(url)
                    .into(ivImagemBolo);
        }else{
            ivImagemBolo.setImageResource(R.drawable.imagem_default);
        }
    }

    public void goToFinalizarPedido(View view){
        Intent i = new Intent(this, ActivityFinalizarPedido.class);
        i.putExtra("objeto", bolo);
        startActivity(i);
    }

    public Boolean UsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() == null ){

            return false;

        }else{

            return true;
        }

    }
}