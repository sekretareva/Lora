package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
    }

    public void gotoMechanic(View v) {
        Intent intent = new Intent(DataActivity.this, SensorMechanicActivity.class);
        startActivity(intent);
    }

    public void gotoTable(View v) {
        Intent intent = new Intent(DataActivity.this, TableActivity.class);
        startActivity(intent);
    }
}