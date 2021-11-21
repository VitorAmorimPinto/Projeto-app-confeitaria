package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Confeitaria;
import com.example.aplicativoconfeitaria.model.Endereco;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityFinalizarPedido extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView tvNomeBolo, tvDescricaoBolo, tvPrecoBolo, tvDataEntrega, tvLocalDeEntrega;
    private EditText edtObservacoes;
    private String nomeBolo, descricaoBolo, ingredientesBolo, precoBolo, pedidoId, horarioEntrega, emailUsuario, idUsuario, metodoPagamento, enderecoEntregaUsuario, enderecoEntregaConfeitaria;
    private ImageView ivImagemBolo;
    private Button btnFinalizarPedido;
    private Spinner spnHoraEntrega, spnMetodoPagamento;
    private DatabaseReference dbReference = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth autenticacao;
    private Bolo bolo;
    private Pedido pedido;
    private SimpleDateFormat dateFormat;
    private RadioButton rbReceberEmCasa, rbRetirarConfeitaria;
    private RadioGroup rgOpcaoEntrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        tvNomeBolo = findViewById(R.id.textViewNomeBoloFinalizarPedido);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoBoloFinalizarPedido);
        tvPrecoBolo = findViewById(R.id.textViewPrecoFinalizarPedido);
        tvDataEntrega = findViewById(R.id.textViewDataEntregaFinalizarPedido);
        tvLocalDeEntrega = findViewById(R.id.textViewLocalDeEntregaFinalizarPedido);
        edtObservacoes = findViewById(R.id.editTextTextMultiLineObservacoesFinalizarPedido);
        ivImagemBolo = findViewById(R.id.imageViewFinalizarPedido);
        btnFinalizarPedido = findViewById(R.id.buttonFinalizarPedidoUsuario);
        rbReceberEmCasa = findViewById(R.id.radioButtonReceberEmCasa);
        rbRetirarConfeitaria = findViewById(R.id.radioButtonRetirarConfeitaria);
        rgOpcaoEntrega = findViewById(R.id.radioGroupOpcaoEntrega);

        spnHoraEntrega = findViewById(R.id.spinnerHorarioEntregaFinalizarPedidio);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.horarios, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHoraEntrega.setAdapter(adapter);
        spnHoraEntrega.setOnItemSelectedListener(this);

        spnMetodoPagamento = findViewById(R.id.spinnerMetodoPagamentoFinalizarPedidio);
        ArrayAdapter<CharSequence> adapterPagamento = ArrayAdapter.createFromResource(this, R.array.pagamento, android.R.layout.simple_spinner_item);
        adapterPagamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMetodoPagamento.setAdapter(adapterPagamento);
        spnMetodoPagamento.setOnItemSelectedListener(this);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        emailUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(emailUsuario);

        Bundle dados = getIntent().getExtras();
        bolo = (Bolo) dados.getSerializable("objeto");
        preencheDados(bolo);
        recuperarEnderecos();
        radioButton();
    }

    public void cadastraPedido(View view){
        if(!tvDataEntrega.getText().toString().isEmpty()) {

            Date dataPedido = Calendar.getInstance().getTime();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dataPedidoString = dateFormat.format(dataPedido);
            String idBoloPedido = Base64Custom.codificarBase64(bolo.getNome());
            String valorTotalPedido = tvPrecoBolo.getText().toString();
            String localEntregaPedido = tvLocalDeEntrega.getText().toString();
            String observacaoPedido = edtObservacoes.getText().toString();
            String dataHoraEntregaPedido = tvDataEntrega.getText().toString() + " " + horarioEntrega;

            DatabaseReference pedidosReference = dbReference.child("pedidos");
            pedido = new Pedido();

            pedido.setIdBolo(idBoloPedido);
            pedido.setIdUsuario(idUsuario);
            pedido.setValorTotal(Double.parseDouble(valorTotalPedido.replace(",",".")));
            pedido.setMetodoPagamento(metodoPagamento);
            pedido.setDataRealizacao(dataPedidoString);
            pedido.setDataEntrega(dataHoraEntregaPedido);
            pedido.setLocalEntrega(localEntregaPedido);
            pedido.setObservacao(observacaoPedido);
            pedido.setStatus(0);

            pedidosReference.push().setValue(pedido);

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso");
            dialog.setMessage("Seu pedido foi enviado para a confeitaria!");

            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goToPrincipal();
                }
            });

            dialog.create();
            dialog.show();
        }else {
            Toast.makeText(ActivityFinalizarPedido.this,
                    "Escolha a data de entrega para prosseguir",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void preencheDados(Bolo boloParametro){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        nomeBolo = boloParametro.getNome();
        descricaoBolo = boloParametro.getDescricao();
        ingredientesBolo = boloParametro.getIngredientes();
        precoBolo = decimalFormat.format(boloParametro.getPreco());
        Uri url = Uri.parse(bolo.getFoto());

        tvNomeBolo.setText(nomeBolo);
        tvDescricaoBolo.setText(descricaoBolo);
        tvPrecoBolo.setText(precoBolo);

        if(url != null){
            Glide.with(ActivityFinalizarPedido.this)
                    .load(url)
                    .into(ivImagemBolo);
        }else{
            ivImagemBolo.setImageResource(R.drawable.imagem_default);
        }
    }

    public void escolherData(View view){
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH);
        int ano = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(
                ActivityFinalizarPedido.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                ano, mes, dia
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            i1 += 1;

            String data = i2 + "/" + i1 + "/" + i;
            tvDataEntrega.setText(data);
        }
    };

    public void goToDetalhesItem(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void goToPrincipal(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void recuperarEnderecos() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("enderecos");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String t = data.getKey();
                    if (t.equals(idUsuario)) {
                        Endereco dados = data.getValue(Endereco.class);
                        enderecoEntregaUsuario = dados.getLogradouro() + ", nº " + dados.getNumero() + " / " + dados.getBairro() + ", " + dados.getLocalidade();
                        rbReceberEmCasa.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("confeitaria");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                        Confeitaria dados = data.getValue(Confeitaria.class);
                        enderecoEntregaConfeitaria = dados.getEndereco();
                        tvLocalDeEntrega.setText(enderecoEntregaConfeitaria);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void radioButton(){
        rgOpcaoEntrega.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButtonRetirarConfeitaria){
                    tvLocalDeEntrega.setText(enderecoEntregaConfeitaria);
                }else if (i == R.id.radioButtonReceberEmCasa){
                    tvLocalDeEntrega.setText(enderecoEntregaUsuario);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spinnerHorarioEntregaFinalizarPedidio:
                String horario = adapterView.getItemAtPosition(i).toString();
                horarioEntrega = horario;
                break;
            case R.id.spinnerMetodoPagamentoFinalizarPedidio:
                String pagamento = adapterView.getItemAtPosition(i).toString();
                metodoPagamento = pagamento;
                break;
            default:
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
