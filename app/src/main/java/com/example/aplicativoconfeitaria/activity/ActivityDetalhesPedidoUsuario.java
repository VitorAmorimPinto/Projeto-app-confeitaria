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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class ActivityDetalhesPedidoUsuario extends AppCompatActivity {
    public String statusText,total,observacaoUsuario,localEntrega,dataEntrega,tituloBolo,descricaoBolo,precoBolo,dataRealizacao,formaPagamento,observacaoConfeiteiro;
    public TextView txtStatusPedidoUser,txtFormaPagamentoUser,txtDataRealizacaoUser,txtTotalUser;
    public TextView txtObsUser,txtObsConfeiteiroUser,txtLocalEntregaUser,txtDataEntregaUser;
    public TextView txtTituloBoloPedidoUser,txtDescricaoUser,txtPrecoUser;
    public ImageView imgBoloPedidoUser;
    public String idPedido;
    public Pedido pedido;
    public String idUser,idBolo;
    public Integer status;
    public Button btnCancelarPedidoUser;
    public Bolo boloObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido_usuario);
        txtStatusPedidoUser = findViewById(R.id.txtStatusPedidoUser);
        txtFormaPagamentoUser = findViewById(R.id.txtFormaPagamentoUser);
        txtDataRealizacaoUser = findViewById(R.id.txtDataRealizacaoUser);
        txtTotalUser = findViewById(R.id.txtTotalUser);
        txtObsUser = findViewById(R.id.txtObsUser);
        txtObsConfeiteiroUser = findViewById(R.id.txtObsConfeiteiroUser);
        txtLocalEntregaUser = findViewById(R.id.txtLocalEntregaUser);
        txtTituloBoloPedidoUser = findViewById(R.id.txtTituloBoloPedidoUser);
        txtDescricaoUser = findViewById(R.id.txtDescricaoUser);
        txtPrecoUser = findViewById(R.id.txtPrecoUser);
        txtDataEntregaUser = findViewById(R.id.txtDataEntregaUser);
        imgBoloPedidoUser = findViewById(R.id.imgBoloPedidoUser);
        btnCancelarPedidoUser = findViewById(R.id.btnCancelarPedidoUser);
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
                        txtStatusPedidoUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_novo));
                        break;
                    case 1:
                        statusText = "Em andamento";
                        txtStatusPedidoUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_em_andamento));
                        break;
                    case 2:
                        statusText = "Finalizado";
                        txtStatusPedidoUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_finalizado));
                        btnCancelarPedidoUser.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        statusText = "Cancelado";
                        txtStatusPedidoUser.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.status_cancelado));
                        btnCancelarPedidoUser.setVisibility(View.INVISIBLE);
                        break;
                }
                if (observacaoUsuario.equals("")) {
                    txtObsUser.setText("Sem observações");
                }else{
                    txtObsUser.setText(observacaoUsuario);
                }

                if (observacaoConfeiteiro.equals("")) {
                    txtObsConfeiteiroUser.setText("Sem observações");
                }else{
                    txtObsConfeiteiroUser.setText(observacaoConfeiteiro);
                }

                txtStatusPedidoUser.setText(statusText);
                txtTotalUser.setText("R$ " + total);
                txtLocalEntregaUser.setText(localEntrega);
                txtDataEntregaUser.setText(dataEntrega);
                txtDataRealizacaoUser.setText (dataRealizacao);
                txtFormaPagamentoUser.setText(formaPagamento);
                InformacoesBolo();
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

                txtTituloBoloPedidoUser.setText(tituloBolo);
                txtDescricaoUser.setText(descricaoBolo);
                txtPrecoUser.setText("R$ "+precoBolo);

                if(url != null){
                    Glide.with(getApplicationContext())
                            .load(url)
                            .into(imgBoloPedidoUser);
                }else{
                    imgBoloPedidoUser.setImageResource(R.drawable.boloqq);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void cancelarPedido(View view){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child(idPedido);
        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityDetalhesPedidoUsuario.this);
        alert.setTitle(" Pedido");
        alert.setMessage("Deseja realmente cancelar esse pedido?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                pedido.setStatus(3);

                firebase.setValue(pedido);
                dialog.dismiss();

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityDetalhesPedidoUsuario.this);
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