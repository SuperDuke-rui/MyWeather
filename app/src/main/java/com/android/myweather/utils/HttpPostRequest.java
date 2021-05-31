package com.android.myweather.utils;

import okhttp3.*;

public class HttpPostRequest {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void okhttpPost(String url, RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
