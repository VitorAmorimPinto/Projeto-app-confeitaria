package com.example.aplicativoconfeitaria.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class BolosFragment extends Fragment {

    private RecyclerView recyclerViewListaBolos;
    private BolosAdapter adapter;
    private ArrayList<Bolo> listaBolos = new ArrayList<>();
    private DatabaseReference bolosRef;
    private ValueEventListener valueEventListenerBolos;
    private SearchView pesquisaView;

    public BolosFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_listagens, container, false);

        // Configurações iniciais
        recyclerViewListaBolos = view.findViewById(R.id.recyclerViewListaBolos);
        bolosRef = ConfiguracaoFirebase.getFirebaseDataBase().child("bolos");

        //Configura o SearchView
        pesquisaView = view.findViewById(R.id.pesquisaView);
        pesquisaView.setQueryHint("Pesquisar bolos");
        pesquisaView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String textoDigitado = s.toUpperCase();
                pesquisarBolos( textoDigitado );
                return true;
            }
        });


        //Configurar adapter
        adapter = new BolosAdapter( listaBolos, getActivity() );

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerViewListaBolos.setLayoutManager( layoutManager );
        recyclerViewListaBolos.setHasFixedSize( true );
        recyclerViewListaBolos.setAdapter( adapter );

        recuperarBolos();

        return view;
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
    private void pesquisarBolos(String texto){
        listaBolos.clear();
        if (texto.length() > 0){

            Query query = bolosRef.orderByChild("nomePesquisa")
                    /*.equalTo(texto + "\uf8ff");*/
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){

                        listaBolos.add( ds.getValue(Bolo.class));
                    }

                    adapter.notifyDataSetChanged();
                    /*int total = listaBolos.size();
                    Log.i("totalbolos","total: " + total);*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            recuperarBolos();
        }
    }

}