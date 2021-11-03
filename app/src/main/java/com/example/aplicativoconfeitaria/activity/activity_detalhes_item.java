package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
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
    public Bolo boloEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        tvNomeBolo = findViewById(R.id.textViewNomeDetalheItem);
        tvPrecoBolo = findViewById(R.id.textViewPrecoDetalheItem);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoDetalheItem);
        tvIngedientesBolo = findViewById(R.id.textViewIngredientesDetalheItem);
        ivImagemBolo = findViewById(R.id.imageViewImagemDetalheItem);

        recuperarBolo();
    }


    public void recuperarBolo(){
        DatabaseReference boloref = firebaseref.child("bolos").child("Qm9sbyBGb3JtaWd1ZWlybw==");

        boloref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bolo bolo = snapshot.getValue(Bolo.class);
                boloEnviar = bolo;
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToFinalizarPedido(View view){
        Intent i = new Intent(this, ActivityFinalizarPedido.class);
        i.putExtra("objeto", boloEnviar);
        startActivity(i);
    }
}