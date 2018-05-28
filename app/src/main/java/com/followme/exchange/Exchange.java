package com.followme.exchange;

import com.followme.bean.User;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/5/26.
 */

public class Exchange {
    private static final OkHttpClient client = new OkHttpClient();

    private Exchange() {
    }

    public static void sendRequestGet(final String url, okhttp3.Callback callback) {
        Request request = new Request
                .Builder()
                .url(url)
                .build();
        Exchange.client.newCall(request).enqueue(callback);
    }

    public static void sendRequestPost(final String url, String json, okhttp3.Callback callback) {
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Exchange.client.newCall(request).enqueue(callback);
    }
}
