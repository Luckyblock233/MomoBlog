package com.example.demo02.utils.Network;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Post请求
 */
public class HttpPostRequest {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static synchronized void okhttpPost(String url, RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
