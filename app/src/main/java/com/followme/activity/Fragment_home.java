package com.followme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.followme.lusir.followmeandroid.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_home extends Fragment {

    private Spinner mSpinner;
    private List<String> dataList;
    private ArrayAdapter<String> arr_adapter;
    private TextView mTextView;

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
        mTextView = getView().findViewById(R.id.fragment_home_textView);

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

        /*为spinner定义适配器，也就是将数据源存入adapter，这里需要三个参数
        1. 第一个是Context（当前上下文），这里就是this
        2. 第二个是spinner的布局样式，这里用android系统提供的一个样式
        3. 第三个就是spinner的数据源，这里就是dataList*/
        arr_adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        mSpinner.setAdapter(arr_adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mTextView.setText("选择为："+adapterView.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}