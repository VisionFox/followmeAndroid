package com.followme.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.lusir.followmeandroid.R;
import com.followme.observer.attractionList.AttractionListObserver;
import com.followme.util.JsonTransform;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class Fragment_notification extends Fragment implements View.OnClickListener {
    private Button goButton;
    private Button delAllButton;
    private TextView mTextView;

    private List<Attraction> attractionList=new ArrayList<>();


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case Const.handlerFlag.GET_ATTRACTION_LIST:
                    attractionList.clear();
                    attractionList.addAll((List<Attraction>) msg.obj);
                    funtionGO();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        goButton = getActivity().findViewById(R.id.user_plan_GO_button);
        delAllButton = getActivity().findViewById(R.id.user_plan_del_all_button);
        mTextView = getActivity().findViewById(R.id.user_plan_textview);

        goButton.setOnClickListener(this);
        delAllButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_plan_GO_button:
                AttractionListObserver attractionListObserver = AttractionListObserver.getInstance();
                attractionListObserver.getUserPlanAttractionList(mHandler);
                break;
            case R.id.user_plan_del_all_button:
                delAllPlan();
                break;
            case R.id.user_plan_textview:
                Intent intent = new Intent(MyApplication.getContext(), UserPlanDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void funtionGO() {
        Intent routeIntent = new Intent(MyApplication.getContext(), TestRouteActivity.class);
        routeIntent.putExtra("attractionList", JsonTransform.attractionListToJson(attractionList));
        startActivity(routeIntent);
//        Intent intent = new Intent(MyApplication.getContext(), TestRouteActivity.class);
//        startActivity(intent);
    }

    private void delAllPlan() {
        DataSupport.deleteAll(UserPlan.class, "uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()));
        Log.d("删除完成", "删除完成");
    }

//    private void initPlanList() {
//        mRecyclerView = getActivity().findViewById(R.id.fragment_notification_recyclerview);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        userPlanList = new ArrayList<UserPlan>();
//        mAdapter = new MyUserPlanListAdapter(userPlanList);
//        mRecyclerView.setAdapter(mAdapter);
//    }
//
//    private void updateUserPlan() {
//        userPlanList.clear();
//        userPlanList.addAll(
//                DataSupport
//                        .where("uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()))
//                        .find(UserPlan.class));
//
//        Log.d("sasa","sadadsasadsa");
//
//        for (UserPlan t : userPlanList) {
//            Log.d("当前计划:", t.toString());
//        }
//        mAdapter.notifyDataSetChanged();
//    }

}