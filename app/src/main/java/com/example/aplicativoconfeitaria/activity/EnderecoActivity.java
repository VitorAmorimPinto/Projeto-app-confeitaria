package com.example.aplicativoconfeitaria.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.api.CEPService;
import com.example.aplicativoconfeitaria.model.Endereco;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnderecoActivity extends AppCompatActivity {
    Button button3;
    EditText edtCep,edtRua,edtBairro,edtCidade,edtNumero,edtComplemento;

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
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

}