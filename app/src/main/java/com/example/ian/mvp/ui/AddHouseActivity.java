package com.example.ian.mvp.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.GridViewAddImgesAdpter;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.permission.PermissionListener;
import com.example.ian.mvp.permission.PermissionManager;
import com.example.ian.mvp.utils.FileUtils;
import com.example.ian.mvp.utils.Utils;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.ian.mvp.ui.LoginActivity.tempUri;

/**
 * Created by Ian on 2018/1/5 0005.
 */

public class AddHouseActivity extends BaseActivity {
    private GridView gw;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private String mFilePath;
    private String mFileName;
    private BmobFile tempFile;
    private String img_url;
    private ArrayList<String>filePaths;
    private Button addHouse_btn;
    private ArrayList<String> houseImg;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;

    EditText addressInfo;
    Spinner typeInfo;
    EditText detailInfo1;
    EditText priceInfo;
    EditText contactsInfo;
    EditText areaInfo;
    EditText floorInfo;
    EditText telInfo;
    EditText directionInfo;
    EditText allFloorInfo;
    EditText desInfo;
    LoadingDialog ld;
    PermissionManager helper;

    Rooms r = new Rooms();

    @Override
    public void initControl() {
        setContentView(R.layout.add_house_layout);
         addressInfo = findViewById(R.id.add_house_addressInfo);
         typeInfo = findViewById(R.id.add_house_typeInfo);
         detailInfo1 = findViewById(R.id.add_house_address_detailInfo1);
         priceInfo = findViewById(R.id.add_house_priceInfo);
         contactsInfo = findViewById(R.id.add_house_contactsInfo);
         areaInfo = findViewById(R.id.add_house_areaInfo);
         floorInfo= findViewById(R.id.add_house_floorInfo);
         telInfo = findViewById(R.id.add_house_contacts_telInfo);
         directionInfo = findViewById(R.id.add_house_directionInfo);
         allFloorInfo = findViewById(R.id.add_house_all_floorInfo);
         desInfo = findViewById(R.id.add_house_desInfo);
         ld = new LoadingDialog(AddHouseActivity.this);
        gw =  findViewById(R.id.gw);
        addHouse_btn = findViewById(R.id.add_house_btn);
        datas = new ArrayList<>();
        filePaths = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gw.setAdapter(gridViewAddImgesAdpter);
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showChoosePicDialog();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.finish(AddHouseActivity.this);
            }
        });
        FileUtils.init();

        helper = PermissionManager.with(AddHouseActivity.this)
                //添加权限请求码
                .addRequestCode(AddHouseActivity.TAKE_PICTURE)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                      //  Utils.showShortToast(AddHouseActivity.this,"Camera Permission granted");
                    }

                    @Override
                    public void onDenied() {
                        //用户拒绝该权限时调用
                        Toast.makeText(AddHouseActivity.this, "Camera Permission denied",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(addHouse_btn, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("发布房屋");
        return true;
    }
    @Override
    public void initView() {



    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {
        addHouse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String str[]=new String[filePaths.size()];
               String tel=telInfo.getText().toString();
               String floor=floorInfo.getText().toString();
               Log.i("tel",tel);
               if (tel.matches("[1][358]\\d{9}")){
                   if (Utils.isNumber(floor)){
                       filePaths.toArray(str);
                       ld.setLoadingText("上传图片中")
                               .setSuccessText("上传成功")//显示加载成功时的文字
                               .setFailedText("上传失败")
                               .show();
                       uploadBatch(str);

                   }else {
                       Utils.showShortToast(AddHouseActivity.this,"所在楼层请输入纯数字");
                   }
               }else {
                   Utils.showShortToast(AddHouseActivity.this,"请输入正确的手机号码");
               }
            }
        });

    }

    protected void showChoosePicDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddHouseActivity.this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片", "拍照"};
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

                            tempUri = FileUtils.getUriForFile(AddHouseActivity.this, file);
                          //  FileUtils.startActionFile(AddHouseActivity.this, file, "image/*");
                            FileUtils.startActionCapture(AddHouseActivity.this, file, TAKE_PICTURE);

                        } else {
                            Log.e("main", "sdcard not exists");
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
        switch (requestCode) {
            case TAKE_PICTURE:
                Cursor cursor1 = getContentResolver().query(tempUri, null, null, null, null);
                Log.e("uri", "uri:" + tempUri + "\n" + cursor1);
                Toast.makeText(AddHouseActivity.this, "uri:" + tempUri, Toast.LENGTH_SHORT).show();
                if (cursor1 != null && cursor1.moveToFirst()) {
                    int index1 = cursor1.getColumnIndex("_display_name");
                    img_url = "/storage/emulated/0/ian/files/"+ cursor1.getString(index1);
                    filePaths.add(img_url);
                    photoPath(img_url);
                    Log.e("cp_uri", "url:" + img_url);
                }
                else {
                    img_url = tempUri.getPath();
                    filePaths.add(img_url);
                    photoPath(img_url);
                }


            case CHOOSE_PICTURE:
                if (data != null) {
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    Log.e("bd_uri", "uri:" + data.getData() + "\n" + cursor);
                    Toast.makeText(AddHouseActivity.this, "uri:" + data.getData(), Toast.LENGTH_SHORT).show();
                    if (cursor != null && cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex("_data");
                        String cimg_url = cursor.getString(index);
                        filePaths.add(cimg_url);
                        photoPath(cimg_url);
                        //upload(cimg_url);

                    }
                }


        }
    }


    private void upload (final String imgPath){
        final BmobFile file = new BmobFile(new File(imgPath));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    tempFile = file;
                    Log.e("success!!!!","上传成功"+file.getFileUrl());
//                    Message message = new Message();
//                    message.what = 0xAAAAAAAA;
//                    message.obj = imgPath;
//                    handler.sendMessage(message);


                }else {
                    Log.e("fail!!!!!","失败："+e.getMessage());
                }
            }
        });
    }

    private void uploadBatch(final String[] paths){
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {

                if (list.size()==filePaths.size()){
                    houseImg = (ArrayList<String>) list1;
                    handler.sendEmptyMessage(0);
                }


            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                ld.loadFailed();
                Utils.showShortToast(AddHouseActivity.this,"错误码"+i+"，错误描述"+s);
                finish();

            }
        });
    }

    private void save(String user,ArrayList<String> houseImg , String addressInfo, String status, String type, String area, String addressDetail,
                      String price, String contacts, String floor, String contactsTel ,String direction,String allFloor,String houseDes){
        r.setAddressInfo(addressInfo);
        r.setHouseImg(houseImg);
        r.setStatus(status);
        r.setType(type);
        r.setArea(area);
        r.setAddressDetail(addressDetail);
        r.setPrice(price);
        r.setContacts(contacts);
        r.setFloor(floor);
        r.setContactsTel(contactsTel);
        r.setDirection(direction);
        r.setAllFloor(allFloor);
        r.setHouseDes(houseDes);
        r.setUser(user);
        r.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());

                }
            }
        });

    }

    public void photoPath(String path) {
        Map<String,Object> map=new HashMap<>();
        map.put("path",path);
        datas.add(map);
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.what == 0xAAAAAAAA) {
//                photoPath(msg.obj.toString());
//            }
            String user = BmobUser.getCurrentUser(MyUser.class).getUsername();
            String address=addressInfo.getText().toString();
            String type=typeInfo.getSelectedItem().toString();
            String detail=detailInfo1.getText().toString();
            String price=priceInfo.getText().toString();
            String contacts=contactsInfo.getText().toString();
            String area=areaInfo.getText().toString();
            String floor=floorInfo.getText().toString();
            String tel=telInfo.getText().toString();
            String direction = directionInfo.getText().toString();
            String allFloor = allFloorInfo.getText().toString();
            String houseDes = desInfo.getText().toString();
            String status = "未出租";


            save(user,houseImg,address,status,type,area,detail,price,contacts,floor,tel,direction,allFloor,houseDes);
            ld.loadSuccess();
            Utils.showShortToast(AddHouseActivity.this,"添加房源成功，请下拉刷新一下");
            finish();


        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
