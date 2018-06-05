package com.followme.activity;

import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.followme.adapter.MyAdapter;
import com.followme.bean.Attraction;
import com.followme.common.ServerResponse;
import com.followme.exchange.AttractionModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;
import com.google.gson.Gson;
import com.stone.vega.library.VegaLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment_home extends Fragment {
    private Spinner mSpinner;
    private List<String> dataList;
    private ArrayAdapter<String> arr_adapter;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private List<String> mData;

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
        mTextView = getActivity().findViewById(R.id.fragment_home_textView);
        mRecyclerView = getActivity().findViewById(R.id.fragment_home_recyclerview);


        initData();
        initDropDownList();
        initRecyclerView();
    }

    private void initData() {
        dataList = new ArrayList<String>();
        dataList.add("全部");
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
                mTextView.setText("选择为：" + adapterView.getItemAtPosition(position));
                String area = (String) adapterView.getItemAtPosition(position);

                Log.d("aaaaaa", Thread.currentThread().getName());

                if (!"全部".equals(area)) {
                    AttractionModuleRequest.get_attractions_info_by_area(area, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("查询景点信息出现异常", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String jsonstr = response.body().string();
                            Log.d("查询景点信息json结果 ", jsonstr);
                            List<Attraction> res = JsonTransform.ServerResponseToAttractionList(jsonstr);
                            showRes(res);
                        }
                    });

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
        for (int i = 0; i < 20; i++) {
            mData.add("Item " + i);
        }
        mAdapter = new MyAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showRes(List<Attraction> res) {
        //todo 待完善 main和okhttp线程如何传递数据
    }
}