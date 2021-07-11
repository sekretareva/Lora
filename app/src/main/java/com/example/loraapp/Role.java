package com.example.loraapp;

import android.app.Application;

import java.util.List;

public class Role extends Application {

    private String role;
    private List<String> devices;

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String r){
        role = r;
    }
}

