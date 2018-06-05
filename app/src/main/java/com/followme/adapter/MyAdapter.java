package com.followme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.followme.lusir.followmeandroid.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mDataSet;

    //构造器，接受数据集
    public MyAdapter(List<String> data){
        mDataSet = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attraction_item_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //将数据填充到具体的view中
        holder.mTextView.setText(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            mTextView = (TextView) itemView.findViewById(R.id.attraction_name);
        }
    }
}
