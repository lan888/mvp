package com.example.ian.mvp;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by Ian on 2018/1/2 0002.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
    }
    public void initBmob(){
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("26a7be21ef92ba03984d461048b40981")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(1800)
                .build();
        Bmob.initialize(config);
    }
}
