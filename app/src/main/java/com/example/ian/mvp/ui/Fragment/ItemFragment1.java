package com.example.ian.mvp.ui.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.mvp.model.MyUser;
import com.example.ian.mvp.mvp.model.Renters;

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
public class ItemFragment1 extends Fragment {

    // TODO: Customize parameters
    private MyItemRecyclerViewAdapter mAdapter;
    private ArrayList<String> mTenantNameData = new ArrayList<>();
    private ArrayList<String> mTenantGenderData= new ArrayList<>();
    private ArrayList<String> mTenantTelData= new ArrayList<>();
    private ArrayList<String> mTenantIdData= new ArrayList<>();
    private ArrayList<String> mTenantPhotoData= new ArrayList<>();
    String user;

    public ItemFragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_item_list1, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
            if (myUser!=null){
                user = myUser.getUsername();
            }
            BmobQuery<Renters> query = new BmobQuery<Renters>();
            query.addWhereEqualTo("landlord",user);
            query.findObjects(new FindListener<Renters>(){
                @Override
                public void done(List<Renters> list, BmobException e) {
                    if (e == null) {
                        for (Renters r : list) {
                            mTenantNameData.add(r.getName());
                            mTenantGenderData.add(r.getSex());
                            mTenantIdData.add(r.getIdcard());
                            mTenantTelData.add(r.getPhone());
                            mTenantPhotoData.add(r.getPhoto());
                        }
                        RecyclerView recyclerView = (RecyclerView) view;
                        mAdapter = new MyItemRecyclerViewAdapter();
                        recyclerView.setAdapter(mAdapter);

                        Log.i("bmob","成功："+mTenantNameData.size());
                    }else {
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }


        return view;
    }


    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {



        @Override
        public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.tenantName.setText(mTenantNameData.get(position));
            if (mTenantGenderData.get(position).equals("男")){
                holder.tenantGender.setImageResource(R.drawable.male);
            }else {
                holder.tenantGender.setImageResource(R.drawable.female);
            }
            holder.tenantTel.setText("手机号码："+mTenantTelData.get(position));
            holder.tenantId.setText("身份证号码："+mTenantIdData.get(position));
            Glide.with(holder.tenantPhoto.getContext()).load(mTenantPhotoData.get(position)).centerCrop().error(R.drawable.image_add).into(holder.tenantPhoto);
        }

        @Override
        public int getItemCount() {
            return mTenantNameData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView tenantPhoto;
            TextView tenantName;
            ImageView tenantGender;
            TextView tenantTel;
            TextView tenantId;


            public ViewHolder(View itemView) {
                super(itemView);
                tenantPhoto = itemView.findViewById(R.id.renter_img);
                tenantName = itemView.findViewById(R.id.renter_name);
                tenantGender = itemView.findViewById(R.id.renter_gender);
                tenantTel = itemView.findViewById(R.id.renter_tel);
                tenantId = itemView.findViewById(R.id.renter_id);
            }


            @Override
            public void onClick(View view) {




            }
        }


    }
    }





