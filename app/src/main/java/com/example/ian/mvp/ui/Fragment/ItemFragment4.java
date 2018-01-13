package com.example.ian.mvp.ui.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.mvp.R;
import com.example.ian.mvp.mvp.model.Bill;
import com.example.ian.mvp.mvp.model.MyUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ItemFragment4 extends Fragment {

    // TODO: Customize parameters
    private MyItemRecyclerViewAdapter mAdapter;
    private List<String> mDatas = new ArrayList<String>();
    private List<String> mDataTime = new ArrayList<String>();
    private List<String> mDataTenant = new ArrayList<String>();
    private List<String> mDataMonth = new ArrayList<String>();
    private List<String> mDataMoney = new ArrayList<String>();
    String pi = null;
    String po =null;
    String pp = null;
    String user = BmobUser.getCurrentUser(MyUser.class).getUsername();

    public ItemFragment4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_item_list2, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            BmobQuery<Bill> query = new BmobQuery<Bill>();
            query.addWhereEqualTo("tenant",user);
            query.findObjects(new FindListener<Bill>(){
                @Override
                public void done(List<Bill> list, BmobException e) {
                    if (e == null) {
                        for (Bill r : list) {
                            String s = r.getRoom();
                            String b = r.getMonth();
                            String g = r.getCreatedAt();
                            String m = r.getBill();
                            String t = r.getStatus();
                            mDatas.add(s);
                            mDataTime.add(g);
                            mDataMonth.add(b);
                            mDataMoney.add(m);
                            mDataTenant.add(t);

                        }
                        RecyclerView recyclerView = (RecyclerView) view;
                        mAdapter = new MyItemRecyclerViewAdapter(mDatas);;
                        recyclerView.setAdapter(mAdapter);

                    }else {
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }
        return view;
    }
    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {


        private List<String> mData;


        public MyItemRecyclerViewAdapter(List<String> data) {
            this.mData = data;

        }

        @Override
        public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_time, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mAddress.setText(mData.get(position));
            holder.mStatus.setText(mDataTenant.get(position));
            holder.mTime.setText(mDataTime.get(position));
            holder.mMoney.setText(mDataMoney.get(position));
            holder.mMonth.setText(mDataMonth.get(position));
           // holder.mTag.setText("单号             房号              金额                   账单月份 ");

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView mAddress;
            TextView mStatus;
            TextView mTime;
            TextView mMonth;
            TextView mMoney;
            //TextView mTag;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mAddress = itemView.findViewById(R.id.address);
                mStatus = itemView.findViewById(R.id.status);
                mTime = itemView.findViewById(R.id.time);
                mMoney = itemView.findViewById(R.id.amount);
                mMonth = itemView.findViewById(R.id.month);
              //  mTag = itemView.findViewById(R.id.tag);

            }

            @Override
            public void onClick(View view) {
                String roomInfo = mDataTime.get(getAdapterPosition()) ;
                BmobQuery query = new BmobQuery<Bill>();
                query.addWhereEqualTo("createdAt",roomInfo);
                query.setLimit(1);
                query.findObjects(new FindListener<Bill>() {
                    @Override
                    public void done(List<Bill> list, BmobException e) {
                        if(e==null){
                            Log.e("success","查询成功:"+list.size()+"条数据");
                            for (  Bill r  : list){
                                pi = r.getRoom();
                                po = r.getCreatedAt();
                                pp = r.getBill();
                            }
                            Toast.makeText(getActivity(),"房间号为："+pi+"\n时间："+po+"\n上月房租金额为："+pp,Toast.LENGTH_SHORT).show();
                        }else {
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }

                });


                }

            }
        }


}
