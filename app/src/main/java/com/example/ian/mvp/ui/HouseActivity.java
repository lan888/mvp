package com.example.ian.mvp.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.HouseRvAdapter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Ian on 2018/1/5 0005.
 */

public class HouseActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private HouseRvAdapter mAdapter;
    private ArrayList<String> mHouseNameData;
    private ArrayList<String> mHouseDetailData;
    private ArrayList<String> mHousePriceData;
    private ArrayList<String> mHouseImgData;
    private ArrayList<String> mHouseStatusData;
    private List<Rooms> roomsList;
    private int lastVisibleItem;
    LinearLayoutManager manager;
    SwipeRefreshLayout srl;
    private Handler handler;

    @Override
    public void initControl() {
        setContentView(R.layout.house_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(HouseActivity.this,LandlordActivity.class);
            }
        });
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void initView() {
         mHouseNameData=new ArrayList<>();
         mHouseDetailData=new ArrayList<>();
         mHousePriceData=new ArrayList<>();
         mHouseImgData = new ArrayList<>();
         mHouseStatusData = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(manager);
         srl = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mAdapter = new HouseRvAdapter(HouseActivity.this,mHouseNameData,mHouseDetailData,mHousePriceData,mHouseImgData,mHouseStatusData,roomsList);
                mRecyclerView.setAdapter(mAdapter);

            }
        };




    }

    @Override
    public void initData() {
        String user = BmobUser.getCurrentUser(MyUser.class).getUsername();
        BmobQuery<Rooms> query = new BmobQuery<Rooms>();
        query.addWhereEqualTo("user",user);
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
                        roomsList = list;
                    }
                       Utils.putIntValue(HouseActivity.this,"a",mHouseNameData.size());
                       handler.sendEmptyMessage(0);

                    Log.i("bmob","成功："+mHouseNameData.size());
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
                        mHouseNameData.clear();
                        mHouseDetailData.clear();
                        mHousePriceData.clear();
                        mHouseStatusData.clear();
                        mHouseImgData.clear();
                        initData();
                        mAdapter.notifyDataSetChanged();
                        Log.i("datas","成功："+mHouseNameData.size());
                        srl.setRefreshing(false);

                    }
                }, 2000);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//               if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == mAdapter.getItemCount()) {
//
//               }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            mAdapter.notifyDataSetChanged();

                    }
                }, 1500);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        setTitle("房源列表");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
               start_Activity(HouseActivity.this,AddHouseActivity.class);
                break;

            case R.id.settings:

                break;
            default:
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
