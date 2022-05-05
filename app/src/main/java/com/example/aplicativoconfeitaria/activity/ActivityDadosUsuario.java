package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Endereco;
import com.example.aplicativoconfeitaria.model.MaskEditUtil;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ActivityDadosUsuario extends AppCompatActivity {
    EditText edtNomeUsuario,editTextTelefoneUsuario;
    TextView txtEnderecoUsuario;
    private FirebaseAuth autenticacao;
    String idUsuario;
    Usuario dadosUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuario);
        edtNomeUsuario = findViewById(R.id.edtNomeUsuario);
        editTextTelefoneUsuario = findViewById(R.id.editTextTelefoneUsuario);
        txtEnderecoUsuario = findViewById(R.id.txtEnderecoUsuario);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

        editTextTelefoneUsuario.addTextChangedListener(MaskEditUtil.mask(editTextTelefoneUsuario, MaskEditUtil.FORMAT_FONE));
        pegarUsuario();
        recuperarEndereco();
    }

    public void pegarUsuario(){

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUsuario);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dadosUsuario = snapshot.getValue(Usuario.class);

                String nome = dadosUsuario.getNome();
                String  telefone = dadosUsuario.getTelefone();
                edtNomeUsuario.setText(nome);
                editTextTelefoneUsuario.setText(telefone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void recuperarEndereco(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("enderecos");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String t = data.getKey();
                    if (t.equals(idUsuario)) {
                        Endereco dados = data.getValue(Endereco.class);
                      String  enderecoEntregaUsuario = dados.getLogradouro() + ", nº " + dados.getNumero() + " / " + dados.getBairro() + ", " + dados.getLocalidade();
                        txtEnderecoUsuario.setText(enderecoEntregaUsuario);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void atualizarDadosUsuario(View view){
        String msg = "";
        String nome = edtNomeUsuario.getText().toString();
        String  telefone = editTextTelefoneUsuario.getText().toString();
        dadosUsuario.setIdUsuario(idUsuario);
        dadosUsuario.setNome(nome);
        dadosUsuario.setTelefone(telefone);

        if(dadosUsuario.atualizarDadosUsuario()){
            msg = "Dados atualizados com sucesso";

        }else{
            msg = "Erro ao atualizar, favor tente mais tarde";

        }
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setTitle("Mensagem")
                .setPositiveButton("Ok", null)
                .show();



    }

    public void goToEndereco(View view){
        startActivity(new Intent(this, NovoEnderecoActivity.class));

    }
}