package com.example.ian.mvp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.mvp.R;
import com.example.ian.mvp.adapter.SpinnerAdapter;
import com.example.ian.mvp.adapter.StickyHeaderAdapter;
import com.example.ian.mvp.mvp.model.Bill;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.widget.OnValueChangeListener;
import com.example.ian.mvp.widget.StickyHeaderListView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Ian on 2017/10/1 0001.
 */

public class BillsActivity extends AppCompatActivity {

    final Bill b1= new Bill() ;
    private StickyHeaderListView mListView;
    private SectionAdapter mAdapter;
    private CheckBox mCheckAll;
    private List<Bill> rows = new ArrayList<>();
    private List<String> data = new LinkedList<>();
    private Map<String,String> tMap = new HashMap<>();
    private String dataPrice ;
    private boolean isOnLoadMore = false;
    private int count = 0;
    LoadingDialog ld;
    String user = BmobUser.getCurrentUser(MyUser.class).getUsername();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillsActivity.this,LandlordActivity.class);
                startActivity(intent);
            }
        });

        initListView();
        initChechBox();
        ld = new LoadingDialog(BillsActivity.this);
        ld.show();
        ld.setLoadingText("载入中");
        BmobQuery<Rooms> query = new BmobQuery<Rooms>();
        query.addWhereEqualTo("user",user);
        query.addWhereEqualTo("status","已出租");
        query.findObjects(new FindListener<Rooms>(){
            @Override
            public void done(List<Rooms> list, BmobException e) {
                if (e == null) {
                    for (Rooms r : list) {
                        data.add(r.getAddressInfo());
                        tMap.put(r.getAddressInfo(),r.getTenant());

                    }


                    Log.i("bmob","成功："+data.size());
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.setAdapter(mAdapter = new SectionAdapter(BillsActivity.this, rows));
            ld.close();
            mAdapter.setOnValueChangedListener(new OnValueChangeListener() {
                @Override
                public void onChange(int totalCount, double totalAmount, boolean isCheckAll) {
                    BigDecimal bd = new BigDecimal(totalAmount).setScale(2, RoundingMode.UP);
                    ((TextView) findViewById(R.id.tv_desc)).setText(totalCount + "个房间，共" + bd.doubleValue() + "元");
                    // 防止调用onCheckedChanged
                    mCheckAll.setOnCheckedChangeListener(null);
                    mCheckAll.setChecked(isCheckAll);
                    mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
                }
            });
        }
    };

    private void initListView() {
        mListView = (StickyHeaderListView) findViewById(R.id.lv);
        BmobQuery<Bill> query = new BmobQuery<Bill>();
        query.addWhereEqualTo("user",user);
        query.findObjects(new FindListener<Bill>(){
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    for (Bill bill : list){
                        rows.add(bill);
                    }

                    mHandler.sendEmptyMessage(0);
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public synchronized void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (!isOnLoadMore) {

                                if(count == 0){
                                    Toast.makeText(BillsActivity.this, "没有更多数据啦~", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                count++;

                                isOnLoadMore = true;

                                mAdapter.addData(rows);

                                isOnLoadMore = false;
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initChechBox() {
        mCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
        mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAdapter.setCheckAll(isChecked);
        }
    };

    public class SectionAdapter extends StickyHeaderAdapter {

        private Context mContext;
        private OnValueChangeListener listener;

        private List<Bill> entityRows;

        private LinkedHashMap<String, List<Bill>> map = new LinkedHashMap<String, List<Bill>>();


        public SectionAdapter(Context context, List<Bill> entityRows) {
            this.mContext = context;
            this.entityRows = entityRows;

            addData(entityRows);
        }

        /**
         * 添加数据，并进行分类
         *
         * @param list
         */
        public void addData(List<Bill> list) {
//            BmobQuery<Bill> query = new BmobQuery<Bill>();
//            query.findObjects(new FindListener<Bill>() {
//                @Override
//                public void done(List<Bill> list, BmobException e) {
//                    if (e == null) {
            for (Bill row : list) {
                String time = row.getCreatedAt();
                String head = time.substring(0,7); // time
                if (map.get(head) == null) {
                    List<Bill> newRows = new ArrayList<>();
                    newRows.add(row);
                    map.put(head, newRows);
                } else {
                    List<Bill> newRows = map.get(head);
                    newRows.add(row);
                }
            }
//                    } else {
//                        Log.i("bmob", "失败" + e.getMessage() + "," + e.getErrorCode());
//                    }
//                }
//            });
            updateValue();
            notifyDataSetChanged();
        }

        public void setCheckAll(boolean checkAll) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Bill>> entry = (Map.Entry<String, List<Bill>>) iter.next();
                String key = entry.getKey();
                List<Bill> val = entry.getValue();

                for (Bill row : val) {
                    row.isChecked = checkAll;
                }
            }

            updateValue();

            notifyDataSetChanged();
        }

        public void setOnValueChangedListener(OnValueChangeListener listener) {
            this.listener = listener;
        }

        private void updateValue() {
            if (listener == null)
                return;

            int count = 0;
            double amount = 0;
            boolean isCheckAll = true;

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Bill>> entry = (Map.Entry<String, List<Bill>>) iter.next();
                String key = entry.getKey();
                List<Bill> val = entry.getValue();

                for (Bill row : val) {
                    if (row.isChecked) {
                        count++;
                        amount += Double.parseDouble(row.getBill());
                    } else {
                        isCheckAll = false;
                    }
                }
            }

            listener.onChange(count, amount, isCheckAll);
        }

        @Override
        public int sectionCounts() {
            return map.keySet().toArray().length;
        }

        @Override
        public int rowCounts(int section) {
            if (section < 0)
                return 0;

            Object[] key = map.keySet().toArray();


            return map.get(key[section]).size();
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_row, null);

            Object[] keys = map.keySet().toArray();
            String key = (String) keys[section];
            final Bill item = map.get(key).get(row);
            ((TextView) view.findViewById(R.id.time)).setText(item.getCreatedAt());
            ((TextView) view.findViewById(R.id.src)).setText(item.getRoom());
            ((TextView) view.findViewById(R.id.dest)).setText(item.getStatus());
            ((TextView) view.findViewById(R.id.amount)).setText(item.getBill() + "");
            ((TextView) view.findViewById(R.id.waterBill)).setText("水费："+item.getWaterBill());
            ((TextView) view.findViewById(R.id.electricityBill)).setText("电费："+item.getElectricityBill());
            final CheckBox checkBox = ((CheckBox) view.findViewById(R.id.cb));
            Button chk = (Button) findViewById(R.id.check);
            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BillsActivity.this);
                    LayoutInflater factory = LayoutInflater.from(BillsActivity.this);
                    final View textEntryView = factory.inflate(R.layout.money,null);
                    builder.setTitle("收款");
                    builder.setView(textEntryView);

                    final Spinner address = textEntryView.findViewById(R.id.nice_spinner);
                    final EditText waterBill = textEntryView.findViewById(R.id.waterBill);
                    final EditText electricityBill = textEntryView.findViewById(R.id.electricityBill);
                    SpinnerAdapter spinnerAdapter = new SpinnerAdapter(BillsActivity.this,data);
                    address.setAdapter(spinnerAdapter);


                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SimpleDateFormat formatter  =   new    SimpleDateFormat    ("yyyy年MM月");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            final String str =  formatter.format(curDate);

                            final String addressInfo = data.get(address.getSelectedItemPosition());
                            Log.i("addressInfo",addressInfo);
                            BmobQuery<Rooms> query = new BmobQuery<Rooms>();
                            query.addWhereEqualTo("addressInfo",addressInfo);
                            query.findObjects(new FindListener<Rooms>(){
                                @Override
                                public void done(List<Rooms> list, BmobException e) {
                                    if (e == null) {
                                        for (Rooms r : list) {
                                            dataPrice = r.getPrice();

                                        }
                                        double waterBillInfo1 = Double.parseDouble(waterBill.getText().toString());
                                        double electricityBillInfo1 = Double.parseDouble(electricityBill.getText().toString());
                                        double bill = Double.parseDouble(dataPrice);
                                        String waterBillInfo = waterBill.getText().toString();
                                        String electricityBillInfo = electricityBill.getText().toString();
                                        Double sum =  waterBillInfo1+electricityBillInfo1+bill;
                                        String sumInfo = sum.toString();
                                        if (waterBill.getText().toString().matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                            if (electricityBill.getText().toString().matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                                b1.setBill(sumInfo);
                                                b1.setRoom(addressInfo);
                                                b1.setTenant(tMap.get(addressInfo));
                                                Log.i("map","tenant:"+tMap.get(addressInfo));
                                                b1.setWaterBill(waterBillInfo);
                                                b1.setElectricityBill(electricityBillInfo);
                                                b1.setMonth(str);
                                                b1.setUser(user);
                                                b1.setStatus("未缴费");
                                                b1.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if (e == null) {
                                                            Log.e("success", "添加数据成功，返回objectId为：" + s);
                                                        } else {
                                                            Log.e("fail", "创建数据失败：" + e.getMessage());
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(BillsActivity.this, "电费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(BillsActivity.this, "水费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                        }


                                        Intent intent = new Intent(BillsActivity.this,SuccessAddActivity.class);
                                        startActivity(intent);

                                        Log.i("bmob","成功："+data.size());
                                    }else {
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }

                            });


                        }

                    });
                    builder.create().show();

                }
            });

            checkBox.setChecked(item.isChecked);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isChecked = isChecked;
                    Log.e("item_ck","item "+item.isChecked);
                    updateValue();
                    if (item.isChecked==true){
                        b1.setChecked(true);
                        b1.update(item.getObjectId(),new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("success","更新成功:"+ item.getObjectId() + item +b1.isChecked);
                                }else{
                                    Log.e("succes","更新失败：" + e.getMessage());
                                }
                            }
                        });
                    }else {
                        b1.setChecked(false);
                        b1.update(item.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.e("success！", "更新成功:" + item.getObjectId() + item +b1.isChecked);
                                } else {
                                    Log.e("succes", "更新失败：" + e.getMessage());
                                }
                            }
                        });
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                    if (item.isChecked==true){
                        b1.setChecked(true);
                        b1.update(item.getObjectId(),new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("success","更新成功:"+ item.getObjectId() + item +b1.isChecked);
                                }else{
                                    Log.e("succes","更新失败：" + e.getMessage());
                                }
                            }
                        });
                    }else {
                        b1.setChecked(false);
                        b1.update(item.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.e("success！", "更新成功:" + item.getObjectId() + item +b1.isChecked);
                                } else {
                                    Log.e("succes", "更新失败：" + e.getMessage());
                                }
                            }
                        });
                    }


                }
            });

            return view;
        }

        @Override
        public Object getRowItem(int section, int row) {
            if (section < 0)
                return null;

            Object[] key = map.keySet().toArray();

            return map.get((String) key[section]).get(row);
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_header, null);

            Object[] keys = map.keySet().toArray();

            String key = (String) keys[section];

            ((TextView) view.findViewById(R.id.month)).setText(key.split("-")[1]+"月");

            return view;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("账单");
        return true;
    }



}

