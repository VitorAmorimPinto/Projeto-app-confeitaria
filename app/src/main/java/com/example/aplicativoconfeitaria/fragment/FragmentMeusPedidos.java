package com.example.aplicativoconfeitaria.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.adapter.PedidosAdminAdapter;
import com.example.aplicativoconfeitaria.adapter.PedidosClienteAdapter;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentMeusPedidos extends Fragment {

    private RecyclerView recyclerView;
    private PedidosClienteAdapter adapter;
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private DatabaseReference pedidosRef;
    private ValueEventListener valueEventListenerPedidos;
    private FirebaseAuth autenticacao;
    String emailUsuario, idUsuario;
    private Query pedidosFiltro;

    public FragmentMeusPedidos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_meus_pedidos, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewListaPedidosCliente);
        pedidosRef = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos");

        pedidosFiltro = pedidosRef.orderByChild("status").startAt(0);

        adapter = new PedidosClienteAdapter( listaPedidos, getActivity() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adapter );
        recuperarPedidos();
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        pedidosRef.removeEventListener( valueEventListenerPedidos );
    }

    @Override
    public void onStart() {
        super.onStart();
        listaPedidos.clear();
        recuperarPedidos();
    }

    public void recuperarPedidos(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        emailUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(emailUsuario);

        valueEventListenerPedidos = pedidosFiltro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPedidos.clear();

                for ( DataSnapshot dados: dataSnapshot.getChildren() ){

                    Pedido pedido = dados.getValue( Pedido.class );
                    pedido.setId(dados.getKey());

                    if(pedido.getIdUsuario().equals(idUsuario)){
                        listaPedidos.add(pedido);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}