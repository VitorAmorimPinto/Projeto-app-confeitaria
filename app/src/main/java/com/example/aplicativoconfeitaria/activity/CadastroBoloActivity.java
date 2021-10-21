package com.example.aplicativoconfeitaria.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.google.firebase.database.DatabaseReference;

public class CadastroBoloActivity extends AppCompatActivity {

    private EditText txtNomeBolo, txtPreco, txtIngredientes, txtDescricao;
    private Bolo bolo;
    private Button btnCadastrarBolo;
    private DatabaseReference referencia = ConfiguracaoFirebase.getFirebaseDataBase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_bolo);
        txtNomeBolo = findViewById(R.id.edtNomeBolo);
        txtPreco = findViewById(R.id.edtPreco);
        txtIngredientes = findViewById(R.id.edtIngredientes);
        txtDescricao = findViewById(R.id.edtDescricao);
        btnCadastrarBolo = findViewById(R.id.btnCadastraBolo);

        btnCadastrarBolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String NomeBolo = txtNomeBolo.getText().toString();
                String Preco = txtPreco.getText().toString();
                //Double Preco = Double.parseDouble(txtPreco.getText().toString());
                String Ingredientes = txtIngredientes.getText().toString();
                String Descricao = txtDescricao.getText().toString();

                //Validar se os campos foram preenchidos
                if (!NomeBolo.isEmpty() && !Ingredientes.isEmpty() && !Descricao.isEmpty() && !Preco.isEmpty()) {
                    bolo = new Bolo();
                    bolo.setNome(NomeBolo);
                    bolo.setPreco(Double.parseDouble(Preco));
                    bolo.setIngredientes(Ingredientes);
                    bolo.setDescricao(Descricao);
                    cadastrarBolo();
                    limpaInformacoes();

                } else {
                    Toast.makeText(CadastroBoloActivity.this,
                            "Preencha Todos os campos!",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    public void cadastrarBolo(){
        String mensagem = "";
        try {
            DatabaseReference bolos = referencia.child("bolos");
            bolos.push().setValue(bolo);

            mensagem = "Bolo cadastrado com sucesso!";
        }catch (Exception ex){
            mensagem = "Erro ao cadastrar bolo Tente novamente mais tarde.";
        }
        new AlertDialog.Builder(this)
                .setMessage(mensagem)
                .setTitle("Mensagem")
                .setPositiveButton("Ok", null)
                .show();

    }

    private void limpaInformacoes() {
        txtNomeBolo.setText("");
        txtDescricao.setText("");
        txtIngredientes.setText("");
        txtPreco.setText("");
        bolo = new Bolo();
    }


}