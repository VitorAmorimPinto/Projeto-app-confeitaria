package com.example.aplicativoconfeitaria.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.aplicativoconfeitaria.FragmentCarrinho;
import com.example.aplicativoconfeitaria.FragmentHome;
import com.example.aplicativoconfeitaria.FragmentMeusPedidos;
import com.example.aplicativoconfeitaria.FragmentPerfil;
import com.example.aplicativoconfeitaria.LoginFragment;
import com.example.aplicativoconfeitaria.R;
import com.example.aplicativoconfeitaria.configfirebase.ConfiguracaoFirebase;
import com.example.aplicativoconfeitaria.fragment_pedidos_admin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityPrincipal extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.viewPager, new BolosFragment()).commit();

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
                        case R.id.ic_carrinho:
                            selectedFragment = new FragmentCarrinho();
                            break;
                        case R.id.ic_pedidos:
                            selectedFragment = new fragment_pedidos_admin();
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