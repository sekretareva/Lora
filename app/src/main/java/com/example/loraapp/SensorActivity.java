package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SensorActivity extends AppCompatActivity {

    String device_code;
    ListView lv;
    String url = "http://192.168.50.170:5000";
    LineChart lineChart;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        device_code = getIntent().getStringExtra("device_code");
        lv = findViewById(R.id.listview);

        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);

        ServerConnection conn = new ServerConnection(url, this);
        conn.getValuesDef(device_code, lv, lineChart, barChart);
    }

    public void gotoMain(View v) {
        // добавить роль

        String role = checkRole();
        Intent intent;
        if (role.equals("MECHANIC"))  intent = new Intent(SensorActivity.this, MechanicActivity.class);
        else  intent = new Intent(SensorActivity.this, EngineerActivity.class);
        startActivity(intent);
    }

    protected String checkRole() {
        Role r = ((Role)getApplicationContext());
        return r.getRole();
    }
}