package com.example.ian.mvp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.example.ian.mvp.R;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.utils.Utils;

/**
 * Created by Ian on 2018/1/4 0004.
 */

public class StartActivity extends BaseActivity {

    private Handler handler = new Handler();
//    int a;
//    int b;
//    int c;
//    int d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：此处将setContentView()方法注释掉
         setContentView(R.layout.activity_start);




        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoLogin();
                // 判断是否是第一次开启应用
                Utils.putBoolean(StartActivity.this, Utils.FIRST_OPEN, false);
            }
        }, 2000);
    }

    /**
     * 前往注册、登录主页
     */
    private void gotoLogin() {
        boolean isFirstOpen = Utils.getBoolean(this, Utils.FIRST_OPEN, true);
        int a = Utils.getIntValue(this,"is_landlord");
       if (isFirstOpen){
           Utils.start_Activity(this,LoginActivity.class);
           finish();
           //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
           overridePendingTransition(0, 0);
       }else if (a==1){
           Utils.start_Activity(this, LandlordActivity.class);
           finish();
       }else {
           Utils.start_Activity(this, TenantActivity.class);
           finish();
       }

    }

    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initControl() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
//        if (myUser==null){
//
//        }else {
//            final String user = myUser.getUsername();
//            final ArrayList<String> mData1 = new ArrayList<String>();
//            final ArrayList<String> mData2 = new ArrayList<String>();
//            final ArrayList<String> mData3 = new ArrayList<String>();
//            final ArrayList<String> mData4 = new ArrayList<String>();
//            BmobQuery<Rooms> query = new BmobQuery<Rooms>();
//            query.addWhereEqualTo("user",user);
//            query.findObjects(new FindListener<Rooms>() {
//                @Override
//                public void done(List<Rooms> list, BmobException e) {
//                    if (e == null) {
//
//                        for (Rooms r : list) {
//                            String s = r.getAddressInfo();
//                            mData1.add(s);
//                        }
//                        Log.i("a", "a:" + mData1.size());
//                        a = mData1.size();
//
//                        BmobQuery<Renters> query = new BmobQuery<Renters>();
//                        query.addWhereEqualTo("landlord", user);
//                        query.findObjects(new FindListener<Renters>() {
//                            @Override
//                            public void done(List<Renters> list, BmobException e) {
//                                if (e == null) {
//                                    for (Renters r : list) {
//                                        String s = r.getName();
//                                        mData2.add(s);
//                                    }
//                                    Log.i("b", "b:" + mData2.size());
//                                    b = mData2.size();
//                                    BmobQuery<Bill> query = new BmobQuery<Bill>();
//                                    query.addWhereEqualTo("user", user);
//                                    query.findObjects(new FindListener<Bill>() {
//                                        @Override
//                                        public void done(List<Bill> list, BmobException e) {
//                                            if (e == null) {
//                                                for (Bill r : list) {
//                                                    String s = r.getRoom();
//                                                    mData3.add(s);
//                                                }
//                                                Log.i("c", "c:" + mData3.size());
//                                                c = mData3.size();
//                                                BmobQuery<Bill> query = new BmobQuery<Bill>();
//                                                query.addWhereEqualTo("tenant", user);
//                                                query.findObjects(new FindListener<Bill>() {
//                                                    @Override
//                                                    public void done(List<Bill> list, BmobException e) {
//                                                        if (e == null) {
//                                                            for (Bill r : list) {
//                                                                String s = r.getRoom();
//                                                                mData4.add(s);
//                                                            }
//                                                            d = mData4.size();
//                                                            Log.i("d", "d:" + mData4.size()+user);
//                                                            Utils.putIntValue(StartActivity.this, "a", a);
//                                                            Utils.putIntValue(StartActivity.this, "b", b);
//                                                            Utils.putIntValue(StartActivity.this, "c", c);
//                                                            Utils.putIntValue(StartActivity.this, "d", d);
//
//                                                        } else {
//                                                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                                                        }
//                                                    }
//                                                });
//
//                                            } else {
//                                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                                            }
//                                        }
//                                    });
//                                } else {
//                                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        }

    }


    @Override
    public void setListener() {

    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }


}
