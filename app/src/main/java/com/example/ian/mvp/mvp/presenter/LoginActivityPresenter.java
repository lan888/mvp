package com.example.ian.mvp.mvp.presenter;

import android.content.Context;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Ian on 2018/1/3 0003.
 */

public interface LoginActivityPresenter {
    void login(String username, String password);
    void register(String username, String password, String mail, String mobilePhoneNum, BmobFile uri);
    void requestSMSCode(String mobile,String template,Context context);
}
