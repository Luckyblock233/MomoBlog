package com.example.demo02.utils.Network;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpGetRequest {
    public static synchronized void sendOkHttpGetRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}