package com.example.ian.mvp.mvp.view;

import com.example.ian.mvp.mvp.model.MyUser;

/**
 * Created by Ian on 2018/1/3 0003.
 */

public interface LoginActivityView {
    void loginSuccess(MyUser user,String str);
    void loginFailed(String str);
    void regSuccess(String str);
    void regFailed(String str);

}
