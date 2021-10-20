package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {
    private EditText txtEmail,txtSenha;
    private Button btnEntrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.edtLogin);
        txtSenha = findViewById(R.id.edtSenhaLog);
        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtEmail.getText().toString();
                String senha = txtSenha.getText().toString();
                if (!email.isEmpty() && !senha.isEmpty()){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    autenticarUsuario();
                }else{
                    Toast.makeText(activity_login.this,
                            "Preencha todos os campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void autenticarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(activity_login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(activity_login.this,
                                    "Usuário logado",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(activity_login.this,
                                    "Usuário inválido!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void cadUser(View view){
        startActivity(new Intent(this,CadastroUsuarioActivity.class));
        finish();
    }



}