package com.example.aplicativoconfeitaria;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicativoconfeitaria.adapter.BolosAdapter;
import com.example.aplicativoconfeitaria.adapter.PedidosAdminAdapter;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.example.aplicativoconfeitaria.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_pedidos_admin extends Fragment {

    private RecyclerView recyclerView;
    private PedidosAdminAdapter adapter;
    private ArrayList<Pedido> listaPedidos = new ArrayList<>();
    private DatabaseReference pedidosRef;
    private ValueEventListener valueEventListenerPedidos;



    public fragment_pedidos_admin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pedidos_admin, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewListaPedidosAdmin);
        pedidosRef = ConfiguracaoFirebase.getFirebaseDataBase().child("pedidos");

        adapter = new PedidosAdminAdapter( listaPedidos, getActivity() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adapter );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarPedidos();
    }

    @Override
    public void onStop() {
        super.onStop();
        pedidosRef.removeEventListener( valueEventListenerPedidos );
    }

    public void recuperarPedidos(){

        valueEventListenerPedidos = pedidosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for ( DataSnapshot dados: dataSnapshot.getChildren() ){

                    Pedido pedido = dados.getValue( Pedido.class );
                    listaPedidos.add ( pedido );

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}