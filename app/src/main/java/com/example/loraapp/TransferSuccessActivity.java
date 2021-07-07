package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TransferSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_success);
    }

    public void gotoBack(View v) {
        Intent intent = new Intent(TransferSuccessActivity.this, CheckSuccessActivity.class);
        startActivity(intent);
    }

    public void gotoSensors(View v) {
        Intent intent = new Intent(TransferSuccessActivity.this, SensorActivity.class);
        startActivity(intent);
    }
}