package com.example.ian.mvp.mvp.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by chenyinghao on 2017/12/7.
 */

public class MyUser extends BmobUser {

    private BmobFile image;

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
