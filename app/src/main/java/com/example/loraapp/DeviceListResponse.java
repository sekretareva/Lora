package com.example.loraapp;

import java.util.List;

public class DeviceListResponse {
    List<List<String>> devices;

    public DeviceListResponse(List<List<String>> devices){
        this.devices = devices;
    }
}
