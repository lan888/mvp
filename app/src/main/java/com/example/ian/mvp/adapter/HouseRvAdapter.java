package com.example.ian.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;
import com.example.ian.mvp.mvp.model.Rooms;
import com.example.ian.mvp.ui.HouseDetailActivity;
import com.example.ian.mvp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Ian on 2018/1/6 0006.
 */

public class HouseRvAdapter  extends RecyclerView.Adapter<HouseRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mHouseNameData;
    private ArrayList<String> mHouseDetailData;
    private ArrayList<String> mHousePriceData;
    private ArrayList<String> mHouseImgData;
    private ArrayList<String> mHouseStatusData;
    private List<Rooms> roomsList;
    private boolean hasNoData;
    String deleteText;
    Rooms r = new Rooms();

    public HouseRvAdapter(Context context, ArrayList<String> mHouseNameData, ArrayList<String> mHouseDetailData,
                          ArrayList<String> mHousePriceData, ArrayList<String> mHouseImgData,ArrayList<String>mHouseStatusData,List<Rooms>roomsList){
        this.context = context;
        this.mHouseNameData=mHouseNameData;
        this.mHouseDetailData = mHouseDetailData;
        this.mHousePriceData = mHousePriceData;
        this.mHouseImgData = mHouseImgData;
        this.mHouseStatusData = mHouseStatusData;
        this.roomsList = roomsList;

        if (this.mHouseNameData == null || this.mHouseNameData.isEmpty()){
            hasNoData = true;
        }

    }
    @Override
    public HouseRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (hasNoData){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_has_no_data, parent, false);
        }else {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_house, parent, false);
        }

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(HouseRvAdapter.ViewHolder holder, int position) {

            holder.houseName.setText(mHouseNameData.get(position));
            holder.houseDetail.setText(mHouseDetailData.get(position));
            holder.housePrice.setText(mHousePriceData.get(position));
            holder.houseStatus.setText(mHouseStatusData.get(position));
            Glide.with(holder.houseImg.getContext()).load(mHouseImgData.get(position)).centerCrop().error(R.drawable.image_add).into(holder.houseImg);




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
        Button delBtn;


        public ViewHolder(View itemView) {
            super(itemView);
           houseImg = itemView.findViewById(R.id.house_img);
           houseName = itemView.findViewById(R.id.house_name);
           houseDetail = itemView.findViewById(R.id.house_detail);
           housePrice = itemView.findViewById(R.id.ll_house_price);
           houseStatus = itemView.findViewById(R.id.house_status);
           delBtn = itemView.findViewById(R.id.del_btn);
           itemView.setOnClickListener(this);
           delBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view==delBtn){
                deleteText = roomsList.get(getAdapterPosition()).getObjectId();
                r.setObjectId(deleteText);
                r.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("success", "删除成功:" + r.getAddressInfo());
                        } else {
                            Log.e("fail", "删除失败：" + e.getMessage());
                        }
                    }
                });
                mHouseNameData.remove(getAdapterPosition());
                mHouseImgData.remove(getAdapterPosition());
                Utils.showShortToast(context,"删除成功，请下拉刷新一下");
            }else {
                Intent info = new Intent(context, HouseDetailActivity.class);
                info.putExtra("address",mHouseNameData.get(getAdapterPosition()));
                context.startActivity(info);
            }


        }
    }
}
