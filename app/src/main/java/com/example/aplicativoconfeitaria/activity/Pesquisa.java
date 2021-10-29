package com.example.aplicativoconfeitaria.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.model.Bolo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Pesquisa extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Bolo> boloArrayList;
    Myadapter myadapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando dados");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        boloArrayList = new ArrayList<Bolo>();
        myadapter = new Myadapter(Pesquisa.this,boloArrayList);

        recyclerView.setAdapter(myadapter);

        EventChangeListener();

    }

    private void EventChangeListener() {

        db.collection("bolos").orderBy("nome", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Erro de conexão",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                boloArrayList.add(dc.getDocument().toObject(Bolo.class));

                            }

                            myadapter.notifyItemRangeRemoved();

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                    }
                });

    }


}
