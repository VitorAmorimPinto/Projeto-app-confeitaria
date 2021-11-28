package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.aplicativoconfeitaria.auxiliar.Base64Custom;
import com.example.aplicativoconfeitaria.fragment.BolosFragment;
import com.example.aplicativoconfeitaria.fragment.FragmentCarrinho;
import com.example.aplicativoconfeitaria.fragment.FragmentMeusPedidos;
import com.example.aplicativoconfeitaria.fragment.FragmentPerfil;
import com.example.aplicativoconfeitaria.fragment.LoginFragment;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.fragment.fragment_pedidos_admin;
import com.example.aplicativoconfeitaria.model.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ActivityPrincipal extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private Boolean ehAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.viewPager, new BolosFragment()).commit();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(autenticacao.getCurrentUser() != null){
            pegarUsuario();
        }

    }
    public void pegarUsuario(){
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(idUsuario);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario dadosUsuario = snapshot.getValue(Usuario.class);
                Integer nivel;
                nivel = dadosUsuario.getNivel();
                if (nivel == 10){
                    ehAdmin = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    Boolean logado;

                    switch(item.getItemId()){
                        case R.id.ic_home:
                            selectedFragment = new BolosFragment();
                            break;
                        case R.id.ic_pedidos:
                            logado = UsuarioLogado();
                            if(logado){
                                if(ehAdmin){
                                    selectedFragment = new fragment_pedidos_admin();

                                }else{
                                    selectedFragment = new FragmentMeusPedidos();
                                }
                            }else{
                                selectedFragment = new LoginFragment();
                            }
                            break;
                        case R.id.ic_perfil:
                           logado = UsuarioLogado();
                           if(logado){
                              selectedFragment = new FragmentPerfil();
                           }else{
                               selectedFragment = new LoginFragment();
                           }
                        default:
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewPager, selectedFragment).commit();
                    return true;
                }

            };
    public Boolean UsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if( autenticacao.getCurrentUser() == null ){

           return false;

        }else{

            return true;
        }

    }

}