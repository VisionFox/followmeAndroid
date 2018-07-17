package com.followme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.followme.adapter.MyUserPlanItemListAdapter;
import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.lusir.followmeandroid.R;
import com.followme.observer.attractionList.AttractionListObserver;
import com.followme.util.JsonTransform;
import com.followme.util.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class Fragment_notification extends Fragment implements View.OnClickListener {
    private Button goButton;
    private Button delAllButton;
    private TextView mTextView;
    private List<Attraction> attractionList;
    private List<UserPlan> userPlanList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case Const.handlerFlag.GET_ATTRACTION_LIST:
                    attractionList.clear();
                    attractionList.addAll((List<Attraction>) msg.obj);
                    changeUserPlanItem();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_plan, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        goButton = getActivity().findViewById(R.id.user_plan_GO_button_new);
        delAllButton = getActivity().findViewById(R.id.user_plan_del_all_button_new);
        mTextView = getActivity().findViewById(R.id.user_plan_textview_new);

        goButton.setOnClickListener(this);
        delAllButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);

        initUserPlanItemRecyclerView();
    }


    //更新attractionList
    public void updateAttractionList() {
        if (attractionList == null) {
            attractionList = new ArrayList<>();
        }
        //先从本手机的sqlite里获取用户的planlist信息
        updateUserPlanList();
        //异步从服务器获取景点列表信息，结果传回handler
        AttractionListObserver attractionListObserver = AttractionListObserver.getInstance();
        attractionListObserver.getUserPlanAttractionList(mHandler);
    }


    private void updateUserPlanList() {
        if (userPlanList == null) {
            userPlanList = new ArrayList<>();
        }
        userPlanList.clear();
        userPlanList.addAll(
                DataSupport
                        .where("uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()))
                        .find(UserPlan.class)
        );
    }

    //初始化recycleview及其相关配置
    private void initUserPlanItemRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView = getActivity().findViewById(R.id.user_plan_detail_RecycleView_new);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new MyUserPlanItemListAdapter(userPlanList, attractionList);
        recyclerView.setAdapter(adapter);
    }

    private void changeUserPlanItem() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_plan_GO_button_new:
                funtionGO();
                break;
            case R.id.user_plan_del_all_button_new:
                delAllPlan();
                break;
        }
    }

    //开始绘制路线规划的activyty
    private void funtionGO() {
        ToastUtil.show(getActivity(), "开始规划路线！");
        Intent routeIntent = new Intent(MyApplication.getContext(), TestRouteActivity.class);
        routeIntent.putExtra("attractionList", JsonTransform.attractionListToJson(attractionList));
        startActivity(routeIntent);
    }

    //清空用户的旅游路线计划
    private void delAllPlan() {
        DataSupport.deleteAll(UserPlan.class, "uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()));
        userPlanList.clear();
        attractionList.clear();
        changeUserPlanItem();
        ToastUtil.show(getActivity(), "全部清空");
    }
}