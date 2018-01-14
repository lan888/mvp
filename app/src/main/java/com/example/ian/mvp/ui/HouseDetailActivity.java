package com.example.ian.mvp.ui;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.base.BaseActivity;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.utils.Utils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Ian on 2018/1/8 0008.
 */

public class HouseDetailActivity extends BaseActivity {
    private Button collectHouseBill_btn;
    private Banner banner;
    TextView addressInfo;
    TextView  typeInfo;
    TextView timeInfo;
    TextView  detailInfo1;
    TextView  priceInfo;
    TextView  contactsInfo;
    TextView  areaInfo;
    TextView  floorInfo;
    TextView  telInfo;
    TextView  directionInfo;
    TextView  allFloorInfo;
    TextView  desInfo;
    TextView  statusInfo;
    TextView tenantNameInfo;
    TextView rentTimeInfo;
    String address;
    @Override
    public void initControl() {
        setContentView(R.layout.house_detail_layout);
        address = getIntent().getStringExtra("address");
        addressInfo = findViewById(R.id.add_house_address);
        typeInfo = findViewById(R.id.add_house_type);
        timeInfo = findViewById(R.id.add_house_time);
        detailInfo1 = findViewById(R.id.add_house_address_detail);
        priceInfo = findViewById(R.id.add_house_price);
        contactsInfo = findViewById(R.id.add_house_contacts);
        areaInfo = findViewById(R.id.add_house_area);
        floorInfo= findViewById(R.id.add_house_floor);
        telInfo = findViewById(R.id.add_house_contacts_tel);
        directionInfo = findViewById(R.id.add_house_direction);
        allFloorInfo = findViewById(R.id.add_house_all_floor);
        desInfo = findViewById(R.id.add_house_des);
        collectHouseBill_btn = findViewById(R.id.collectHouseBill_btn);
        statusInfo = findViewById(R.id.status);
        tenantNameInfo = findViewById(R.id.tenant);
        rentTimeInfo = findViewById(R.id.tenant_time);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.finish(HouseDetailActivity.this);
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
                        setText(r.getStatus(),r.getAddressInfo(),r.getType(),r.getAddressDetail(),
                                r.getCreatedAt(),r.getPrice(),r.getContacts(),r.getArea(),r.getFloor(),
                                r.getContactsTel(),r.getDirection(),r.getAllFloor(),r.getHouseDes(),r.getTenant(),r.getUpdatedAt());
                        Log.i("at",r.getUpdatedAt());
                    }

                    Log.i("bmob","成功：");
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

    @Override
    public void setListener() {

    }

    private void setText(String status,String address,String type,String detail,String time,String price
    ,String contacts,String area,String floor,String tel,String direction,String allFloor,String des,String tenantName,String rentTime){
        statusInfo.setText("出租状态："+status);
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
        tenantNameInfo.setText("租客名称:"+tenantName);
        rentTimeInfo.setText("出租时间:"+rentTime);
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
