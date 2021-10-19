package com.example.aplicativoconfeitaria.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.adapter.MinhaContaAdapter;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.ItensMenu;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MinhaContaActivity extends AppCompatActivity {
    private ListView listOpcoes;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        listOpcoes = findViewById(R.id.listOpcoes);

        ArrayAdapter adapter = new MinhaContaAdapter(this, adicionarItens());
        listOpcoes.setAdapter(adapter);

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
    public void deslogarUsuario(View view){
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
        }

    }
    public void abrirLogin(){
        startActivity(new Intent(this, activity_login.class));
    }

}