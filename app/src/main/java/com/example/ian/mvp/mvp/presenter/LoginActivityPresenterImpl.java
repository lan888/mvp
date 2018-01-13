package com.example.ian.mvp.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.view.LoginActivityView;
import com.example.ian.mvp.utils.Utils;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Ian on 2018/1/3 0003.
 */

public class LoginActivityPresenterImpl implements LoginActivityPresenter {
    private LoginActivityView loginActivityView;
    final MyUser user = new MyUser();
    public LoginActivityPresenterImpl(LoginActivityView loginView){
        this.loginActivityView=loginView;
    }

    @Override
    public void login(String username, String password) {
        if(Utils.isMobileNO(username)){
            BmobUser.loginBySMSCode(username, password, new LogInListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (myUser!=null){

                        loginActivityView.loginSuccess(myUser,",登录成功");
                    }else {
                        loginActivityView.loginFailed("该手机号码没注册或者验证码错误");
                    }
                }
            });
        }else {
            user.setUsername(username);
            user.setPassword(password);
            user.login(new SaveListener<MyUser>(){
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e==null){
                        MyUser user1 = BmobUser.getCurrentUser(MyUser.class);
                        loginActivityView.loginSuccess(user1,",登录成功");

                    }else {
                        loginActivityView.loginFailed("用户名或者密码错误");
                    }


                }
            });
        }


    }

    @Override
    public void requestSMSCode(String mobile, String template, final Context context) {
        if (Utils.isMobileNO(mobile)){
            BmobSMS.requestSMSCode(mobile, template, new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e==null){
                        Utils.showShortToast(context,"验证码发送成功");
                    }else {
                        Log.i("failed","失败："+e);
                    }
                }
            });
        }else {
            Utils.showShortToast(context,"手机号格式不对");
        }
    }

    @Override
    public void register(String username, String password, String mail, String mobilePhoneNum, BmobFile uri) {
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(mail);
        user.setMobilePhoneNumber(mobilePhoneNum);
        user.setImage(uri);
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    loginActivityView.regSuccess("注册成功"+myUser.toString());
                }else {
                    loginActivityView.regFailed("注册失败"+e);
                }
            }
        });

    }
}
