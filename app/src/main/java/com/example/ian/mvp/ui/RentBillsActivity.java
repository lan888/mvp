package com.example.ian.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ian.mvp.R;
import com.example.ian.mvp.ui.Fragment.ItemFragment4;
import com.example.ian.mvp.utils.Utils;

/**
 * Created by Ian on 2018/1/13 0013.
 */

public class RentBillsActivity extends AppCompatActivity {

    private ItemFragment4 itemFragment4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_bills_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的账单");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.start_Activity(RentBillsActivity.this,TenantActivity.class);
            }
        });
        itemFragment4 = new ItemFragment4();
        if (savedInstanceState!=null){
            itemFragment4 = (ItemFragment4)getSupportFragmentManager().getFragment(savedInstanceState,"itemFragment4");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main,itemFragment4).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"itemFragment4",itemFragment4);
    }


}
