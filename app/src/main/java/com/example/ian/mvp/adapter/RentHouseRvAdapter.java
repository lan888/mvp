package com.example.ian.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.ui.RentHouseDetailActivity;

import java.util.ArrayList;

/**
 * Created by Ian on 2018/1/6 0006.
 */

public class RentHouseRvAdapter extends RecyclerView.Adapter<RentHouseRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mHouseNameData;
    private ArrayList<String> mHouseDetailData;
    private ArrayList<String> mHousePriceData;
    private ArrayList<String> mHouseImgData;
    private ArrayList<String> mHouseStatusData;
    private boolean hasNoData;


    public RentHouseRvAdapter(Context context, ArrayList<String> mHouseNameData, ArrayList<String> mHouseDetailData,
                              ArrayList<String> mHousePriceData, ArrayList<String> mHouseImgData, ArrayList<String>mHouseStatusData){
        this.context = context;
        this.mHouseNameData=mHouseNameData;
        this.mHouseDetailData = mHouseDetailData;
        this.mHousePriceData = mHousePriceData;
        this.mHouseImgData = mHouseImgData;
        this.mHouseStatusData = mHouseStatusData;

        if (this.mHouseNameData == null || this.mHouseNameData.isEmpty()){
            hasNoData = true;
        }

    }
    @Override
    public RentHouseRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (hasNoData){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_has_no_data, parent, false);
        }else {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        }

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(RentHouseRvAdapter.ViewHolder holder, int position) {

            holder.houseName.setText(mHouseNameData.get(position));
            holder.houseDetail.setText(mHouseDetailData.get(position));
            holder.housePrice.setText(mHousePriceData.get(position));
            holder.houseStatus.setText(mHouseStatusData.get(position));
            Glide.with(holder.houseImg.getContext()).load(mHouseImgData.get(position)).override(500,500).centerCrop().error(R.drawable.image_add).into(holder.houseImg);





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
            Intent info = new Intent(context, RentHouseDetailActivity.class);
            info.putExtra("address",mHouseNameData.get(getAdapterPosition()));
            context.startActivity(info);

        }
    }
}
