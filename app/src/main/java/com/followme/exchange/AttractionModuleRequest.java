package com.followme.exchange;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.followme.common.Const;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AttractionModuleRequest {
    private static final OkHttpClient client = new OkHttpClient();

    private AttractionModuleRequest() {
    }

    public static void get_attractions_info_by_area(final String area, final Handler mHandler) {
        String url = Const.requestURL.GET_ATTRACTION_INFO_BY_AREA + "?area=陕西·西安·" + area;
        Log.d("URL: ", url);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("请求出现异常：", e.toString());
                Message msg = Message.obtain();
                msg.what = Const.handlerFlag.ERROR;
                msg.obj = e.getMessage();
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Message msg = Message.obtain();
                msg.what = Const.handlerFlag.SUCCESS;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }

    public static void get_attractions_info_by_attraction_id(final Long attractionId, final Handler mHandler) {
        String url = Const.requestURL.GET_ATTRACTION_INFO_BY_ATTRACTION_ID + "?attractionId=" + attractionId;
        Log.d("URL: ", url);

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("请求出现异常：", e.toString());
                Message msg = Message.obtain();
                msg.what = Const.handlerFlag.ERROR;
                msg.obj = e.getMessage();
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Message msg = Message.obtain();
                msg.what = Const.handlerFlag.SUCCESS;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
        });
    }
}
