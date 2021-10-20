package com.example.aplicativoconfeitaria.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import com.example.aplicativoconfeitaria.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void goToLogin(View view){
        Intent i = new Intent(this, activity_login.class);
        startActivity(i);
    }
    public void goToCadastro(View view){

        Intent i = new Intent(this, CadastroUsuarioActivity.class);
        startActivity(i);
    }
    public void goToPerfil(View view){

        Intent i = new Intent(this, activity_perfil.class);
        startActivity(i);
    }
    public void goToCadastroBolo(View view){
        Intent i = new Intent(this, CadastroBoloActivity.class);
        startActivity(i);
    }

    public void goToDetalheItem(View view){

        Intent i = new Intent(this, activity_detalhes_item.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}