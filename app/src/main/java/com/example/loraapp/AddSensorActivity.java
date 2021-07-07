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

    EditText tv_code, tv_type, tv_period, tv_sensor_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        tv_code = findViewById(R.id.code);
        tv_type = findViewById(R.id.type);
        tv_period = findViewById(R.id.period);
        tv_sensor_group = findViewById(R.id.sensor_group);
    }

    public void goToMain(View v){
        Intent intent = new Intent(AddSensorActivity.this, EngineerActivity.class);
        startActivity(intent);
    }

    public void gotoRegisterSuccess(View v) throws IOException {
        String code = tv_code.getText().toString().trim();
        String type = tv_type.getText().toString();
        String period = tv_period.getText().toString();
        String group = tv_sensor_group.getText().toString();

        if (code.length()>0 && type.length()>0 && period.length()>0 && group.length()>0){
            ServerConnection conn = new ServerConnection(url, this);

            QRGenerator qrGenerator = new QRGenerator(code, this);
            conn.sendDev(new DeviceParameters(code, qrGenerator.generate(), type, Integer.parseInt(period)));

            Intent intent = new Intent(AddSensorActivity.this, AddSuccessActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(AddSensorActivity.this, "Заполните все поля ", Toast.LENGTH_SHORT).show();
        }
    }

}