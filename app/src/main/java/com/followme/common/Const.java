package com.followme.common;

/**
 * Created by Administrator on 2018/5/26.
 */

public class Const {
    public static final String SERVER_URL = "http://47.106.76.243:8080/followme";

    public interface requestURL {
        String LOGIN = Const.SERVER_URL + "/user/login.do";
        String REGISTER=Const.SERVER_URL+ "/user/register.do";
    }
}
