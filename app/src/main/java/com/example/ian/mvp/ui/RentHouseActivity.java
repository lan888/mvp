package com.example.ian.mvp.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.RentHouseRvAdapter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Ian on 2018/1/11 0011.
 */

public class RentHouseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private RentHouseRvAdapter mAdapter;
    private ArrayList<String> mHouseNameData;
    private ArrayList<String> mHouseDetailData;
    private ArrayList<String> mHousePriceData;
    private ArrayList<String> mHouseImgData;
    private ArrayList<String> mHouseStatusData;
    LinearLayoutManager manager;
    SwipeRefreshLayout srl;
    private Handler handler;
    @Override
    public void initControl() {
        setContentView(R.layout.house_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("租房列表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(RentHouseActivity.this,TenantActivity.class);
            }
        });

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void initView() {
        mHouseNameData = new ArrayList<>();
        mHouseDetailData = new ArrayList<>();
        mHousePriceData = new ArrayList<>();
        mHouseImgData = new ArrayList<>();
        mHouseStatusData = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(manager);
        srl = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mAdapter = new RentHouseRvAdapter(RentHouseActivity.this, mHouseNameData,
                        mHouseDetailData, mHousePriceData, mHouseImgData, mHouseStatusData);
                mRecyclerView.setAdapter(mAdapter);
            }
        };
    }

    @Override
    public void initData() {
        String user = getIntent().getStringExtra("user");
        if (user!=null){
            BmobQuery<Rooms> query = new BmobQuery<Rooms>();
            query.addWhereEqualTo("tenant",user);
            query.findObjects(new FindListener<Rooms>(){
                @Override
                public void done(List<Rooms> list, BmobException e) {
                    if (e == null) {
                        for (Rooms r : list) {
                            mHouseNameData.add(r.getAddressInfo()) ;
                            mHouseDetailData.add(r.getAddressDetail());
                            mHousePriceData.add(r.getPrice());
                            mHouseImgData.add(r.getHouseImg().get(0));
                            mHouseStatusData.add(r.getStatus());

                        }
                        Utils.putIntValue(RentHouseActivity.this,"rent",1);
                        handler.sendEmptyMessage(0);

                        Log.i("bmob","成功："+mHouseNameData.size());
                    }else {
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }else {
            String status = "未出租";
            BmobQuery<Rooms> query = new BmobQuery<Rooms>();
            query.addWhereEqualTo("status",status);
            query.findObjects(new FindListener<Rooms>(){
                @Override
                public void done(List<Rooms> list, BmobException e) {
                    if (e == null) {
                        for (Rooms r : list) {
                            mHouseNameData.add(r.getAddressInfo()) ;
                            mHouseDetailData.add(r.getAddressDetail());
                            mHousePriceData.add(r.getPrice());
                            mHouseImgData.add(r.getHouseImg().get(0));
                            mHouseStatusData.add(r.getStatus());
                        }
                        Utils.putIntValue(RentHouseActivity.this,"rent",2);
                        handler.sendEmptyMessage(0);

                        Log.i("bmob","成功："+mHouseNameData.size());
                    }else {
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }

    }

    @Override
    public void setListener() {
        srl.setColorSchemeColors(getResources().getColor(R.color.blue_text));
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHouseNameData.clear();
                        mHouseDetailData.clear();
                        mHousePriceData.clear();
                        mHouseStatusData.clear();
                        //  mHouseImgData.clear();
                        initData();
                        mAdapter.notifyDataSetChanged();
                        Log.i("datas","成功："+mHouseNameData.size());
                        srl.setRefreshing(false);

                    }
                }, 2000);
            }
        });
    }
}
