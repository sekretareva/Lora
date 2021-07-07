package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UNAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_n_auth);
    }

    public void gotoMain(View v) {
        Intent intent = new Intent(UNAuthActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void gotoChoose(View v) {
        Intent intent = new Intent(UNAuthActivity.this, ChooseActivity.class);
        startActivity(intent);
    }
}