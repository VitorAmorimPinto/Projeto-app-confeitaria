package com.example.aplicativoconfeitaria.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.adapter.BolosAdapter;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BolosFragment extends Fragment {

    private RecyclerView recyclerViewListaBolos;
    private BolosAdapter adapter;
    private ArrayList<Bolo> listaBolos = new ArrayList<>();
    private DatabaseReference bolosRef;
    private ValueEventListener valueEventListenerBolos;

    public BolosFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_listagens, container, false);

        // Configurações iniciais
        recyclerViewListaBolos = view.findViewById(R.id.recyclerViewListaBolos);
        bolosRef = ConfiguracaoFirebase.getFirebaseDataBase().child("bolos");


        //Configurar adapter
        adapter = new BolosAdapter( listaBolos, getActivity() );

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaBolos.setLayoutManager( layoutManager );
        recyclerViewListaBolos.setHasFixedSize( true );
        recyclerViewListaBolos.setAdapter( adapter );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarBolos();
    }

    @Override
    public void onStop() {
        super.onStop();
        bolosRef.removeEventListener( valueEventListenerBolos );
    }

    public void recuperarBolos(){

        valueEventListenerBolos = bolosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for ( DataSnapshot dados: dataSnapshot.getChildren() ){

                    Bolo bolo = dados.getValue( Bolo.class );
                    listaBolos.add ( bolo );

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
