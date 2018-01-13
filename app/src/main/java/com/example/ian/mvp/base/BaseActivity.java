package com.example.ian.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.ian.mvp.utils.Utils;

import cn.bmob.v3.Bmob;

/**
 * Created by Ian on 2018/1/2 0002.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Bmob.initialize(this, "26a7be21ef92ba03984d461048b40981");
        initControl();
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    /**
     * 绑定控件id
     */
    public abstract void initControl();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 设置监听
     */
    public abstract void setListener();


    /**
     * 打开 Activity
     *
     * @param activity
     * @param cls
     */
    public void start_Activity(Activity activity, Class<?> cls
                               ) {
        Utils.start_Activity(activity, cls);
    }

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        Utils.finish(activity);
    }

    /**
     * 判断是否有网络连接
     */
    public boolean isNetworkAvailable(Context context) {
        return Utils.isNetworkAvailable(context);
    }


}
