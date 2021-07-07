package com.example.loraapp;

public class CoordsRequest {
    DeviceCoords device;

    CoordsRequest(DeviceCoords deviceCoords){
        this.device = deviceCoords;
    }
}

class DeviceCoords{
    double latitude;
    double longitude;

    DeviceCoords(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
