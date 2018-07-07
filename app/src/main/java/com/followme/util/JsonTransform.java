package com.followme.util;


import android.util.Log;

import com.followme.bean.Attraction;
import com.followme.common.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonTransform {
    private final static Gson gson = new Gson();

    private JsonTransform() {
    }

    public static List<Attraction> ServerResponseToAttractionList(String json) {
        //泛型转换是个难点
        Type type = new TypeToken<ServerResponse<List<Attraction>>>() {
        }.getType();
        ServerResponse<List<Attraction>> res = gson.fromJson(json, type);
        return res.getData();
    }

    public static Attraction ServerResponseToAttraction(String json) {
        Type type = new TypeToken<ServerResponse<Attraction>>() {
        }.getType();
        ServerResponse<Attraction> res = gson.fromJson(json, type);
        return res.getData();
    }

    public static Attraction attractionJsonToattraction(String attractionJson) {
        Attraction res = gson.fromJson(attractionJson, Attraction.class);
        return res;
    }

    public static String attractionToJson(Attraction attraction) {
        String res = gson.toJson(attraction);
        return res;
    }

    public static String attractionListToJson(List<Attraction> attractionList) {
        String res = gson.toJson(attractionList);
        return res;
    }

    public static List<Attraction> attractionListJsonToAttractionList(String attractionListJson) {
        Type type = new TypeToken<List<Attraction>>() {
        }.getType();
        List<Attraction> res = gson.fromJson(attractionListJson, type);
        return res;
    }

    public static ServerResponse jsonToServerResponse(String json) {
        ServerResponse res = gson.fromJson(json, ServerResponse.class);
        return res;
    }
}
