package com.example.ian.mvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ian.mvp.R;
import com.example.ian.mvp.mvp.model.Bill;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Ian on 2018/2/25 0025.
 */

public class Section2Adapter extends StickyHeaderAdapter {
    private Context context;
    private List<Bill> billsList;
    private LinkedHashMap<String, List<Bill>> map = new LinkedHashMap<String, List<Bill>>();

    public Section2Adapter(Context context, List<Bill> billsList) {
        this.context = context;
        this.billsList = billsList;
        addData(billsList);

    }

    public void addData(List<Bill> list){
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_row,null);

        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        final Bill item = map.get(key).get(row);
        ((TextView) view.findViewById(R.id.time)).setText(item.getCreatedAt());
        ((TextView) view.findViewById(R.id.src)).setText(item.getRoom());
        ((TextView) view.findViewById(R.id.dest)).setText(item.getStatus());
        ((TextView) view.findViewById(R.id.amount)).setText(item.getBill() + "");
        ((TextView) view.findViewById(R.id.waterBill)).setText("水费："+item.getWaterBill());
        ((TextView) view.findViewById(R.id.electricityBill)).setText("电费："+item.getElectricityBill());

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_header, null);

        Object[] keys = map.keySet().toArray();

        String key = (String) keys[section];

        ((TextView) view.findViewById(R.id.month)).setText(key.split("-")[0]+"年"+key.split("-")[1]+"月");

        return view;
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }
}
