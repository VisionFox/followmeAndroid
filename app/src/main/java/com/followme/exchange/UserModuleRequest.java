package com.followme.exchange;


import com.followme.bean.User;
import com.followme.common.Const;

import okhttp3.FormBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class UserModuleRequest {
    private static final OkHttpClient client = new OkHttpClient();

    private UserModuleRequest() {
    }

    public static void login(final String username, final String password, okhttp3.Callback callback) {
        String url = Const.requestURL.LOGIN;
        FormBody.Builder params = new FormBody.Builder();
        params.add("username", username);
        params.add("password", password);
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        UserModuleRequest.client.newCall(request).enqueue(callback);
    }

    public static void register(final User user, okhttp3.Callback callback) {
        String url = Const.requestURL.REGISTER;
        FormBody.Builder params = new FormBody.Builder();
        params.add("username", user.getUsername());
        params.add("password", user.getPassword());
        params.add("email", user.getEmail());
        params.add("phone", user.getPhone());
        params.add("question", user.getQuestion());
        params.add("answer", user.getAnswer());
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        UserModuleRequest.client.newCall(request).enqueue(callback);
    }

}
