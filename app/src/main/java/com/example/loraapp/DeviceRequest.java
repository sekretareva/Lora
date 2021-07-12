package com.example.loraapp;

import java.util.Arrays;

public class DeviceRequest {
    DeviceParameters device;

    DeviceRequest(DeviceParameters deviceParameters){
        this.device = deviceParameters;
    }

    @Override
    public String toString() {
        return "{\"device\":{" +
                "\"code\":\"" + device.code + "\"" +
                ", \"qr\":" + Arrays.toString(device.qr) +
                ", \"type\":\"" + device.type + "\"" +
                ", \"period\":" + device.period +
                "}}";
    }
}

class DeviceParameters {
    String code;
    byte[] qr;
    String type;
    int period;
    String freq;

    DeviceParameters (String code, byte[] qr, String type, int period, String freq){
        this.code = code;
        this.qr = qr;
        this.type = type;
        this.period = period;
        this.freq = freq;
    }
}
