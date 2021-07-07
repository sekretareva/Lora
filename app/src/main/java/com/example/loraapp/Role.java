package com.example.loraapp;

import android.app.Application;

public class Role extends Application {

    private String role;

    public String getRole(){
        return role;
    }

    public void setRole(String r){
        role = r;
    }
}

