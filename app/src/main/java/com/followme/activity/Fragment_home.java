package com.followme.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

import com.followme.adapter.MyAttractionListAdapter;
import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.exchange.AttractionModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class Fragment_home extends Fragment implements RecyclerView.RecyclerListener {

    private static final String TAG = "HomeFragment";
    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private Spinner mSpinner;
    private List<String> areaList;
    private List<Attraction> attractionList;
    private ArrayAdapter<String> arr_adapter;

    private RecyclerView mRecyclerView;
    private MyAttractionListAdapter mAdapter;


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

        mSpinner = getActivity().findViewById(R.id.fragment_spinner);
        mRecyclerView = getActivity().findViewById(R.id.fragment_home_recyclerview);
        mRecyclerView.setRecyclerListener(this);

        initData();
        initDropDownList();
        initRecyclerView();
    }


    private void initData() {
        areaList = new ArrayList<String>();
        areaList.add("未选择区域，请点击此处进行选择");
        areaList.add("蓝田县");
        areaList.add("长安区");
        areaList.add("碑林区");
        areaList.add("灞桥区");
        areaList.add("未央区");
        areaList.add("莲湖区");
        areaList.add("临潼区");
        areaList.add("终南山");
        areaList.add("户县");
        areaList.add("周至县");
        areaList.add("雁塔区");
        areaList.add("高陵区");
        areaList.add("阎良区");
        areaList.add("西安世博园");
        areaList.add("曲江旅游度假区");
        areaList.add("终南山古楼观");
        areaList.add("兴庆宫公园");
        areaList.add("秦始皇陵博物院（兵马俑）");
    }

    private void initDropDownList() {
        //为spinner定义适配器，也就是将数据源存入adapter.
        arr_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, areaList);
        //为适配器设置下拉列表下拉时的菜单样式。
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //为spinner绑定我们定义好的数据适配器
        mSpinner.setAdapter(arr_adapter);
        //绑定点击事件
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String area = (String) adapterView.getItemAtPosition(position);

                if (!areaList.get(0).equals(area)) {
                    AttractionModuleRequest.get_attractions_info_by_area(area, mHandler);
                } else {
                    attractionList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initRecyclerView() {
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new VegaLayoutManager());
        attractionList = new ArrayList<>();
        mAdapter = new MyAttractionListAdapter(attractionList);
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setRecyclerListener(this);/////////////////////////////////////////////////////////////////////////////////
    }

    private void showAttraction(List<Attraction> attractionList) {
        this.attractionList.clear();
        this.attractionList.addAll(attractionList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }
}