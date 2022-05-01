package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class ActivityDetalhesPedido extends AppCompatActivity {
    public String statusText,total,observacaoUsuario,localEntrega,dataEntrega,nomeUser,tituloBolo,descricaoBolo,precoBolo,dataRealizacao,formaPagamento,textoBotao,observacaoConfeiteiro,telefone;
    public TextView txtStatusPedido,txtFormaPagamento,txtTotal,txtObs,txtLocalEntrega,txtDataEntrega,txtNomeUser,txtTituloBoloPedido,txtDescricao,txtPreco,txtDataRealizacao,txtTelefone;
    public EditText txtObservacaoConfeiteiro;
    public String idUser,idBolo;
    public ImageView imgBoloPedido;
    public Bolo boloObj;
    public Pedido pedido;
    public String idPedido;
    public Integer status;
    public Button btnAlterarStatus,btnRecusarPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido);



        txtStatusPedido = findViewById(R.id.txtStatusPedido);
        txtFormaPagamento = findViewById(R.id.txtFormaPagamento);
        txtTotal = findViewById(R.id.txtTotal);
        txtObs = findViewById(R.id.txtObs);
        txtLocalEntrega = findViewById(R.id.txtLocalEntrega);
        txtDataEntrega = findViewById(R.id.txtDataEntrega);
        txtNomeUser = findViewById(R.id.txtNomeUser);
        txtTituloBoloPedido = findViewById(R.id.txtTituloBoloPedido);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        imgBoloPedido = findViewById(R.id.imgBoloPedido);
        txtDataRealizacao = findViewById(R.id.txtDataRealizacao);
        btnAlterarStatus = findViewById(R.id.btnAlterarStatus);
        btnRecusarPedido = findViewById(R.id.btnRecusarPedido);
        txtObservacaoConfeiteiro = findViewById(R.id.txtObservacaoConfeiteiro);
        txtTelefone = findViewById(R.id.txtTelefone);
        Bundle dados = getIntent().getExtras();

        idPedido = (String) dados.getSerializable("idPedido");

        informacoesPedido();
    }
    public void informacoesPedido(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child(idPedido);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pedido = snapshot.getValue(Pedido.class);
                idBolo = pedido.getIdBolo();
                idUser = pedido.getIdUsuario();
                status = pedido.getStatus();
                total = pedido.getValorTotal().toString();
                observacaoUsuario = pedido.getObservacao();
                localEntrega = pedido.getLocalEntrega();
                dataEntrega = pedido.getDataEntrega();
                dataRealizacao = pedido.getDataRealizacao();
                formaPagamento = pedido.getMetodoPagamento();
                observacaoConfeiteiro = pedido.getObservacaoConfeiteiro();
                switch (status){
                    case 0:
                        statusText = "Novo";
                        textoBotao = "Aceitar Pedido";
                        txtStatusPedido.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_novo));
                        break;
                    case 1:
                        statusText = "Em andamento";
                        textoBotao = "Finalizar Pedido";
                        txtStatusPedido.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_em_andamento));
                        btnRecusarPedido.setText("Cancelar Pedido");
                        break;
                    case 2:
                        statusText = "Finalizado";
                        txtStatusPedido.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_finalizado));
                        btnRecusarPedido.setVisibility(View.INVISIBLE);
                        btnAlterarStatus.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        statusText = "Cancelado";
                        txtStatusPedido.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_cancelado));
                        btnRecusarPedido.setVisibility(View.INVISIBLE);
                        btnAlterarStatus.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        statusText = "Recusado";
                        txtStatusPedido.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_recusado));
                        btnRecusarPedido.setVisibility(View.INVISIBLE);
                        btnAlterarStatus.setVisibility(View.INVISIBLE);
                        break;
                }
                if (observacaoUsuario.equals("")) {
                    txtObs.setText("Sem observações");
                }else{
                    txtObs.setText(observacaoUsuario);
                }
                txtStatusPedido.setText(statusText);
                txtTotal.setText("R$ " + total);
                txtLocalEntrega.setText(localEntrega);
                txtDataEntrega.setText(dataEntrega);
                txtDataRealizacao.setText (dataRealizacao);
                txtFormaPagamento.setText(formaPagamento);
                btnAlterarStatus.setText(textoBotao);
                txtObservacaoConfeiteiro.setText(observacaoConfeiteiro);
                informacoesUsuario();
                InformacoesBolo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void informacoesUsuario(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUser);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario u = snapshot.getValue(Usuario.class);
                nomeUser = u.getNome();
                telefone = u.getTelefone();


                txtNomeUser.setText(nomeUser);
                txtTelefone.setText(telefone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void InformacoesBolo(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("bolos").child(idBolo);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bolo bolo = snapshot.getValue(Bolo.class);
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                boloObj = bolo;
                tituloBolo = bolo.getNome();
                descricaoBolo = bolo.getDescricao();
                precoBolo = decimalFormat.format(bolo.getPreco());
                Uri url = Uri.parse(bolo.getFoto());

                txtTituloBoloPedido.setText(tituloBolo);
                txtDescricao.setText(descricaoBolo);
                txtPreco.setText("R$ "+precoBolo);

                if(url != null){
                    Glide.with(getApplicationContext())
                            .load(url)
                            .into(imgBoloPedido);
                }else{
                    imgBoloPedido.setImageResource(R.drawable.boloqq);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void alterarStatusPedido(View view){
        String[] estadosPedidido = {"aceitar", "finalizar"};
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child(idPedido);
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityDetalhesPedido.this);
        alert.setTitle(" Pedido");
        alert.setMessage("Deseja realmente "+estadosPedidido[status] +" esse pedido?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (status){
                    case 0:
                        pedido.setStatus(1);
                        break;
                    case 1:
                        pedido.setStatus(2);
                        break;
                }
                pedido.setObservacaoConfeiteiro(txtObservacaoConfeiteiro.getText().toString());
                firebase.setValue(pedido);
                dialog.dismiss();

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetalhesPedido.this);
                alerta.setTitle("Mensagem");
                alerta.setMessage("Status do Pedido atualizado");
                alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alerta.show();
            }

        });
        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
    public void cancelarPedido(View view){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child(idPedido);
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityDetalhesPedido.this);
        alert.setTitle(" Pedido");

        if(pedido.getStatus().equals(0)){
            alert.setMessage("Deseja realmente recusar esse pedido?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    pedido.setStatus(4);
                    pedido.setObservacaoConfeiteiro(txtObservacaoConfeiteiro.getText().toString());

                    firebase.setValue(pedido);
                    dialog.dismiss();

                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetalhesPedido.this);
                    alerta.setTitle("Mensagem");
                    alerta.setMessage("Pedido recusado");
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alerta.show();
                }

            });
        }else{
            alert.setMessage("Deseja realmente cancelar esse pedido?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    pedido.setStatus(3);
                    pedido.setObservacaoConfeiteiro(txtObservacaoConfeiteiro.getText().toString());

                    firebase.setValue(pedido);
                    dialog.dismiss();

                    AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetalhesPedido.this);
                    alerta.setTitle("Mensagem");
                    alerta.setMessage("Pedido cancelado");
                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alerta.show();
                }

            });
        }

        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void goToItem(View view){
        Intent i = new Intent(this, activity_detalhes_item.class);
        i.putExtra("objetoBolo", boloObj);
        startActivity(i);
    }
}