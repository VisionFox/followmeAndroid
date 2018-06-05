package com.followme.exchange;

import android.util.Log;

import com.followme.common.Const;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AttractionModuleRequest {
    private static final OkHttpClient client = new OkHttpClient();

    private AttractionModuleRequest() {
    }

    public static void get_attractions_info_by_area(final String area, okhttp3.Callback callback) {
        String url = Const.requestURL.GET_ATTRACTION_INFO_BY_AREA + "?area=陕西·西安·" + area;
        Log.d("URL: ", url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        AttractionModuleRequest.client.newCall(request).enqueue(callback);
    }
}
