package com.example.ian.mvp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.Bill;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Renters;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Ian on 2018/1/11 0011.
 */

public class RentHouseDetailActivity extends BaseActivity {
    private Banner banner;
    TextView addressInfo;
    TextView typeInfo;
    TextView timeInfo;
    TextView detailInfo1;
    TextView priceInfo;
    TextView contactsInfo;
    TextView areaInfo;
    TextView floorInfo;
    TextView telInfo;
    TextView directionInfo;
    TextView allFloorInfo;
    TextView desInfo;
    String address;
    String tel;
    String id;
    String landlord;
    Button call_btn;
    Button rent_btn;
    Renters r1 = new Renters();
    Rooms r2 = new Rooms();
    Bill bill = new Bill();
    MyUser user = BmobUser.getCurrentUser(MyUser.class);

    @Override
    public void initControl() {
        setContentView(R.layout.renthouse_detail_layout);
        address = getIntent().getStringExtra("address");
        addressInfo = findViewById(R.id.house_address);
        typeInfo = findViewById(R.id.house_type);
        timeInfo = findViewById(R.id.house_time);
        detailInfo1 = findViewById(R.id.house_address_detail);
        priceInfo = findViewById(R.id.house_price);
        contactsInfo = findViewById(R.id.house_contacts);
        areaInfo = findViewById(R.id.house_area);
        floorInfo= findViewById(R.id.house_floor);
        telInfo = findViewById(R.id.house_contacts_tel);
        directionInfo = findViewById(R.id.house_direction);
        allFloorInfo = findViewById(R.id.house_all_floor);
        desInfo = findViewById(R.id.house_des);
        call_btn = findViewById(R.id.call_btn);
        rent_btn = findViewById(R.id.rent_btn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.finish(RentHouseDetailActivity.this);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle(address);
        return true;
    }

    @Override
    public void initView() {
        banner = findViewById(R.id.gw);
        banner.setImageLoader(new GlideImageLoader());
    }

    @Override
    public void initData() {
        BmobQuery<Rooms> query = new BmobQuery<Rooms>();
        query.addWhereEqualTo("addressInfo",address);
        query.findObjects(new FindListener<Rooms>(){
            @Override
            public void done(List<Rooms> list, BmobException e) {
                if (e == null) {
                    for (Rooms r : list) {
                        banner.setImages(r.getHouseImg());
                        banner.start();
                        landlord = r.getUser();
                        tel = r.getContactsTel();
                        setText(r.getAddressInfo(),r.getType(),r.getAddressDetail(),
                                r.getCreatedAt(),r.getPrice(),r.getContacts(),r.getArea(),r.getFloor(),
                                r.getContactsTel(),r.getDirection(),r.getAllFloor(),r.getHouseDes());
                    }

                    Log.i("bmob","成功：");
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }
    private void setText(String address,String type,String detail,String time,String price
            ,String contacts,String area,String floor,String tel,String direction,String allFloor,String des){
        addressInfo.setText("房屋名称:"+address);
        typeInfo.setText("房屋类型:"+type);
        detailInfo1.setText("详细地址:"+detail);
        timeInfo.setText("房屋更新时间:"+time);
        priceInfo.setText("月租金:"+price);
        contactsInfo.setText("联系人:"+contacts);
        areaInfo.setText("房屋面积:"+area);
        floorInfo.setText("房间所在楼层:"+floor);
        telInfo.setText("联系人电话:"+tel);
        directionInfo.setText("房屋朝向:"+direction);
        allFloorInfo.setText("房屋总楼层:"+allFloor);
        desInfo.setText(des);
    }
    @Override
    public void setListener() {
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(tel);
            }
        });
        if (Utils.getIntValue(RentHouseDetailActivity.this,"rent")==2){
            rent_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RentHouseDetailActivity.this);
                    LayoutInflater factory = LayoutInflater.from(RentHouseDetailActivity.this);
                    final View textEntryView = factory.inflate(R.layout.renter,null);
                    builder.setTitle("确认租房");
                    builder.setView(textEntryView);

                    final EditText renter_name = textEntryView.findViewById(R.id.renter_name);
                    final Spinner renter_sex = textEntryView.findViewById(R.id.renter_sex);
                    final EditText renter_phone = textEntryView.findViewById(R.id.renter_phone);
                    final EditText renter_idCard = textEntryView.findViewById(R.id.renter_idcard);

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String renter_nameInfo = renter_name.getText().toString();
                            final String renter_sexInfo = (String)renter_sex.getSelectedItem();
                            final String renter_phoneInfo = renter_phone.getText().toString();
                            final String renter_idCardInfo = renter_idCard.getText().toString();

                            String photo = user.getImage().getFileUrl();


                            if (!renter_nameInfo.equals("")) {
                                if (Utils.isMobileNO(renter_phoneInfo)) {
                                    if (renter_idCardInfo.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)")) {
                                        r1.setName(renter_nameInfo);
                                        r1.setHouse(address);
                                        r1.setLandlord(landlord);
                                        r1.setSex(renter_sexInfo);
                                        r1.setPhone(renter_phoneInfo);
                                        r1.setIdcard(renter_idCardInfo);
                                        r1.setPhoto(photo);
                                        r1.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {

                                                    Log.e("success", "添加数据成功，返回objectId为：" + s);
                                                } else {
                                                    Log.e("fail", "创建数据失败：" + e.getMessage());
                                                }
                                            }
                                        });

                                        String bql = "select getObjectId from Rooms where addressInfo = ?";
                                        new BmobQuery<Rooms>().doSQLQuery(bql, new SQLQueryListener<Rooms>() {
                                            @Override
                                            public void done(BmobQueryResult bmobQueryResult, BmobException e) {
                                                if (e == null) {
                                                    List<Rooms> list = (List<Rooms>) bmobQueryResult.getResults();
                                                    if (list != null && list.size() > 0) {
                                                        for (Rooms r : list) {
                                                            id = r.getObjectId();

                                                        }
                                                        r2.setStatus("已出租");
                                                        r2.setTenant(user.getUsername());
                                                        r2.update(id, new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Log.e("success", "更新成功:" + r2.getUpdatedAt());
                                                                } else {
                                                                    Log.e("succes", "更新失败：" + e.getMessage());
                                                                }
                                                            }
                                                        });
                                                        Log.e("name", "查询成功" + id);
                                                    }
                                                } else {
                                                    Log.e("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                                                }
                                            }
                                        }, address);


                                        Toast.makeText(RentHouseDetailActivity.this, "已成功租房", Toast.LENGTH_SHORT).show();
                                        Intent info = new Intent(context, RentHouseActivity.class);
                                        info.putExtra("user",user.getUsername());
                                        context.startActivity(info);

                                    } else {
                                        Toast.makeText(RentHouseDetailActivity.this, "请填写合法的身份证号码", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(RentHouseDetailActivity.this, "请填写合法的手机号码", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(RentHouseDetailActivity.this, "租户姓名不能为空", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    builder.create().show();
                }
            });
        }else {
            rent_btn.setText("交房租");
            rent_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RentHouseDetailActivity.this);
                    dialog.setTitle("确定已交房租？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BmobQuery<Bill> query = new BmobQuery<Bill>();
                            query.addWhereEqualTo("status","未缴费");
                            query.addWhereEqualTo("tenant",user.getUsername());
                            query.addWhereEqualTo("room",address);
                            query.findObjects(new FindListener<Bill>() {
                                @Override
                                public void done(List<Bill> list, BmobException e) {
                                    if (e==null){
                                        if (list != null && list.size() > 0) {
                                            for (Bill b : list) {
                                                id = b.getObjectId();

                                            }
                                            Log.e("id",id);
                                            bill.setStatus("已缴费");
                                            bill.update(id, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Log.e("success", "更新成功:" + bill.getUpdatedAt());
                                                    } else {
                                                        Log.e("failed", "更新失败：" + e.getMessage());
                                                    }
                                                }
                                            });
                                            Log.e("name", "查询成功" + id);
                                        }
                                    }else {
                                        Log.e("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                                    }
                                }
                            });
                            Utils.showShortToast(RentHouseDetailActivity.this,"已提交");
                        }
                    });
                    dialog.create().show();

                }
            });
        }



    }
    public void callPhone(String str) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + str));
        startActivity(intent);
    }
    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);


        }
    }
}
