package com.followme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.followme.adapter.MyUserPlanItemListAdapter;
import com.followme.bean.Attraction;
import com.followme.common.MyApplication;
import com.followme.lusir.followmeandroid.R;

import java.util.ArrayList;
import java.util.List;


public class UserPlanDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyUserPlanItemListAdapter mAdapter;
    private List<Attraction> attractionList;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plan_item_list);
        initUserPlanItemList();
    }

    private void initUserPlanItemList() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        mRecyclerView=findViewById(R.id.user_plan_detail_RecycleView);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setLayoutManager(new VegaLayoutManager());
        mAdapter = new MyUserPlanItemListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
