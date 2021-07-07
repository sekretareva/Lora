package com.example.loraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spinner_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_role = findViewById(R.id.role);
    }

    public void gotoChooseActivity(View v) {
        String role = spinner_role.getSelectedItem().toString();

        Role r = ((Role)getApplicationContext());
        r.setRole(role);

        Intent intent = new Intent();

        if (role.equals("MECHANIC"))  intent = new Intent(MainActivity.this, ChooseActivity.class);
        else  intent = new Intent(MainActivity.this, EngineerActivity.class);

        startActivity(intent);
    }
}