package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.api.CEPService;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Confeitaria;
import com.example.aplicativoconfeitaria.model.Endereco;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NovoEnderecoActivity extends AppCompatActivity {
    Button button3;
    EditText edtCep,edtRua,edtBairro,edtCidade,edtNumero,edtComplemento;
    private FirebaseAuth autenticacao;
    private String idUsuario,idPesquisa;
    Endereco endereco;
    private Boolean ehAdmin = false;
    Confeitaria confeitaria;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_endereco);
        button3 = findViewById(R.id.button3);
        edtCep = findViewById((R.id.edtCep));
        edtRua = findViewById((R.id.edtRua));
        edtBairro = findViewById((R.id.edtBairro));
        edtCidade = findViewById((R.id.edtCidade));
        edtNumero = findViewById((R.id.edtNumero));
        edtComplemento = findViewById((R.id.edtComplemento));
        edtRua.setEnabled(false);
        edtBairro.setEnabled(false);
        edtCidade.setEnabled(false);
        pegarUsuario();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        edtCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (edtCep.getText().length() == 8){
                    recuperarCEPRetrofit();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadEndereco();
            }
        });
    }
    private void recuperarEndereco(){

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("enderecos");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String t = data.getKey();
                    if (t.equals(idPesquisa)) {
                        //do ur stuff
                        Endereco dados = data.getValue(Endereco.class);
                        edtCep.setText(dados.getCep());
                        edtNumero.setText(dados.getNumero());
                        edtComplemento.setText(dados.getComplemento());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void recuperarCEPRetrofit(){
        CEPService cepService = retrofit.create(CEPService.class);
        Call<Endereco> call = cepService.recuperarCEP(edtCep.getText().toString());

        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if(response.isSuccessful()){
                    Endereco cep = response.body();
                    edtRua.setText(cep.getLogradouro());
                    edtBairro.setText(cep.getBairro());
                    edtCidade.setText(cep.getLocalidade());

                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {

            }
        });
    }
    public void pegarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUsuario);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario dadosUsuario = snapshot.getValue(Usuario.class);
                Integer nivel;
                nivel = dadosUsuario.getNivel();
                if (nivel == 10){
                    ehAdmin = true;
                    buscarIdConfeitaria();
                }else{
                    idPesquisa = idUsuario;
                    recuperarEndereco();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void buscarIdConfeitaria(){

        DatabaseReference   firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    confeitaria = data.getValue(Confeitaria.class);
                    idPesquisa = data.getKey();

                    if(idPesquisa != null){
                        recuperarEndereco();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    public void cadEndereco(){
        String logradouro = edtRua.getText().toString();
        String bairro = edtBairro.getText().toString();
        String cidade = edtCidade.getText().toString();
        String cep = edtCep.getText().toString();
        String numero = edtNumero.getText().toString();
        String complemento = edtComplemento.getText().toString();
        Boolean result;
        String mensagem = "";
        if (!logradouro.isEmpty() && !bairro.isEmpty() && !cidade.isEmpty() && cep.length() == 8){
            endereco = new Endereco();
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setLocalidade(cidade);
            endereco.setCep(cep);
            endereco.setIdUsuario(idPesquisa);
            endereco.setComplemento(complemento);
            endereco.setNumero(numero);
            if(ehAdmin){
                String enderecoConfeitaria = logradouro + ", nº " + numero + " / " + bairro + ", " +cidade;
                salvarEnderecoConfeitaria(enderecoConfeitaria);
            }
            result = endereco.salvarEndereco();
            if (result){
                mensagem = "Endereço salvo com sucesso";
            }else{
                mensagem = "Erro ao salvar endereço";
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(NovoEnderecoActivity.this);
            alert.setTitle("Mensagem");
            alert.setMessage(mensagem);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            alert.show();
        }else{
            Toast.makeText(NovoEnderecoActivity.this,
                    "Preencha todos os campos corretamente!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void salvarEnderecoConfeitaria(String endereco){
        DatabaseReference   firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria").child(idPesquisa);
        confeitaria.setEndereco(endereco);
        firebase.setValue(confeitaria);

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
    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();

    }
}