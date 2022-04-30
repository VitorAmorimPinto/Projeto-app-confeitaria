package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.adapter.MinhaContaAdapter;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.ItensMenu;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MinhaContaActivity extends AppCompatActivity {
    private ListView listOpcoes;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    public String nome,email;
    public TextView textViewNome, textViewEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        listOpcoes = findViewById(R.id.listOpcoes);
        textViewNome = findViewById(R.id.textViewNome);
        textViewEmail = findViewById(R.id.textViewEmail);

        ArrayAdapter adapter = new MinhaContaAdapter(this, adicionarItens());
        listOpcoes.setAdapter(adapter);

        listOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                opcoesLista(position);
            }
        });


    }
    public void pegarUsuario(){
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
            DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUsuario);

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("Dados pessoa   : ", snapshot.getValue().toString() );
                    Usuario dadosUsuario = snapshot.getValue(Usuario.class);
                    nome = dadosUsuario.getNome();
                    email = dadosUsuario.getEmail();

                    textViewNome.setText(nome);
                    textViewEmail.setText(email);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private ArrayList<ItensMenu> adicionarItens() {
        ArrayList<ItensMenu> itens = new ArrayList<ItensMenu>();
        ItensMenu i = new ItensMenu("Dados pessoais",
                "Altere senha e informações pessoais", R.drawable.ic_baseline_person_24);
        itens.add(i);
        i = new ItensMenu("Endereço",
                "Altere suas informações de localidade", R.drawable.ic_baseline_location_on_24);
        itens.add(i);
        i = new ItensMenu("Sair da conta",
                "Acesse outra conta", R.drawable.ic_baseline_exit_to_app_24);
        itens.add(i);

        return itens;
    }
    public void deslogarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();

    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() == null ){
            abrirLogin();
            finish();
        }else{
            pegarUsuario();

        }

    }
    public void abrirLogin(){
        startActivity(new Intent(this, activity_login.class));
    }
    public void opcoesLista(int i){
        switch (i){
            case 0 :
//                startActivity(new Intent(this, activity_login.class));
                break;
            case 1 :
                startActivity(new Intent(this, NovoEnderecoActivity.class));
                break;
            case 2 :
                this.deslogarUsuario();
                break;
            default:

                break;
        }

    }

}