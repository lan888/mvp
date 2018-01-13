package com.example.ian.mvp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.mvp.R;

import java.util.ArrayList;

/**
 * Created by Ian on 2018/1/6 0006.
 */

public class TenantsRvAdapter extends RecyclerView.Adapter<TenantsRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> mTenantNameData = new ArrayList<>();
    private ArrayList<String> mTenantGenderData= new ArrayList<>();
    private ArrayList<String> mTenantTelData= new ArrayList<>();
    private ArrayList<String> mTenantIdData= new ArrayList<>();
    private ArrayList<String> mTenantPhotoData= new ArrayList<>();
    private boolean hasNoData;

    public TenantsRvAdapter(Context context, ArrayList<String> mTenantNameData, ArrayList<String> mTenantGenderData,
                            ArrayList<String> mTenantTelData, ArrayList<String> mTenantIdData, ArrayList<String> mTenantPhotoData) {
        this.context = context;
        this.mTenantNameData = mTenantNameData;
        this.mTenantGenderData = mTenantGenderData;
        this.mTenantTelData = mTenantTelData;
        this.mTenantIdData = mTenantIdData;
        this.mTenantPhotoData = mTenantPhotoData;
        if (this.mTenantNameData == null || this.mTenantNameData.isEmpty()){
            hasNoData = true;
        }
    }



    @Override
    public TenantsRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (hasNoData){
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_has_no_data, parent, false);
        }else {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renter, parent, false);
        }

            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(TenantsRvAdapter.ViewHolder holder, int position) {

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
           itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mTenantTelData.get(getAdapterPosition())));
            context.startActivity(intent);

        }
    }
}
