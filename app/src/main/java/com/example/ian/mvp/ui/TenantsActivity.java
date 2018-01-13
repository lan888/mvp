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
import com.example.ian.mvp.adapter.TenantsRvAdapter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Renters;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Ian on 2018/1/8 0008.
 */

public class TenantsActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private TenantsRvAdapter mAdapter;
    private ArrayList<String> mTenantNameData = new ArrayList<>();
    private ArrayList<String> mTenantGenderData= new ArrayList<>();
    private ArrayList<String> mTenantTelData= new ArrayList<>();
    private ArrayList<String> mTenantIdData= new ArrayList<>();
    private ArrayList<String> mTenantPhotoData= new ArrayList<>();
    LinearLayoutManager manager;
    SwipeRefreshLayout srl;
    private Handler handler;

    @Override
    public void initControl() {
        setContentView(R.layout.renter_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的租户");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.finish(TenantsActivity.this);
            }
        });
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mAdapter = new TenantsRvAdapter(TenantsActivity.this,mTenantNameData,mTenantGenderData,mTenantTelData,mTenantIdData,mTenantPhotoData);
                mRecyclerView.setAdapter(mAdapter);

            }
        };
    }


    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(manager);
        srl = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
    }

    @Override
    public void initData() {
        String user = BmobUser.getCurrentUser(MyUser.class).getUsername();
        BmobQuery<Renters> query = new BmobQuery<Renters>();
        query.addWhereEqualTo("landlord",user);
        query.findObjects(new FindListener<Renters>(){
            @Override
            public void done(List<Renters> list, BmobException e) {
                if (e == null) {
                    for (Renters r : list) {
                        mTenantNameData.add(r.getName());
                        mTenantGenderData.add(r.getSex());
                        mTenantIdData.add(r.getIdcard());
                        mTenantTelData.add(r.getPhone());
                        mTenantPhotoData.add(r.getPhoto());
                    }

                    handler.sendEmptyMessage(0);

                    Log.i("bmob","成功："+mTenantNameData.size());
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
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
                        mTenantNameData.clear();
                        mTenantTelData.clear();
                        mTenantGenderData.clear();
                        mTenantIdData.clear();
                        initData();
                        mAdapter.notifyDataSetChanged();
                        Log.i("datas","成功："+mTenantNameData.size());
                        srl.setRefreshing(false);

                    }
                }, 2000);
            }
        });
    }
}
