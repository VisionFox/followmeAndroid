package com.followme.observer.attractionList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.exchange.AttractionModuleRequest;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.util.JsonTransform;
import com.followme.util.LatLngUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

//因为http的请求是异步的，我们小组用观察者模式来设置AttractionList
public class AttractionList extends Observable {
    private List<Attraction> attractionList;
    private List<UserPlan> userPlanList;
    private static AttractionList instance;

    private int lenFlag = 0;

    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private static final String TAG = "AttractionList";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case flag_error:
                    Log.d(TAG, "收到错误异常信息为: " + msg.obj);
                    break;
                case flag_success:
                    Log.d(TAG, "收到的信息" + msg.obj);
                    Attraction attraction = JsonTransform.ServerResponseToAttraction((String) msg.obj);
                    addAttraction(attraction);
                    if (attractionList.size() == lenFlag) {
                        notifyObservers();
                    }
                    Log.d("转换得到的景点信息为:", attraction.toString());
                    break;
            }
        }
    };

    //设置为单例模式，使用双重锁校验
    public static AttractionList getInstance() {
        if (instance == null) {
            synchronized (AttractionList.class) {
                if (instance == null) {
                    instance = new AttractionList();
                    instance.attractionList = new ArrayList<>();
                    instance.userPlanList = new ArrayList<>();
                }
            }
        }
        return instance;
    }

    private AttractionList() {
    }

    //将Attraction加入list里
    public void addAttraction(Attraction attraction) {
        LatLng newLatLng = LatLngUtil.coordinateTransform(attraction.getLatitude(), attraction.getLongitude());
        attraction.setLatitude(newLatLng.latitude);
        attraction.setLongitude(newLatLng.longitude);
        attractionList.add(attraction);
        Log.d("加入景点", attraction.toString());
    }

    //通知观察者事件的变化
    public void notifyObservers() {
        this.setChanged();
        this.notifyObservers(attractionList);
    }

    //更新list
    public void updateList() {
        attractionList.clear();
        userPlanList.clear();

        userPlanList.addAll(
                DataSupport
                        .where("uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()))
                        .find(UserPlan.class)
        );
        lenFlag = userPlanList.size();
        if (userPlanList.size() > 0) {
            for (UserPlan t : userPlanList) {
                AttractionModuleRequest.get_attractions_info_by_attraction_id(t.getAttractionId(), mHandler);
            }
        }
    }
}
