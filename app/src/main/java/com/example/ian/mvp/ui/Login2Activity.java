package com.example.ian.mvp.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.presenter.LoginActivityPresenter;
import com.example.ian.mvp.mvp.presenter.LoginActivityPresenterImpl;
import com.example.ian.mvp.mvp.view.LoginActivityView;
import com.example.ian.mvp.permission.PermissionListener;
import com.example.ian.mvp.permission.PermissionManager;
import com.example.ian.mvp.utils.FileUtils;
import com.example.ian.mvp.utils.JumpTextUtil;
import com.example.ian.mvp.utils.Utils;
import com.example.ian.mvp.widget.RoundImageView;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Ian on 2018/1/3 0003.
 */

public class Login2Activity extends BaseActivity implements LoginActivityView {

    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login_btn;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码
    private RoundImageView mImageView;//头像
    private TextView phoneLogin;

    PermissionManager helper;



    private Bitmap mBitmap;
    protected static Uri tempUri;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    private String mFilePath;
    private String mFileName;
    private String img_url1;
    private BmobFile uri;
    private LoginActivityPresenter presenter;
    private long exitTime;
    private ImageView img;


    @Override
    public void initControl() {
        setContentView(R.layout.activity_password);

        name =  findViewById(R.id.admin_login_activity_name_input);
        password =  findViewById(R.id.admin_login_activity_password_input);
        login_btn =  findViewById(R.id.admin_login_activity_login);
        register =  findViewById(R.id.admin_login_activity_register);
        forgetNum =  findViewById(R.id.admin_login_activity_forgetNum);
        mImageView =  findViewById(R.id.admin_pic);
        phoneLogin = findViewById(R.id.phone_btn);
        presenter = new LoginActivityPresenterImpl(this);



        FileUtils.init();

        helper = PermissionManager.with(Login2Activity.this)
                //添加权限请求码
                .addRequestCode(Login2Activity.TAKE_PICTURE)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                        Utils.showShortToast(Login2Activity.this,"Camera Permission granted");
                    }

                    @Override
                    public void onDenied() {
                        //用户拒绝该权限时调用
                        Toast.makeText(Login2Activity.this, "Camera Permission denied",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(login_btn, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //必须调用该`setIsPositive(true)`方法
                                        helper.setIsPositive(true);
                                        helper.request();

                                    }
                                }).show();
                    }
                })
                //请求权限
                .request();
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {
        name.addTextChangedListener(new JumpTextUtil(name,password));

        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(Login2Activity.this, LoginActivity.class);
            }
        });

        forgetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login2Activity.this);
                LayoutInflater factory = LayoutInflater.from(Login2Activity.this);
                final View textEntryView = factory.inflate(R.layout.forgetnum_layout,null);
                builder.setTitle("找回密码");
                builder.setView(textEntryView);

                final EditText code =  textEntryView.findViewById(R.id.admin_register_info1);

                builder.setPositiveButton("找回密码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String codeInfo = code.getText().toString();
                        BmobUser.resetPasswordByEmail(codeInfo, new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(Login2Activity.this,"重置密码请求成功，请到" + codeInfo + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Login2Activity.this,"失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInfo = name.getText().toString();
                String passwordInfo = password.getText().toString();
                presenter.login(nameInfo,passwordInfo);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login2Activity.this);
                LayoutInflater factory = LayoutInflater.from(Login2Activity.this);
                final View textEntryView = factory.inflate(R.layout.register,null);
                builder.setTitle("管理员注册");
                builder.setView(textEntryView);

                img = textEntryView.findViewById(R.id.admin_pic_reg);
                final EditText phone =  textEntryView.findViewById(R.id.admin_register_phone);
                final EditText name = textEntryView.findViewById(R.id.admin_register_name);
                final EditText firstPassword =  textEntryView.findViewById(R.id.admin_register_first_password);
                final EditText secondPassword = textEntryView.findViewById(R.id.admin_register_second_password);
                final EditText mail = textEntryView.findViewById(R.id.admin_register_mail);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showChoosePicDialog();


                    }
                });
                Toast.makeText(Login2Activity.this, "请点击头像框设置头像" , Toast.LENGTH_SHORT).show();

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", null);
                builder.create();
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phoneInfo = phone.getText().toString();
                        String nameInfo = name.getText().toString();
                        String firstPasswordInfo = firstPassword.getText().toString();
                        String secondPasswordInfo = secondPassword.getText().toString();
                        String mailInfo = mail.getText().toString();
                        if (phoneInfo.matches("[1][3589]\\d{9}")) {
                            if (!nameInfo.equals("")) {
                                if (firstPasswordInfo.matches("[0-9]{6}")) {
                                    if (firstPasswordInfo.equals(secondPasswordInfo)) {
                                        if (!mailInfo.equals("")) {
                                            if (uri != null||!img.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.mipmap.admin_photo).getConstantState())){
                                                presenter.register(nameInfo,secondPasswordInfo,mailInfo,phoneInfo,uri);
                                                dialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(Login2Activity.this, "未添加头像，请点击头像框添加头像", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText(Login2Activity.this, "电子邮箱不能为空", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Login2Activity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login2Activity.this, "密码为6位纯数字", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Login2Activity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login2Activity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

    @Override
    public void loginSuccess(MyUser user, String str) {
        int a = Utils.getIntValue(this,"is_landlord");
        if (a==1){
            Utils.start_Activity(this, LandlordActivity.class);
            finish();
        }else {
            Utils.start_Activity(this, TenantActivity.class);
            finish();
        }
        Utils.showShortToast(this,user.getUsername()+str);
      //  Utils.start_Activity(this, LandlordActivity.class);

    }

    @Override
    public void loginFailed(String str) {
        Utils.showShortToast(this,str);

    }

    @Override
    public void regSuccess(String str) {
        Utils.showShortToast(this,str);
    }

    @Override
    public void regFailed(String str) {
        Utils.showShortToast(this,str);
    }


    protected void showChoosePicDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login2Activity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);

                        break;
                    case TAKE_PICTURE: // 拍照
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            mFilePath = FileUtils.getFileDir() + File.separator;
                            File path = new File(mFilePath);
                            if (!path.exists()) {
                                path.mkdirs();
                            }
                            mFileName = System.currentTimeMillis() + ".jpg";
                            File file = new File(path, mFileName);
                            if (file.exists()) {
                                file.delete();
                            }
                            tempUri = FileUtils.getUriForFile(Login2Activity.this,file);
                            FileUtils.startActionFile(Login2Activity.this,file,"image/*");
                            FileUtils.startActionCapture(Login2Activity.this,file,TAKE_PICTURE);

                        } else {
                            Log.e("main","sdcard not exists");
                        }
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PICTURE:

                Cursor cursor1= getContentResolver().query(tempUri,null,null,null,null);
                Log.e("uri","uri:"+tempUri+"\n"+cursor1);
                Toast.makeText(Login2Activity.this,"uri:"+tempUri,Toast.LENGTH_SHORT).show();
                if (cursor1!=null&&cursor1.moveToFirst()){
                    int index1=cursor1.getColumnIndex("_display_name");
                    img_url1="/storage/emulated/0/ian/files/"+cursor1.getString(index1);
                    upload(img_url1);
                    Log.e("uri_tp","url:"+img_url1);
                }else {
                    img_url1 = tempUri.getPath();
                    upload(img_url1);
                }
                Glide.with(Login2Activity.this).load(img_url1).asBitmap().into(mImageView);
                Glide.with(Login2Activity.this).load(img_url1).asBitmap().into(img);
                //将图片URI转换成存储路径
                //  CursorLoader cursorLoader = new CursorLoader(this,tempUri,null,null,null,null);

                break;
            case CHOOSE_PICTURE:
                cutImage(data.getData());
                if (data.getData()!=null){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    Cursor cursor= getContentResolver().query(data.getData(),null,null,null,null);
                    Log.e("bd_uri","uri:"+data.getData()+"\n"+cursor);
                    Toast.makeText(Login2Activity.this,"uri:"+data.getData(),Toast.LENGTH_SHORT).show();
                    if (cursor!=null&&cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex("_data");
                            String img_url = cursor.getString(index);
                            Log.e("uri_cp","url:"+img_url);
                            upload(img_url);
                        }
                    }else{
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor2 = getContentResolver().query(data.getData(),proj, null, null, null);
                        if (cursor2!=null&&cursor2.moveToFirst()) {
                            int index = cursor2.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            String img_url = cursor2.getString(index);
                            Log.e("uri_cp", "url:" + img_url);
                            upload(img_url);
                        }
                    }
                }


                break;
            case CROP_SMALL_PICTURE:
                if (data != null){
                    setImageToView(data);
                    Toast.makeText(Login2Activity.this,"设置头像成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void cutImage(Uri uri){
        if (uri == null){
            Log.i("al","The uri is not exist.");
        }

        Intent intent = new Intent("com.android.camera.action.CROP");

        Log.e("uri!!!","Url:"+uri);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
//        Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, CROP_SMALL_PICTURE);

    }

    /**
     * 保存裁剪之后的图片数据
     */



    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(mBitmap);
            mImageView.setImageDrawable(drawable);
            img.setImageBitmap(mBitmap);
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）

        }
    }
    private void upload (String imgPath){
        final BmobFile file = new BmobFile(new File(imgPath));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    uri = file;
                    Log.e("success!!!!","上传成功"+file.getFileUrl());
                }else {
                    Log.e("fail!!!!!","失败："+e.getMessage());
                }
            }
        });
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
