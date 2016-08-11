package com.chong.marqueeview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chong.marqueeview.view.MarqueeView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private List<Model> mData;

    public MyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getTitle());
        // 开始轮播，为了设置已经轮播的标记
        holder.marqueeView.startWithList(mData.get(position).getContents());
        holder.marqueeView.stop();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        MarqueeView marqueeView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            marqueeView = (MarqueeView) itemView.findViewById(R.id.mv_content);
        }
    }

    public void setData(List<Model> data) {
        this.mData = data;
    }

}
