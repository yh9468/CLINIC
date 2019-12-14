package com.example.younho.clinic.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.younho.clinic.R;

import java.util.ArrayList;
import java.util.Random;

public class MyListAdapter_Cloth extends BaseAdapter {
    private Context context;
    private ArrayList<Cloth> list_item_Array_List;

    TextView name;
    TextView price;


    public MyListAdapter_Cloth(Context context, ArrayList<Cloth> list_item_Array_List)
    {
        this.context = context;
        this.list_item_Array_List = list_item_Array_List;
    }

    @Override
    public int getCount() {
        return this.list_item_Array_List.size();
    }

    @Override
    public Object getItem(int position) {
        return list_item_Array_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_cloth, null);
        }
        if(list_item_Array_List.size() ==0)
            return convertView;

        name = (TextView)convertView.findViewById(R.id.cloth_name);
        price = (TextView) convertView.findViewById(R.id.cloth_price);
        String nameset = list_item_Array_List.get(position).getName() + " : ";
        String priceset = Integer.toString(list_item_Array_List.get(position).getPrice()) + "원";

        name.setText(nameset);
        price.setText(priceset);
        return convertView;
    }
}
