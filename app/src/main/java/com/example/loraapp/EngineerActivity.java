package com.example.loraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class EngineerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    LatLng myPlace;

    private final int REQUEST_CODE_LOCATION = 1001;

    private LocationManager locationManager;
    Location my_location;
    boolean granted;

    String url = "http://192.168.50.170:5000";

    Button dataButton, deleteButton;
    String chosenDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer);

        dataButton = findViewById(R.id.dataButton);
        deleteButton = findViewById(R.id.deleteButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationListener listener = helpLocation();

        // boolean granted - показывает имеются ли у нас все разрешения для карты
        // разрешения, которые мы запрашиваем :
        // - на определение местоположения
        // - на открытие камеры
        // - на чтение и запись в память устройства

        if (granted || checkPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE})) {
            Log.d("mytag", Boolean.toString(granted));
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 20, listener);
            locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                //my_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                my_location  = new Location(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                myPlace = new LatLng(my_location.getLatitude(), my_location.getLongitude());
            }
        }
    }

    private boolean checkPermission(String[] permissions) {
        // функция для добавления разрешения, если его нет, сюда попадают все разрешения, что мы указали ранее в 87 строчке

        for (int i=0; i<permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this,
                    permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mapFragment.requestPermissions(
                        permissions, REQUEST_CODE_LOCATION);
            }
        }

        if ((ActivityCompat.checkSelfPermission(this,
                permissions[0]) == PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(this,
                permissions[1]) == PackageManager.PERMISSION_GRANTED)) {
            granted = true;
            return true;
        } else return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public LocationListener helpLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location == null) {
                    return;
                } else {
                    myPlace = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        };

        return listener;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // добавление маркера, пока не работает
        mMap = googleMap;

        if (granted == true) {

            mMap.setMyLocationEnabled(true);
            ServerConnection conn = new ServerConnection(url, this);
            conn.getDevices(setMarkers);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
            CameraPosition cameraPosition = new CameraPosition(myPlace, 23, 45, 15);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void gotoMain(View v) {
        Intent intent = new Intent(EngineerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void gotoAddSensor(View v) {
        Intent intent = new Intent(EngineerActivity.this, AddSensorActivity.class);
        startActivity(intent);
    }

    public void gotoData(View v) {
        Intent intent = new Intent(EngineerActivity.this, SensorActivity.class);
        intent.putExtra("device_code", chosenDevice);
        startActivity(intent);
    }

    Runnable setMarkers = new Runnable() {
        @Override
        public void run() {
            ServerConnection conn = new ServerConnection(url, EngineerActivity.this);

            Role deviceList = (Role) getApplicationContext();
            List<String> devices = deviceList.getDevices();

            for (String device : devices){
                Log.d("RESULT DEVICES", device);
                conn.setMarker(device, mMap);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        conn.setCurValue((String) marker.getTag(), marker);
                        if (dataButton.getVisibility() == View.VISIBLE){
                            dataButton.setVisibility(View.INVISIBLE);
                            deleteButton.setVisibility(View.INVISIBLE);
                        }
                        else{
                            dataButton.setVisibility(View.VISIBLE);
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setOnClickListener(v -> {
                                marker.remove();
                                conn.delete(device);
                            });
                            chosenDevice = device;
                        }
                        return true;
                    }
                });
            }
        }
    };
}