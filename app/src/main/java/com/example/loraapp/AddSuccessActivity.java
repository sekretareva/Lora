package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success);

    }

    public void gotoChoose(View v){
        Intent intent = new Intent(AddSuccessActivity.this, EngineerActivity.class);
        startActivity(intent);
    }

    public void gotoSensors(View v) {
        Intent intent = new Intent(AddSuccessActivity.this, SensorActivity.class);
        startActivity(intent);
    }


}