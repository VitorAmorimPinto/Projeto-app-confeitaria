package com.example.aplicativoconfeitaria.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.adapter.BolosAdapter;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.ItemPedido;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoFragment extends Fragment {
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
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private Pedido pedidoRecuperado;
    private String statusPedido;
    private RecyclerView recyclerViewListaItensCarrinho;
    private CarrinhoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_pedido_carrinho, container, false);

        recyclerViewListaItensCarrinho = view.findViewById(R.id.recyclerViewListaItensCarrinho);


    }


}
