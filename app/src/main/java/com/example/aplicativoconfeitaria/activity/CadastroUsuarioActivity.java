package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Base64;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText txtNome,txtEmail,txtSenha;
    private Button btnCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        txtNome = findViewById(R.id.edtNome);
        txtEmail = findViewById(R.id.edtEmail);
        txtSenha = findViewById(R.id.edtSenhaCad);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = txtNome.getText().toString();
                String email = txtEmail.getText().toString();
                String senha = txtSenha.getText().toString();
                if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()){
                   usuario = new Usuario();
                   usuario.setNome(nome);
                   usuario.setEmail(email);
                   usuario.setSenha(senha);
                   usuario.setNivel(0);
                   cadastrarUsuario();
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Preencha todos os campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvarUser();
                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Sucesso ao cadastrar usuário!",
                            Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    String execao = "";
                    try {
                        throw  task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                       execao = "Digite uma senha mais segura";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        execao = "Por favor, digite um e-mail válido";

                    }catch (FirebaseAuthUserCollisionException e){
                        execao = "Essa conta já existe";

                    }catch (Exception e){
                        execao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this,
                            execao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}