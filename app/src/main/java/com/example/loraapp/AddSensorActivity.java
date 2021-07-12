package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AddSensorActivity extends AppCompatActivity {

    String role;
    String url = "http://192.168.50.170:5000";

    EditText tv_code, tv_type, tv_period, tv_freq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        tv_code = findViewById(R.id.code);
        tv_type = findViewById(R.id.type);
        tv_period = findViewById(R.id.period);
        tv_freq = findViewById(R.id.freq);
    }

    public void gotoMain(View v){
        Intent intent = new Intent(AddSensorActivity.this, EngineerActivity.class);
        startActivity(intent);
    }

    public void gotoAddSuccess(View v) throws IOException {
        String code = tv_code.getText().toString().trim();
        String type = tv_type.getText().toString();
        String period = tv_period.getText().toString();
        String freq = tv_freq.getText().toString();

        if (code.length()>0 && type.length()>0 && period.length()>0 && freq.length()>0){
            ServerConnection conn = new ServerConnection(url, this);

            QRGenerator qrGenerator = new QRGenerator(code, this);
            conn.sendDev(new DeviceParameters(code, qrGenerator.generate(), type, Integer.parseInt(period), freq));
        }
        else{
            Toast.makeText(AddSensorActivity.this, "Заполните все поля ", Toast.LENGTH_SHORT).show();
        }
    }

}