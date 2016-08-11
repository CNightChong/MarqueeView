package com.chong.marqueeview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecycleViewActivity extends AppCompatActivity {
    private static final int WHAT = 1;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private MyAdapter mAdapter;
    private List<Model> mData = new ArrayList<>();
    private Random mRandom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        mRandom = new Random();
        for (int i = 0; i < 10; i++) {
            Model model = new Model();
            model.setTitle("title---" + i + i + i + i + i + i);
            int k = i + 2;
            if (k > 6) {
                k -= 5;
            }
            List<String> list = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                list.add("content----" + i + i + i + "---" + j + j + j + j);
            }
            model.setContents(list);
            mData.add(model);
        }
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
        Message message = mHandler.obtainMessage(WHAT);
        mHandler.sendMessageDelayed(message, 2000);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT) {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 第一个完全显示的item
                        int first = mManager.findFirstCompletelyVisibleItemPosition();
                        // 最后一个完全显示的item
                        int last = mManager.findLastCompletelyVisibleItemPosition();
                        // 随机数 [first,last]
                        int index = mRandom.nextInt(last + 1);
                        if (index < first) {
                            index = first;
                        }
                        // 获得每个完全显示的item
                        for (int i = first; i < last + 1; i++) {
                            View view = mRecyclerView.getChildAt(index);
                            if (null != mRecyclerView.getChildViewHolder(view)) {
                                // 获得item的ViewHolder
                                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) mRecyclerView.getChildViewHolder(view);
                                if (i == index) { // 如果是随机选中的item
                                    // marqueeView已经轮播过
                                    if (holder.marqueeView.isStarted()) {
                                        // 继续轮播
                                        holder.marqueeView.continueFlipping();
                                    } else {
                                        // 从0开始
                                        holder.marqueeView.start();
                                    }
                                } else {
                                    // 停止轮播
                                    holder.marqueeView.stop();
                                }

                            }
                        }

                        Message message = obtainMessage(WHAT);
                        sendMessageDelayed(message, 2000);
                    }
                });


            }
        }
    };


}
