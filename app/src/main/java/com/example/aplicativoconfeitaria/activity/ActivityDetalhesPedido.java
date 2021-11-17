package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    public String status,total,observacaoUsuario,localEntrega,dataEntrega,nomeUser,tituloBolo,descricaoBolo,precoBolo,dataRealizacao;
    public TextView txtStatusPedido,txtFormaPagamento,txtTotal,txtObs,txtLocalEntrega,txtDataEntrega,txtNomeUser,txtTituloBoloPedido,txtDescricao,txtPreco,txtDataRealizacao;
    public String idUser,idBolo;
    public ImageView imgBoloPedido;
    public Bolo boloObj;
    public Pedido pedido;
    public String idPedido;


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

        Bundle dados = getIntent().getExtras();

        idPedido = (String) dados.getSerializable("idPedido");

        informacoesPedido();
    }
    public void informacoesPedido(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child(idPedido);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pedido p = snapshot.getValue(Pedido.class);
                idBolo = p.getIdBolo();
                idUser = p.getIdUsuario();
                status = p.getStatus();
                total = p.getValorTotal().toString();
                observacaoUsuario = p.getObservacao();
                localEntrega = p.getLocalEntrega();
                dataEntrega = p.getDataEntrega();
                dataRealizacao = p.getDataRealizacao();
                if (observacaoUsuario.equals("")) {
                    txtObs.setText("Sem observações");
                }else{
                    txtObs.setText(observacaoUsuario);
                }
                txtStatusPedido.setText(status);
                txtTotal.setText(total);
                txtLocalEntrega.setText(localEntrega);
                txtDataEntrega.setText(dataEntrega);
                txtDataRealizacao.setText (dataRealizacao);
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

                txtNomeUser.setText(nomeUser);
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
                txtPreco.setText(precoBolo);

                if(url != null){
                    Glide.with(ActivityDetalhesPedido.this)
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
    public void goToItem(View view){
        Intent i = new Intent(this, activity_detalhes_item.class);
        i.putExtra("objetoBolo", boloObj);
        startActivity(i);
    }
}