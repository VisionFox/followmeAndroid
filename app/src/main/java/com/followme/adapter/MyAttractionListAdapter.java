package com.followme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.followme.activity.AttractionDetailActivity;
import com.followme.bean.Attraction;
import com.followme.common.MyApplication;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAttractionListAdapter extends RecyclerView.Adapter<MyAttractionListAdapter.ViewHolder> {

    private List<Attraction> attractionList;
    private Context mContext;


    //构造器，接受数据集
    public MyAttractionListAdapter( List<Attraction> attractionList) {
        this.attractionList = attractionList;
        this.mContext = MyApplication.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_item_layout, parent, false);

        final ViewHolder holder = new ViewHolder(v);
        holder.attractionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Attraction attraction = attractionList.get(position);
                Intent detailIntent = new Intent(view.getContext(), AttractionDetailActivity.class);
                detailIntent.putExtra("attractionJson", JsonTransform.attractionToJson(attraction));
                mContext.startActivity(detailIntent);
            }
        });

        holder.mAttractionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Attraction attraction = attractionList.get(position);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //将数据填充到具体的view中
        holder.mAttractionNameTextView.setText(attractionList.get(position).getName());
        holder.mAttractionAddrTextView.setText(attractionList.get(position).getAddr());

        //图片
        String imageURL = attractionList.get(position).getImageurl();
        Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_localtion).into(holder.mAttractionImageView);
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public View attractionView;
        public ImageView mAttractionImageView;
        public TextView mAttractionNameTextView;
        public TextView mAttractionAddrTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            mAttractionImageView = itemView.findViewById(R.id.attraction_img);
            mAttractionNameTextView = (TextView) itemView.findViewById(R.id.attraction_name);
            mAttractionAddrTextView = (TextView) itemView.findViewById(R.id.attraction_addr);
            attractionView = itemView;
        }
    }
}
