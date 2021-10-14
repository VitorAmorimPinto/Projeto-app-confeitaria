package com.example.aplicativoconfeitaria.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.aplicativoconfeitaria.R;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
    }
}