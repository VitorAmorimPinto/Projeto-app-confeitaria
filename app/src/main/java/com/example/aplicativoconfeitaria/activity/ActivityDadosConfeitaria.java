package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Confeitaria;
import com.example.aplicativoconfeitaria.model.MaskEditUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ActivityDadosConfeitaria extends AppCompatActivity {
    EditText edtNomeConfeitaria,editTextTelefone;
    TextView txtEnderecoConfeitaria;
    String nome,telefone,endereco;
    Confeitaria confeitaria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_confeitaria);
        edtNomeConfeitaria = findViewById(R.id.edtNomeConfeitaria);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        txtEnderecoConfeitaria = findViewById(R.id.txtEnderecoConfeitaria);
        editTextTelefone.addTextChangedListener(MaskEditUtil.mask(editTextTelefone, MaskEditUtil.FORMAT_FONE));
        informacoesConfeitaria();
    }
    public void informacoesConfeitaria(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    confeitaria = data.getValue(Confeitaria.class);
                    nome = confeitaria.getNome();
                    telefone = confeitaria.getTelefone();
                    endereco = confeitaria.getEndereco();

                    edtNomeConfeitaria.setText(nome);
                    editTextTelefone.setText(telefone);
                    txtEnderecoConfeitaria.setText(endereco);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}