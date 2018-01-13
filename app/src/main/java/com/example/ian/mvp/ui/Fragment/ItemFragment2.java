package com.example.ian.mvp.ui.Fragment;

import android.content.Intent;
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
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.ui.HouseDetailActivity;

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
public class ItemFragment2 extends Fragment {

    // TODO: Customize parameters
    private MyItemRecyclerViewAdapter mAdapter;
    private List<String> mHouseNameData= new ArrayList<String>();
    private List<String> mHouseDetailData= new ArrayList<String>();
    private List<String> mHousePriceData= new ArrayList<String>();
    private List<String> mHouseImgData= new ArrayList<String>();
    private List<String> mHouseStatusData= new ArrayList<String>();
    String user;
    public ItemFragment2() {

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
           BmobQuery<Rooms> query = new BmobQuery<Rooms>();
           query.addWhereEqualTo("user",user);
           query.findObjects(new FindListener<Rooms>(){
               @Override
               public void done(List<Rooms> list, BmobException e) {
                   if (e == null) {
                       for (Rooms r : list) {
                           mHouseNameData.add(r.getAddressInfo()) ;
                           mHouseDetailData.add(r.getAddressDetail());
                           mHousePriceData.add(r.getPrice());
                           mHouseImgData.add(r.getHouseImg().get(0));
                           mHouseStatusData.add(r.getStatus());
                       }
                       RecyclerView recyclerView = (RecyclerView) view;
                       mAdapter = new MyItemRecyclerViewAdapter(mHouseNameData);
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


        private List<String> mHouseNameData;



        public MyItemRecyclerViewAdapter( List<String> mHouseNameData) {
            this.mHouseNameData=mHouseNameData;


        }

        @Override
        public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.houseName.setText(mHouseNameData.get(position));
            holder.houseDetail.setText(mHouseDetailData.get(position));
            holder.housePrice.setText(mHousePriceData.get(position));
            holder.houseStatus.setText(mHouseStatusData.get(position));
            Glide.with(holder.houseImg.getContext())
                    .load(mHouseImgData.get(position))
                    .override(500,500)
                    .centerCrop()
                    .error(R.drawable.image_add)
                    .into(holder.houseImg);
            Log.i("img",mHouseImgData.toString());
        }

        @Override
        public int getItemCount() {
            return mHouseNameData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView houseImg;
            TextView houseName;
            TextView houseDetail;
            TextView housePrice;
            TextView houseStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                houseImg = itemView.findViewById(R.id.house_img);
                houseName = itemView.findViewById(R.id.house_name);
                houseDetail = itemView.findViewById(R.id.house_detail);
                housePrice = itemView.findViewById(R.id.ll_house_price);
                houseStatus = itemView.findViewById(R.id.house_status);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent info = new Intent(getContext(), HouseDetailActivity.class);
                info.putExtra("address",mHouseNameData.get(getAdapterPosition()));
                getContext().startActivity(info);
            }
        }
    }



}
