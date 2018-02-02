package com.example.ian.mvp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.MyFragmentPagerAdapter;
import com.example.ian.mvp.adapter.SpinnerAdapter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.behavior.AppBarLayoutOverScrollViewBehavior;
import com.example.ian.mvp.mvp.model.Bill;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.TabEntity;
import com.example.ian.mvp.ui.Fragment.ItemFragment4;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.CircleImageView;
import com.example.ian.mvp.widget.NoScrollViewPager;
import com.example.ian.mvp.widget.RoundProgressBar;
import com.example.mylibrary.CommonTabLayout;
import com.example.mylibrary.listener.CustomTabEntity;
import com.example.mylibrary.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Ian on 2018/1/10 0010.
 */

public class TenantActivity extends BaseActivity {
    private ImageView mZoomIv;
    private Toolbar mToolBar;
    private ViewGroup titleContainer;
    private AppBarLayout mAppBarLayout;
    private ViewGroup titleCenterLayout;
    private RoundProgressBar progressBar;
    private ImageView mSettingIv, mMsgIv,mNavIv,mExit;
    private TextView mMsgTv,nUser,mMcTv;
    private CircleImageView mAvater,mImg;
    private CommonTabLayout mTablayout;
    private NoScrollViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNav;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<String> data = new LinkedList<>();
    private List<Fragment> fragments;
    private int lastState = 1;
    private String billId ;
    LoadingDialog ld;
    MyUser user = MyUser.getCurrentUser(MyUser.class);
    int d;
    private Handler mHandler;
    private long exitTime;
    Bill b1= new Bill() ;
    @Override
    public void initControl() {
        setContentView(R.layout.activity_demo2);

    }

    @Override
    public void initView() {
        mZoomIv =  findViewById(R.id.uc_zoomiv);
        mToolBar =  findViewById(R.id.toolbar);
        titleContainer =  findViewById(R.id.title_layout);
        mAppBarLayout =  findViewById(R.id.appbar_layout);
        titleCenterLayout =  findViewById(R.id.title_center_layout);
        progressBar =  findViewById(R.id.uc_progressbar);
        mMsgTv = findViewById(R.id.frag_uc_follow_tv);
        mMcTv = findViewById(R.id.frag_uc_msg_tv);
        mSettingIv =  findViewById(R.id.uc_setting_iv);
        mMsgIv =  findViewById(R.id.uc_msg_iv);
        mAvater =  findViewById(R.id.uc_avater);
        mTablayout =  findViewById(R.id.uc_tablayout);
        mViewPager =  findViewById(R.id.uc_viewpager);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mNav =  findViewById(R.id.nav_view);
        mNavIv= findViewById(R.id.uc_nav);
        ld = new LoadingDialog(TenantActivity.this);
        ld.show();
        ld.setLoadingText("载入中");

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        fragments = getFragments();
                        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments, getNames());
                        mViewPager.setAdapter(myFragmentPagerAdapter);
                        mTablayout.setTabData(mTabEntities);
                        ld.close();
                        break;
                }


            }
        };
        mToolBar.setNavigationIcon(R.mipmap.ic_menu);
        final View headerView = mNav.getHeaderView(0);
        nUser = headerView.findViewById(R.id.nav_user);
        mImg = headerView.findViewById(R.id.nav_civ);
        mExit = headerView.findViewById(R.id.nav_exit);

        initStatus();
        mMcTv.setText("我要租房");
        mMsgTv.setText("交房租");




        if (user == null){
            Toast.makeText(this,"请重新登陆",Toast.LENGTH_SHORT).show();
            Utils.start_Activity(this,Login2Activity.class);
        }else {
            String s2 = user.getUsername();
            nUser.setText(s2);
            BmobFile file = user.getImage();
            if (file != null) {
                String url = file.getFileUrl();

                if (url != null) {
                    mAvater.setTag(null);
                    Glide.with(this).load(url).error(R.mipmap.ic_launcher_round).into(mAvater);
                    mAvater.setTag(url);
                    mZoomIv.setTag(null);
                    Glide.with(this).load(url).into(mZoomIv);
                    Glide.with(this).load(url).into(mImg);
                }

            } else {
                mAvater.setImageResource(R.drawable.ic_avater);
                mZoomIv.setImageResource(R.drawable.ic_avater);
                mImg.setImageResource(R.drawable.ic_avater);
            }
        }
    }

    @Override
    public void initData() {
        final ArrayList<String> mData4 = new ArrayList<String>();
        if (user==null){

        }else {
            BmobQuery<Bill> query = new BmobQuery<Bill>();
            query.addWhereEqualTo("tenant", user.getUsername());
            query.findObjects(new FindListener<Bill>() {
                @Override
                public void done(List<Bill> list, BmobException e) {
                    if (e == null) {
                        for (Bill r : list) {
                            String s = r.getRoom();
                            mData4.add(s);
                        }
                        d = mData4.size();
                        Log.i("d1", "d:" + d);
                        mHandler.sendEmptyMessage(1);


                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
            BmobQuery<Bill> query1 = new BmobQuery<Bill>();
            query1.addWhereEqualTo("tenant", user.getUsername());
            query1.addWhereEqualTo("status","未缴费");
            query1.findObjects(new FindListener<Bill>() {
                @Override
                public void done(List<Bill> list, BmobException e) {
                    if (e == null) {
                        for (Bill r : list) {
                            data.add(r.getRoom()+"\n"+r.getMonth());
                        }

                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    @Override
    public void setListener() {
        mMcTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(TenantActivity.this,RentHouseActivity.class);
            }
        });
        mMsgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.size()==0){
                    Utils.showShortToast(TenantActivity.this,"还没有需要交款的账单");
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TenantActivity.this);
                    LayoutInflater factory = LayoutInflater.from(TenantActivity.this);
                    final View textEntryView = factory.inflate(R.layout.pay,null);
                    builder.setTitle("交款");
                    builder.setView(textEntryView);

                    final Spinner address = textEntryView.findViewById(R.id.nice_spinner);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(TenantActivity.this,data);
                    address.setAdapter(spinnerAdapter);


                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final String billInfo = data.get(address.getSelectedItemPosition());
                            String bill[] = billInfo.split("\n");
                            Log.i("addressInfo",billInfo);
                            BmobQuery<Bill> query = new BmobQuery<Bill>();
                            query.addWhereEqualTo("room",bill[0]);
                            query.addWhereEqualTo("month",bill[1]);
                            query.findObjects(new FindListener<Bill>(){
                                @Override
                                public void done(List<Bill> list, BmobException e) {
                                    if (e == null) {
                                        for( Bill b : list) {
                                            billId = b.getObjectId();

                                        }
                                                b1.setStatus("已缴费");
                                                b1.update(billId,new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            Log.e("success", "更新成功:" + b1.getUpdatedAt());
                                                            Utils.showShortToast(TenantActivity.this,"缴费成功");
                                                            Utils.start_Activity(TenantActivity.this,RentBillsActivity.class);
                                                        } else {
                                                            Log.e("succes", "更新失败：" + e.getMessage());
                                                        }
                                                    }
                                                });


                                        Log.i("bmob","成功："+data.size());
                                    }else {
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }

                            });


                        }

                    });
                    builder.create().show();
                }
            }
        });
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser.logOut();
                finish();
                Utils.start_Activity(TenantActivity.this,Login2Activity.class);
                Toast.makeText(TenantActivity.this,"已成功退出",Toast.LENGTH_SHORT).show();

            }
        });

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent info = new Intent(context, RentHouseActivity.class);
                        info.putExtra("user",user.getUsername());
                        context.startActivity(info);

                        break;

                    case R.id.nav_bills:
                        Utils.start_Activity(TenantActivity.this,RentBillsActivity.class);

                        break;

                    case R.id.change:
                        Utils.putIntValue(TenantActivity.this,"is_landlord",1);
                        Utils.start_Activity(TenantActivity.this,LandlordActivity.class);
                        break;

                }


                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (titleCenterLayout != null && mAvater != null && mSettingIv != null && mMsgIv != null) {
                    titleCenterLayout.setAlpha(percent);
                    StatusBarUtil.setTranslucentForImageView(TenantActivity.this, (int) (255f * percent), null);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        if (mAvater.getVisibility() != View.GONE) {
                            mAvater.setVisibility(View.GONE);
                        }
                        groupChange(1f, 2);
                    } else {
                        if (mAvater.getVisibility() != View.VISIBLE) {
                            mAvater.setVisibility(View.VISIBLE);
                        }
                        groupChange(percent, 0);
                    }

                }
            }
        });
        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener(new AppBarLayoutOverScrollViewBehavior.onProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
                progressBar.setProgress((int)(progress*360));
                if (progress == 1 && !progressBar.isSpinning && isRelease){

                }
                if (mMsgIv != null){
                    if (progress == 0 && !progressBar.isSpinning){
                        mMsgIv.setVisibility(View.VISIBLE);
                    }else if (progress>0 && mSettingIv.getVisibility()== View.VISIBLE){
                        mMsgIv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {


            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以下不支持状态栏变色
            //注意了，这里使用了第三方库 StatusBarUtil，目的是改变状态栏的alpha
            StatusBarUtil.setTransparentForImageView(TenantActivity.this, null);
            //这里是重设我们的title布局的topMargin，StatusBarUtil提供了重设的方法，但是我们这里有两个布局
            //TODO 关于为什么不把Toolbar和@layout/layout_uc_head_title放到一起，是因为需要Toolbar来占位，防止AppBarLayout折叠时将title顶出视野范围
            int statusBarHeight = getStatusBarHeight(TenantActivity.this);
            CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) titleContainer.getLayoutParams();
            lp1.topMargin = statusBarHeight;
            titleContainer.setLayoutParams(lp1);
            CollapsingToolbarLayout.LayoutParams lp2 = (CollapsingToolbarLayout.LayoutParams) mToolBar.getLayoutParams();
            lp2.topMargin = statusBarHeight;
            mToolBar.setLayoutParams(lp2);
        }
    }

    /**
     * @param alpha
     * @param state 0-正在变化 1展开 2 关闭
     */
    public void groupChange(float alpha, int state) {
        lastState = state;

        mSettingIv.setAlpha(alpha);
        mMsgIv.setAlpha(alpha);

        switch (state) {
            case 1://完全展开 显示白色
                mMsgIv.setImageResource(R.drawable.icon_msg);
                mSettingIv.setImageResource(R.drawable.icon_setting);
                mViewPager.setNoScroll(false);
                break;
            case 2://完全关闭 显示黑色
                mMsgIv.setImageResource(R.drawable.icon_msg_black);
                mSettingIv.setImageResource(R.drawable.icon_setting_black);
                mViewPager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                if (lastState != 0) {
                    mMsgIv.setImageResource(R.drawable.icon_msg_black);
                    mSettingIv.setImageResource(R.drawable.icon_setting_black);
                }
                mViewPager.setNoScroll(true);
                break;
        }
    }


    /**
     * 获取状态栏高度
     * ！！这个方法来自StatusBarUtil,因为作者将之设为private，所以直接copy出来
     *
     * @param context context
     * @return 状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public String[] getNames() {
        String[] mName1 = new String[]{"账单"};
     //   d = Utils.getIntValue(TenantActivity.this,"d");

        for (String str : mName1) {
            mTabEntities.add(new TabEntity(String.valueOf(d), str));
            Log.i("d2", "d:" + d);
        }
        return mName1;
    }


    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ItemFragment4());
        return fragments;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //按返回键不退出程序，仅仅返回桌面
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
