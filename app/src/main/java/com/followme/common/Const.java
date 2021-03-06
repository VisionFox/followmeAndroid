package com.followme.common;

/**
 * Created by Administrator on 2018/5/26.
 */

public class Const {
    public static final String SERVER_URL = "http://47.106.76.243:8080/followme";

    public interface requestURL {
        String LOGIN = Const.SERVER_URL + "/user/login.do";
        String REGISTER = Const.SERVER_URL + "/user/register.do";
        String GET_ATTRACTION_INFO_BY_AREA = Const.SERVER_URL + "/attraction/get_attractions_info_by_area.do";
        String GET_ATTRACTION_INFO_BY_ATTRACTION_ID = Const.SERVER_URL + "/attraction/get_attractions_info_by_attraction_id.do";
    }

    public interface handlerFlag {
        int ERROR = -1;
        int SUCCESS = 0;
        int GET_ATTRACTION_LIST = 1;
        int FAIL = -2;
    }
}
