package com.example.ian.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ian.mvp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Ian on 2017/8/3 0003.
 */

public class ForgetNumActivity extends AppCompatActivity {

    private EditText code;
    private Button find;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetnum_layout);

        code = (EditText) findViewById(R.id.admin_register_info1);
        find = (Button) findViewById(R.id.find);


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final String codeInfo = code.getText().toString();

                BmobUser.resetPasswordByEmail(codeInfo, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(ForgetNumActivity.this,"重置密码请求成功，请到" + codeInfo + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetNumActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(ForgetNumActivity.this,"失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });



    }
}
