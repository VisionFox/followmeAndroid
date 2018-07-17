package com.followme.util;

import com.followme.bean.Attraction;
import com.followme.common.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

//因为我们的服务器传回的都是json字符串，就用这个工具类来转换
//还有就是我们向服务器发送信息也是json格式，这里也提供类到json的转换功能
public class JsonTransform {
    private final static Gson gson = new Gson();

    private JsonTransform() {
    }

    //根据服务器响应提取出AttractionList
    public static List<Attraction> ServerResponseToAttractionList(String json) {
        //泛型转换是个难点
        Type type = new TypeToken<ServerResponse<List<Attraction>>>() {
        }.getType();
        ServerResponse<List<Attraction>> res = gson.fromJson(json, type);
        return res.getData();
    }

    //根据服务器响应提取出Attraction
    public static Attraction ServerResponseToAttraction(String json) {
        Type type = new TypeToken<ServerResponse<Attraction>>() {
        }.getType();
        ServerResponse<Attraction> res = gson.fromJson(json, type);
        return res.getData();
    }

    //将attraction的Json字符串还原为attraction类
    public static Attraction attractionJsonToattraction(String attractionJson) {
        Attraction res = gson.fromJson(attractionJson, Attraction.class);
        return res;
    }

    //将attraction转换为json字符串
    public static String attractionToJson(Attraction attraction) {
        String res = gson.toJson(attraction);
        return res;
    }

    //将attractionList转换为json字符串
    public static String attractionListToJson(List<Attraction> attractionList) {
        String res = gson.toJson(attractionList);
        return res;
    }

    //将attractionList的Json字符串还原为attractionList类
    public static List<Attraction> attractionListJsonToAttractionList(String attractionListJson) {
        Type type = new TypeToken<List<Attraction>>() {
        }.getType();
        List<Attraction> res = gson.fromJson(attractionListJson, type);
        return res;
    }

    //将服务器发来的json字符串转换为ServerResponse
    public static ServerResponse jsonToServerResponse(String json) {
        ServerResponse res = gson.fromJson(json, ServerResponse.class);
        return res;
    }
}
