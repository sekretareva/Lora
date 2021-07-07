package com.example.loraapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    }

    ServerConnection(String url, Context context){
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build();
        this.context = context;
    }

    public void getValuesDef(String device_code){
        Connection conn = retrofit.create(Connection.class);
        Call<List<List<String>>> call = conn.getValuesDef(device_code);
        Callback<List<List<String>>> callback = new Callback<List<List<String>>>(){
            @Override
            public void onResponse(Call<List<List<String>>> call, retrofit2.Response<List<List<String>>> response) {
                List<List<String>> r = response.body();
                Log.d("RESULT",response.headers()+" " + response.body());
                if (r!=null && r.size()>0){
                    Log.d("RESULT", r.get(0)+"");
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("FAIL", t.getLocalizedMessage());
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
                Log.d("RESULT",response.headers()+" " + response.body());
                if (r!=null && r.size()>0){
                    Log.d("RESULT", r.get(0)+"");
                }
            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("FAIL", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
    }

    public void sendDev(DeviceParameters deviceParameters){
        Connection conn = retrofit.create(Connection.class);

        Call<String> call = conn.sendDev(new DeviceRequest(deviceParameters));

        Log.d("RESULT",call.request()+"");
        Callback<String> callback = new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String status = response.body();
                Log.d("RESULT",response.headers()+" " + response.body());
                if (status!=null && status.equals("ok")){
                    Intent intent = new Intent(context, RegisterSuccessActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    //TODO: можно сделать и другую активити на подобии RegisterSuccessActivity, только Failed
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("FAIL", t.getLocalizedMessage());
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }

    public void sendPos(String device_code, DeviceCoords coords){
        Connection conn = retrofit.create(Connection.class);

        CoordsRequest coordsRequest = new CoordsRequest(coords);

        Call<String> call = conn.sendPos(device_code, coordsRequest);
        Callback<String> callback = new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                String status = response.body();
                Log.d("RESULT",response.headers()+" " + response.body());
                if (status!=null && status.equals("ok")){
                    Log.d("RESULT", status);
                    Intent intent = new Intent(context, AddSuccessActivity.class);
                    //TODO: AddSuccessActivity сделала на подобие RegisterSuccessActivity, поменяла ссылки, убрала кнопку для регистрации. Поменяй дизайн, если надо
                    context.startActivity(intent);
                }
                else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                //TODO: можно сделать и другую активити на подобии AddSuccessActivity, только Failed
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("FAIL", t.getLocalizedMessage());
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        call.enqueue(callback);
    }

    public boolean checkPos(String device_code, double latitude, double longitude){
        Connection conn = retrofit.create(Connection.class);
        Call<CoordsResponse> call = conn.checkPos(device_code);
        Callback<CoordsResponse> callback = new Callback<CoordsResponse>(){
            @Override
            public void onResponse(Call<CoordsResponse> call, retrofit2.Response<CoordsResponse> response) {
                CoordsResponse coords = response.body();
                Log.d("RESULT",response.headers()+" " + response.body());
                if (coords!=null && Math.abs(coords.latitude-latitude)<0.0001 && Math.abs(coords.longitude-longitude)<0.0001){
                    String pos = coords.latitude + " " + coords.longitude;
                    Log.d("RESULT", pos);
                }
                else{
                    Intent intent = new Intent(context, UNAuthActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CoordsResponse> call, Throwable t) {
                Log.d("FAIL", t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);
        return true;
    }
}

