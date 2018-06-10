package com.followme.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.followme.lusir.followmeandroid.R;

public class Fragment_dashboard extends Fragment {
    private MapView mMapView = null;
    private AMap mAMap = null;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView = getActivity().findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);


        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
    }





//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        mMapView.onPause();
//    }
//
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}