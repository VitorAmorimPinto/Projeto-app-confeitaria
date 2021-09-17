package com.example.aplicativoconfeitaria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
    }
}