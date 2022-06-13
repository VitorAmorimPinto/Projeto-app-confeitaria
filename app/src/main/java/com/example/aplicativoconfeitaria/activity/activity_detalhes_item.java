package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.ItemPedido;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class activity_detalhes_item extends AppCompatActivity {

    public TextView tvNomeBolo, tvPrecoBolo, tvDescricaoBolo, tvIngedientesBolo;
    public String nomeBolo, descricaoBolo, ingredientesBolo, precoBolo;
    public ImageView ivImagemBolo;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    public Bolo bolo;
    public Button btnComprar;
    private FirebaseAuth autenticacao;
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private Pedido pedidoRecuperado;
    private String idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        tvNomeBolo = findViewById(R.id.textViewNomeDetalheItem);
        tvPrecoBolo = findViewById(R.id.textViewPrecoDetalheItem);
        tvDescricaoBolo = findViewById(R.id.textViewDescricaoDetalheItem);
        tvIngedientesBolo = findViewById(R.id.textViewIngredientesDetalheItem);
        ivImagemBolo = findViewById(R.id.imageViewImagemDetalheItem);
        btnComprar = findViewById(R.id.buttonComprarBoloDetalhesItem);
//        idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        idUsuario = "Y2xpZW50ZUBnbWFpbC5jb20=";
        Bundle dados = getIntent().getExtras();
        bolo = (Bolo) dados.getSerializable("objetoBolo");


        RecuperarPedido();
        recuperarBolo();
    }

    public void addCar(View view){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child("-N4L2Gc8yHiH-zSf5QZ2");
        String idItemBolo = Base64Custom.codificarBase64(bolo.getNome());
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setIdBolo(idItemBolo);
        itemPedido.setNomeBolo(bolo.getNome());
        itemPedido.setQuantidade(1);
        itemPedido.setPreco(bolo.getPreco());
        itemPedido.setFoto(bolo.getFoto());
        itemPedido.setDescricao(bolo.getDescricao());
        itensCarrinho.add( itemPedido);

        pedidoRecuperado.setItens( itensCarrinho );
        pedidoRecuperado.setIdUsuario(idUsuario);
        pedidoRecuperado.setStatus(6);
        pedidoRecuperado.setDataEntrega("");
        pedidoRecuperado.setDataRealizacao("");
        pedidoRecuperado.setIdBolo("");
        pedidoRecuperado.setMetodoPagamento("");
        firebase.setValue(pedidoRecuperado);

        Toast.makeText(activity_detalhes_item.this,
                "Item adicionado ao carrinho",
                Toast.LENGTH_SHORT).show();
    }
    public void RecuperarPedido(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos").child("-N4L2Gc8yHiH-zSf5QZ2");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pedidoRecuperado = snapshot.getValue(Pedido.class);
//                if(!pedidoRecuperado.getItens().equals(null)){
                    itensCarrinho = pedidoRecuperado.getItens();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void recuperarBolo(){
        DecimalFormat decimalFormat = new DecimalFormat("0.##");

        nomeBolo = bolo.getNome();
        descricaoBolo = bolo.getDescricao();
        ingredientesBolo = bolo.getIngredientes();
        precoBolo = decimalFormat.format(bolo.getPreco());
        Uri url = Uri.parse(bolo.getFoto());

        tvNomeBolo.setText(nomeBolo);
        tvPrecoBolo.setText("R$ " + precoBolo);
        tvDescricaoBolo.setText(descricaoBolo);
        tvIngedientesBolo.setText(ingredientesBolo);

        if(url != null){
            Glide.with(activity_detalhes_item.this)
                    .load(url)
                    .into(ivImagemBolo);
        }else{
            ivImagemBolo.setImageResource(R.drawable.imagem_default);
        }
    }

    public void goToFinalizarPedido(View view){
        if(!UsuarioLogado()){
            Toast.makeText(this,
                    "Faça o login para continuar",
                    Toast.LENGTH_SHORT).show();
        }else{
            Intent i = new Intent(this, ActivityFinalizarPedido.class);
            i.putExtra("objeto", bolo);
            startActivity(i);
        }

    }

    public Boolean UsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() == null ){

            return false;

        }else{

            return true;
        }

    }
}