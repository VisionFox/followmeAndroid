package com.followme.observer.attractionList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.followme.bean.Attraction;
import com.followme.common.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class AttractionListObserver implements Observer {
    public List<Attraction> attractionList;
    public static AttractionListObserver instance;
    private Handler handler;

    private AttractionListObserver() {
    }

    //获取单例
    public static AttractionListObserver getInstance() {
        if (instance == null) {
            synchronized (AttractionList.class) {
                if (instance == null) {
                    instance = new AttractionListObserver();
                    instance.attractionList = new ArrayList<>();
                    AttractionList observable = AttractionList.getInstance();
                    observable.addObserver(instance);

                }
            }
        }
        return instance;
    }

    //更新attractionList
    @Override
    public void update(Observable observable, Object o) {
        attractionList.clear();
        attractionList.addAll((ArrayList<Attraction>) o);
        Log.d("全部完成", attractionList.toString() + "\n景点总数为：" + attractionList.size());

        Message msg = Message.obtain();
        msg.what = Const.handlerFlag.GET_ATTRACTION_LIST;
        msg.obj = new ArrayList<>(attractionList);
        handler.sendMessage(msg);
    }


    public void getUserPlanAttractionList(Handler handler) {
        AttractionList observable = AttractionList.getInstance();
        AttractionListObserver attractionListObserver = AttractionListObserver.getInstance();
        observable.addObserver(attractionListObserver);
        observable.updateList();
        this.handler = handler;
    }
}
