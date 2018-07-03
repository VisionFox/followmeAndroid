package com.followme.common;

import android.app.Application;
import android.content.Context;

import com.followme.bean.User;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class MyApplication extends Application {
    private static Context context;

    private static User currentUser;

    public static void bindCurrentUser(User user) {
        currentUser=user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
