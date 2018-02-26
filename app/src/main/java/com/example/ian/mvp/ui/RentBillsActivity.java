package com.example.ian.mvp.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.Section2Adapter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.Bill;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.StickyHeaderListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Ian on 2018/1/13 0013.
 */

public class RentBillsActivity extends BaseActivity {

//    private ItemFragment4 itemFragment4;
        String user ;
        List<Bill> billList;
        private StickyHeaderListView mListView;
        private Section2Adapter mAdapter;
        private boolean isOnLoadMore = false;
        private int count = 0;
        private Handler mHandler;


//        itemFragment4 = new ItemFragment4();
//        if (savedInstanceState!=null){
//            itemFragment4 = (ItemFragment4)getSupportFragmentManager().getFragment(savedInstanceState,"itemFragment4");
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.main,itemFragment4).commit();

    @Override
    public void initControl() {
        setContentView(R.layout.rent_bills_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的账单");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(RentBillsActivity.this,TenantActivity.class);
            }
        });
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        if (myUser!=null) {
            user = myUser.getUsername();
        }
    }

    @Override
    public void initView() {
        mListView = findViewById(R.id.r_lv);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mListView.setAdapter(mAdapter = new Section2Adapter(RentBillsActivity.this, billList));
            }
        };


    }

    @Override
    public void initData() {
        BmobQuery<Bill> query = new BmobQuery<Bill>();
        query.addWhereEqualTo("tenant",user);
        query.findObjects(new FindListener<Bill>(){
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null) {
                    billList = list;
                    Log.i("list!!!","list:"+billList);
                    mHandler.sendEmptyMessage(0);
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    @Override
    public void setListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                            if (!isOnLoadMore) {

                                if(count == 0){
                                    Toast.makeText(RentBillsActivity.this, "没有更多数据啦~", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                count++;

                                isOnLoadMore = true;

                                isOnLoadMore = false;
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState,"itemFragment4",itemFragment4);
//    }


}
