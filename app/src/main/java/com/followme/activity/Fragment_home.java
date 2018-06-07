package com.followme.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;

import com.followme.adapter.MyAdapter;
import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.exchange.AttractionModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;

import java.util.ArrayList;
import java.util.List;


public class Fragment_home extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final int flag_attraction_list_info = Const.handlerFlag.ATTRACTION_LIST_INFO;
    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;

    private Spinner mSpinner;
    private List<String> dataList;
    private ArrayAdapter<String> arr_adapter;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<String> mData;
//    private Handler mHandler;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case flag_error:
                    Log.d(TAG, "收到错误异常信息为: " + msg.obj);
                    break;
                case flag_success:
                    Log.d(TAG, "收到的信息" + msg.obj);
                    List<Attraction> attractionList = JsonTransform.ServerResponseToAttractionList((String) msg.obj);
                    Log.d("转换得出的景点：", attractionList.toString());
                    showAttraction(attractionList);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        new Thread(new MyRunnable()).start();


        mSpinner = getActivity().findViewById(R.id.fragment_spinner);
        mTextView = getActivity().findViewById(R.id.fragment_home_textView);
        mRecyclerView = getActivity().findViewById(R.id.fragment_home_recyclerview);


        initData();
        initDropDownList();
        initRecyclerView();
    }


    private void initData() {
        dataList = new ArrayList<String>();
        dataList.add("未选择区域，请下拉选择");
        dataList.add("蓝田县");
        dataList.add("长安区");
        dataList.add("碑林区");
        dataList.add("灞桥区");
        dataList.add("未央区");
        dataList.add("莲湖区");
        dataList.add("临潼区");
        dataList.add("终南山");
        dataList.add("户县");
        dataList.add("周至县");
        dataList.add("雁塔区");
        dataList.add("高陵区");
        dataList.add("阎良区");
        dataList.add("西安世博园");
        dataList.add("曲江旅游度假区");
        dataList.add("终南山古楼观");
        dataList.add("兴庆宫公园");
        dataList.add("秦始皇陵博物院（兵马俑）");
    }

    private void initDropDownList() {
        //为spinner定义适配器，也就是将数据源存入adapter.
        arr_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, dataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //为spinner绑定我们定义好的数据适配器
        mSpinner.setAdapter(arr_adapter);
        //绑定点击事件
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String area = (String) adapterView.getItemAtPosition(position);
                mTextView.setText("选择为：" + area);
                if (!dataList.get(0).equals(area)){
                    AttractionModuleRequest.get_attractions_info_by_area(area, mHandler);
                }else {
                    mData.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setLayoutManager(new VegaLayoutManager());
        mData = new ArrayList<>();
        mAdapter = new MyAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showAttraction(List<Attraction> attractionList) {
        mData.clear();
        for (Attraction attraction : attractionList) {
            mData.add(attraction.getName());
        }
        mAdapter.notifyDataSetChanged();
    }


//    private class MyRunnable implements Runnable {
//        @Override
//        public void run() {
//            //建立消息循环的步骤
//            Looper.prepare();//1、初始化Looper
//            mHandler = new Handler() {//2、绑定handler到CustomThread实例的Looper对象
//                public void handleMessage(Message msg) {//3、定义处理消息的方法
//                    switch (msg.what) {
//                        case flag_error:
//                            Log.d(TAG, "收到错误异常信息为: " + msg.obj);
//                            break;
//                        case flag_success:
//                            Log.d(TAG, "收到的信息" + msg.obj);
//                            List<Attraction> attractionList=JsonTransform.ServerResponseToAttractionList((String) msg.obj);
//                            Log.d("转换得出的景点：",attractionList.toString());
//                            break;
//                    }
//                }
//            };
//            Looper.loop();//4、启动消息循环
//        }
//    }
}