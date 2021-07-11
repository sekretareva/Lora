package com.example.loraapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    List<List<String>> devices;
    Context context;

    DeviceAdapter(List<List<String>> devices, Context context){
        this.devices = devices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<String> device = devices.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false);

        TextView tvDate = convertView.findViewById(R.id.date);
        TextView tvValue = convertView.findViewById(R.id.value);
        TextView tvRSSI = convertView.findViewById(R.id.rssi);
        tvDate.setText(device.get(3));
        tvValue.setText(device.get(2));
        tvRSSI.setText(device.get(4));

        return convertView;
    }
}
