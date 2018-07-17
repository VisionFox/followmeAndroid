package com.followme.common;

import android.app.Application;
import android.content.Context;

import com.followme.bean.User;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class MyApplication extends Application {
    private static Context context;

    private static User currentUser;

    //绑定登录用户相关信息
    public static void bindCurrentUser(User user) {
        currentUser=user;
    }

    //获取等前登录用户
    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    //参考《第一行代码》书的全局获取context技巧
    public static Context getContext() {
        return context;
    }
}
