package com.example.aplicativoconfeitaria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aplicativoconfeitaria.activity.ActivityPrincipal;
import com.example.aplicativoconfeitaria.activity.EnderecoActivity;
import com.example.aplicativoconfeitaria.activity.activity_login;
import com.example.aplicativoconfeitaria.adapter.MinhaContaAdapter;
import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.model.ItensMenu;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfil extends Fragment {
    private ListView listOpcoes;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    public String nome,email;
    public TextView textViewNome, textViewEmail;
    private Context context;
    public Boolean ehAdmim;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPerfil newInstance(String param1, String param2) {
        FragmentPerfil fragment = new FragmentPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        listOpcoes = v.findViewById(R.id.listOp);
        verificarUsuarioLogado();


        return  v;
    }

    private ArrayList<ItensMenu> adicionarItens() {
        ArrayList<ItensMenu> itens = new ArrayList<ItensMenu>();
        ItensMenu i = new ItensMenu("Dados pessoais",
                "Altere senha e informações pessoais", R.drawable.ic_baseline_person_24);
        itens.add(i);
        i = new ItensMenu("Endereço",
                "Altere suas informações de localidade", R.drawable.ic_baseline_location_on_24);
        itens.add(i);
//        if (this.ehAdmim){
//            i = new ItensMenu("Cadastro de bolos",
//                    "Cadastre seus bolos", R.drawable.ic_baseline_location_on_24);
//            itens.add(i);
//        }
        i = new ItensMenu("Sair da conta",
                "Acesse outra conta", R.drawable.ic_baseline_exit_to_app_24);
        itens.add(i);

        return itens;
    }
    private ArrayList<ItensMenu> esvaziarItens() {
        ArrayList<ItensMenu> itens = new ArrayList<ItensMenu>();


        return itens;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() != null ){
            preecherAdapter();
        }
    }

    public void opcoesLista(int i){
        switch (i){
            case 0 :
//                startActivity(new Intent(this, activity_login.class));
                break;
            case 1 :
                startActivity(new Intent(this.context, EnderecoActivity.class));
                break;
            case 2 :
                this.deslogarUsuario();

                break;
            default:

                break;
        }

    }
    public void inicio(){
        Intent i = new Intent(this.context, ActivityPrincipal.class);
        startActivity(i);
    }
    public void deslogarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signOut();
        getActivity().finish();
        inicio();

    }
    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() == null ){
            abrirLogin();

        }else{
            pegarUsuario();
           preecherAdapter();
        }

    }
    public void preecherAdapter(){
        ArrayAdapter adapter = new MinhaContaAdapter(this.context, adicionarItens());
        listOpcoes.setAdapter(adapter);
        listOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                opcoesLista(position);
            }
        });
    }

    public void abrirLogin(){
        startActivity(new Intent(this.context, activity_login.class));
    }
    public void pegarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUsuario);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario dadosUsuario = snapshot.getValue(Usuario.class);
                nome = dadosUsuario.getNome();
                email = dadosUsuario.getEmail();
//                Integer nivel;
//                nivel = dadosUsuario.getNivel();
//                if (nivel == 10){
//                    ehAdmim = true;
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}