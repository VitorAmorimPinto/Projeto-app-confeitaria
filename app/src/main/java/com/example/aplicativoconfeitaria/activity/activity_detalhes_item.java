package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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

    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        tvNomeBolo = findViewById(R.id.textViewNomeDetalheItem);
        tvPrecoBolo = findViewById(R.id.textViewPrecoDetalheItem);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoDetalheItem);
        tvIngedientesBolo = findViewById(R.id.textViewIngredientesDetalheItem);

        recuperarBolo();
    }

    public void recuperarBolo(){
        DatabaseReference boloref = firebaseref.child("bolos").child("f933643dd508b243e527f994c693ba99");

        boloref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bolo bolo = snapshot.getValue(Bolo.class);
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                nomeBolo = bolo.getNome();
                descricaoBolo = bolo.getDescricao();
                ingredientesBolo = bolo.getIngredientes();
                precoBolo = decimalFormat.format(bolo.getPreco());

                tvNomeBolo.setText(nomeBolo);
                tvPrecoBolo.setText("R$ " + precoBolo);
                tvDescricaoBolo.setText(descricaoBolo);
                tvIngedientesBolo.setText(ingredientesBolo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}