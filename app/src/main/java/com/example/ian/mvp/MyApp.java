package com.example.ian.mvp;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by Ian on 2018/1/2 0002.
 */

public class MyApp extends Application {
    public static final String TAG = "MyApp";

    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
    }
    public void initBmob(){
        Bmob.initialize(this, "26a7be21ef92ba03984d461048b40981");
    }
}
