package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CheckSuccessActivity extends AppCompatActivity {
    String url = "http://192.168.50.170:5000";
    String device_code;

    //TODO: здесь нужно исправить разметку. подпись должна быть Checked successfully, а кнопка - Relocate ((ну, или вроде того))

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_success);
        device_code = getIntent().getStringExtra("device_code");
    }

    public void goToMain(View view) {
        Intent intent = new Intent(CheckSuccessActivity.this, MechanicActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    public void onRelocate(View view) {
        double latitude=0, longitude=0;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                latitude = location.getLongitude();
                longitude = location.getLatitude();
                Log.d("DATA", "Have scan result in your app activity :" + device_code + " " + latitude + " " + longitude);
            }
        }

        ServerConnection conn = new ServerConnection(url, this);
        conn.sendPos(device_code, new DeviceCoords(latitude, longitude), "trans");

        Intent intent = new Intent(CheckSuccessActivity.this, TransferSuccessActivity.class);
        intent.putExtra("device_code", device_code);
        startActivity(intent);
    }

    public void gotoSensors(View v) {
        Intent intent = new Intent(CheckSuccessActivity.this, SensorActivity.class);
        intent.putExtra("device_code", device_code);
        startActivity(intent);
    }
}