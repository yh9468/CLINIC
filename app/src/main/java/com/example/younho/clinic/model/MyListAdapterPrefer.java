package com.example.younho.clinic.model;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.younho.clinic.R;

import java.util.ArrayList;
import java.util.Random;

public class MyListAdapterPrefer extends BaseAdapter {

    private Context context;
    private ArrayList<Laundry> list_item_Array_List;
    ImageView imageView;
    TextView name;
    TextView address;

    public MyListAdapterPrefer(Context context, ArrayList<Laundry> list_item_Array_List)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_prefer, null);
        }
        if(list_item_Array_List.size() ==0)
            return convertView;

        imageView = (ImageView) convertView.findViewById(R.id.Laundry_Image_prefer);
        name = (TextView)convertView.findViewById(R.id.Laundry_Name_prefer);
        address = (TextView) convertView.findViewById(R.id.Laundry_Address_prefer);

        Laundry shop = list_item_Array_List.get(position);
        if(shop.getName().contains("크린토피아"))
            imageView.setImageResource(R.drawable.shop_no_1);
        else if(shop.getName().contains("워시엔조이"))
            imageView.setImageResource(R.drawable.shop_no_2);
        else if(shop.getName().contains("펭귄하우스"))
            imageView.setImageResource(R.drawable.shop_no_3);
        else
            imageView.setImageResource(R.drawable.shop_button_icon);

        name.setText(list_item_Array_List.get(position).getName());
        address.setText(list_item_Array_List.get(position).getAddress());


        return convertView;
    }
}
