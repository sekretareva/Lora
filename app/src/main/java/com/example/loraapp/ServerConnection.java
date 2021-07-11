package com.example.loraapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ServerConnection {
    Retrofit retrofit;
    Context context;

    interface Connection {
        @GET("/data/{device_code}")
        Call<List<List<String>>> getValuesDef(@Path("device_code") String device_code);

        @GET("/data/{device_code}")
        Call<List<List<String>>> getValues(@Path("device_code") String device_code, @Query("amount") int amount);

        @POST("/register/{device_code}")
        Call<String> sendPos(@Path("device_code") String device_code, @Body CoordsRequest coords);

        @GET("/check/{device_code}")
        Call<CoordsResponse> checkPos(@Path("device_code") String device_code);

        @POST("/register/")
        Call<String> sendDev(@Body DeviceRequest device);

        @PUT("/delete/{device_code}")
        Call<String> delete(@Path("device_code") String device_code);

        @GET("/devices")
        Call<DeviceListResponse> getDevices();
    }

    ServerConnection(String url, Context context){
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        this.context = context;
    }

    public void getValuesDef(String device_code, ListView lv){
        Connection conn = retrofit.create(Connection.class);
        Call<List<List<String>>> call = conn.getValuesDef(device_code);
        Callback<List<List<String>>> callback = new Callback<List<List<String>>>(){
            @Override
            public void onResponse(Call<List<List<String>>> call, retrofit2.Response<List<List<String>>> response) {
                List<List<String>> r = response.body();
                Log.d("RESULT GETVALDEF",response.headers()+" " + response.body());
                if (r!=null && r.size()>0){
                    Log.d("RESULT GETVALDEF", r.get(0)+"");
                    DeviceAdapter adapter = new DeviceAdapter(r, context);
                    lv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("FAIL GETVALDEF", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void getValues(String device_code, int amount){
        Connection conn = retrofit.create(Connection.class);
        Call<List<List<String>>> call = conn.getValues(device_code, amount);
        Callback<List<List<String>>> callback = new Callback<List<List<String>>>(){
            @Override
            public void onResponse(Call<List<List<String>>> call, retrofit2.Response<List<List<String>>> response) {
                List<List<String>> r = response.body();
                Log.d("RESULT GETVAL",response.headers()+" " + response.body());
                if (r!=null && r.size()>0){
                    Log.d("RESULT GETVAL", r.get(0)+"");
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("FAIL GETVAL", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void sendDev(DeviceParameters deviceParameters){
        Connection conn = retrofit.create(Connection.class);

        Call<String> call = conn.sendDev(new DeviceRequest(deviceParameters));

        Callback<String> callback = new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String status = response.body();
                Log.d("RESULT SENDDEV",response.headers()+" " + response.body());
                if (status!=null && status.equals("ok")){
                    Intent intent = new Intent(context, AddSuccessActivity.class);
                    intent.putExtra("device_code", deviceParameters.code);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("FAIL SENDDEV", t.getLocalizedMessage());
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }

    public void sendPos(String device_code, DeviceCoords coords, String mode){
        Connection conn = retrofit.create(Connection.class);

        CoordsRequest coordsRequest = new CoordsRequest(coords);

        Call<String> call = conn.sendPos(device_code, coordsRequest);
        Log.d("RESULT SENDPOS",coords.latitude + " " + coords.longitude);
        Callback<String> callback = new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String status = response.body();
                Log.d("RESULT SENDPOS",response.headers()+" " + response.body() + " " + response);
                if (status!=null && status.equals("ok")){
                    Intent intent = null;
                    if (mode.equals("reg"))
                         intent = new Intent(context, RegisterSuccessActivity.class);
                    else if (mode.equals("trans"))
                        intent = new Intent(context, TransferSuccessActivity.class);
                    context.startActivity(intent);
                }
                else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("FAIL SENDPOS", t.getLocalizedMessage());
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }

    public void checkPos(String device_code, double latitude, double longitude){
        Connection conn = retrofit.create(Connection.class);
        Call<CoordsResponse> call = conn.checkPos(device_code);
        Callback<CoordsResponse> callback = new Callback<CoordsResponse>(){
            @Override
            public void onResponse(Call<CoordsResponse> call, retrofit2.Response<CoordsResponse> response) {
                CoordsResponse coords = response.body();
                Log.d("RESULT CHECKPOS",response.headers()+" " + response.body());
                //if (coords!=null && Math.abs(coords.latitude-latitude)<0.0001 && Math.abs(coords.longitude-longitude)<0.0001){
                if (coords!=null){
                    String pos = coords.latitude + " " + coords.longitude;
                    Log.d("RESULT CHECKPOS", pos);
                    Intent intent = new Intent(context, CheckSuccessActivity.class);
                    intent.putExtra("device_code", device_code);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, UNAuthActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CoordsResponse> call, Throwable t) {
                Log.d("FAIL CHECKPOS", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void setMarker(String device_code, GoogleMap map){
        Connection conn = retrofit.create(Connection.class);
        Call<CoordsResponse> call = conn.checkPos(device_code);
        Callback<CoordsResponse> callback = new Callback<CoordsResponse>(){
            @Override
            public void onResponse(Call<CoordsResponse> call, Response<CoordsResponse> response) {
                CoordsResponse coords = response.body();
                Log.d("RESULT SETMARKER",response.headers()+" " + response.body());
                if (coords!=null){
                    LatLng pos = new LatLng(coords.longitude, coords.latitude);
                    Log.d("RESULT SETMARKER",coords.latitude +" "+ coords.longitude);
                    MarkerOptions marker = new MarkerOptions().position(pos);
                    map.addMarker(marker).setTag(device_code);
                }
            }

            @Override
            public void onFailure(Call<CoordsResponse> call, Throwable t) {
                Log.d("FAIL SETMARKER", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void setCurValue(String device_code, Marker marker){
        if(marker.isInfoWindowShown()){
            marker.hideInfoWindow();
            return;
        }
        else{
            Connection conn = retrofit.create(Connection.class);
            Call<List<List<String>>> call = conn.getValues(device_code, 1);
            Callback<List<List<String>>> callback = new Callback<List<List<String>>>(){
                @Override
                public void onResponse(Call<List<List<String>>> call, retrofit2.Response<List<List<String>>> response) {
                    List<List<String>> r = response.body();
                    Log.d("RESULT SETCURVAL",response.headers()+" " + response.body());
                    marker.setTitle("device: "+marker.getTag());
                    marker.showInfoWindow();
                    if (r!=null && r.size()>0){
                        marker.setTitle(marker.getTitle() + "\n value: " + r.get(0).get(0));
                        marker.showInfoWindow();
                        Log.d("RESULT SETCURVAL", r.get(0)+"");
                    }
                }

                @Override
                public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    Log.d("FAIL SETCURVAL", t.getLocalizedMessage());
                }
            };
            call.enqueue(callback);
        }
    }

    public void delete(String device_code){
        Connection conn = retrofit.create(Connection.class);
        Call<String> call = conn.delete(device_code);
        Callback<String> callback = new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = response.body();
                Log.d("RESULT DELETE",response.headers()+" " + response.body());
                if (status!=null && status.equals("ok")){
                    Log.d("RESULT DELETE","Deleted");
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("RESULT DELETE",t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void getDevices(Runnable f){
        Connection conn = retrofit.create(Connection.class);
        Call<DeviceListResponse> call = conn.getDevices();
        Callback<DeviceListResponse> callback = new Callback<DeviceListResponse>(){
            @Override
            public void onResponse(Call<DeviceListResponse> call, Response<DeviceListResponse> response) {
                DeviceListResponse devices = response.body();
                Log.d("RESULT GETDEV",response.headers()+" " + response.raw());
                if (devices!=null && devices.devices.size()>0){
                    Log.d("RESULT GETDEV", devices.devices.size()+"");
                    String[] recievedDevices = new String[devices.devices.size()];
                    for (int i = 0; i < recievedDevices.length; i++) {
                        recievedDevices[i] = devices.devices.get(i).get(3);
                    }
                    Role deviceList = (Role) context.getApplicationContext();
                    deviceList.setDevices(Arrays.asList(recievedDevices));
                    f.run();
                }
                else
                    Log.d("RESULT GETDEV", "No device");
            }

            @Override
            public void onFailure(Call<DeviceListResponse> call, Throwable t) {
                Log.d("FAIL GETDEV", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }
}

