package com.followme.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.followme.bean.Attraction;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.exchange.AttractionModuleRequest;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyUserPlanItemListAdapter extends RecyclerView.Adapter<MyUserPlanItemListAdapter.ViewHolder> {
    private Context mContext;
    private List<Attraction> attractionList;
    private List<UserPlan> userPlanList;
    private MyUserPlanItemListAdapter thisAdapter = this;


    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private static final String TAG = "MyUserPlanItemListAdapter";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case flag_error:
                    Log.d(TAG, "收到错误异常信息为: " + msg.obj);
                    break;
                case flag_success:
                    Log.d(TAG, "收到的信息" + msg.obj);
                    Attraction attraction = JsonTransform.ServerResponseToAttraction((String) msg.obj);
                    attractionList.add(attraction);
                    Log.d("转换得到的景点信息为:", attraction.toString());
                    thisAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    //构造器，接受数据集
    public MyUserPlanItemListAdapter() {
        this.mContext = MyApplication.getContext();
        this.attractionList = new ArrayList<>();
        userPlanList = new ArrayList<>();
        updateList();
    }

    private void updateList() {
        attractionList.clear();
        userPlanList.clear();
        
        thisAdapter.notifyDataSetChanged();

        userPlanList.addAll(
                DataSupport
                        .where("uid = ?", String.valueOf(MyApplication.getCurrentUser().getId()))
                        .find(UserPlan.class)
        );

        if (userPlanList.size() > 0) {
            for (UserPlan t : userPlanList) {
                AttractionModuleRequest.get_attractions_info_by_attraction_id(t.getAttractionId(), mHandler);
            }
        }

    }

    @Override
    public MyUserPlanItemListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_plan_item, parent, false);

        final MyUserPlanItemListAdapter.ViewHolder holder = new MyUserPlanItemListAdapter.ViewHolder(v);
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Attraction attraction = attractionList.get(position);
                DataSupport.deleteAll(UserPlan.class, "uid = ? and attractionId = ?",
                        String.valueOf(MyApplication.getCurrentUser().getId()),
                        String.valueOf(attraction.getAttractionid()));
                updateList();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyUserPlanItemListAdapter.ViewHolder holder, int position) {
        //将数据填充到具体的view中
        holder.addrTextView.setText(attractionList.get(position).getAddr());
        holder.nameTextView.setText(attractionList.get(position).getName());

        //图片
        String imageURL = attractionList.get(position).getImageurl();
        Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_localtion).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;
        public TextView addrTextView;
        public Button delButton;
        public View userPlanItemAttrView;


        public ViewHolder(View itemView) {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            imageView = itemView.findViewById(R.id.user_plan_item_attraction_img);
            nameTextView = itemView.findViewById(R.id.user_plan_item_attraction_name);
            addrTextView = itemView.findViewById(R.id.user_plan_item_attraction_addr);
            delButton = itemView.findViewById(R.id.user_plan_item_del_button);
            userPlanItemAttrView = itemView;
        }
    }
}
