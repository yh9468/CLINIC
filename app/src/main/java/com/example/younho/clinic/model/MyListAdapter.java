package com.example.younho.clinic.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.younho.clinic.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MyListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Laundry> list_item_Array_List;

    TextView name;
    TextView address;
    TextView rate;


    public MyListAdapter(Context context, ArrayList<Laundry> list_item_Array_List)
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
           convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
       }
       if(list_item_Array_List.size() ==0)
           return convertView;

       name = (TextView)convertView.findViewById(R.id.Laundry_Name);
       address = (TextView) convertView.findViewById(R.id.Laundry_Address);
       rate = (TextView) convertView.findViewById(R.id.star_rate);

       name.setText(list_item_Array_List.get(position).getName());
       address.setText(list_item_Array_List.get(position).getAddress());
       Random random = new Random();
       double rannum = random.nextDouble()*20;
       rannum = Math.round(rannum)/10.0 + 3;
       String str = Double.toString(rannum);

       rate.setText(str);

       return convertView;
    }
}
