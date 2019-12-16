package com.example.younho.clinic;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.younho.clinic.model.Fix_shop;
import com.example.younho.clinic.model.MyListAdapter_Fix;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FixActivity  extends AppCompatActivity {
    public final static int REQUEST_ACT = 1001;

    //listview 어뎁터 및 리스트뷰
    MyListAdapter_Fix adapter;
    ListView listView;
    ArrayList<Fix_shop> items;

    Button address_button;
    TextView title;
    ImageButton back_button;

    LatLng picked_latlng = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);
        Intent intent = new Intent(this.getIntent());
        listView = (ListView)findViewById(R.id.fix_shop_list);

        items = new ArrayList<Fix_shop> ();
        adapter = new MyListAdapter_Fix(FixActivity.this, items);
        listView.setAdapter(adapter);


        address_button = (Button) findViewById(R.id.select_fix_address_btn);

        address_button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), Select_address_Activity.class);
                //intent.putExtra()
                startActivityForResult(intent,REQUEST_ACT);
            }
        });

        if(MainActivity.picked_location != null) {
            ArrayList<Fix_shop> Fix_shops = getFixshops(MainActivity.picked_location);       //요게 얻은 세탁소들..
            items.addAll(Fix_shops);
            adapter.notifyDataSetChanged();
            address_button.setText(MainActivity.picked_address);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                    intent1.putExtra("id", items.get(position).getId());
                    intent1.putExtra("is_clean", false);
                    startActivity(intent1);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_ACT) {
            String Address = data.getStringExtra("Address");
            double Lon = data.getDoubleExtra("Lon",0);
            double Lat = data.getDoubleExtra("Lat", 0);
            address_button.setText(Address);
            Location location_picked = new Location("picked");
            location_picked.setLatitude(Lat);
            location_picked.setLongitude(Lon);
            MainActivity.picked_address = Address;
            MainActivity.picked_location = location_picked;         //Main에서 유지하도록 설정.

            ArrayList<Fix_shop> Fix_shops = getFixshops(location_picked);       //요게 얻은 세탁소들..

            items.addAll(Fix_shops);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                    intent1.putExtra("id", items.get(position).getId());
                    intent1.putExtra("is_clean", false);
                    startActivity(intent1);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        title = findViewById(R.id.title_bar_text);
        title.setText("수선물 배달 신청");
        back_button = (ImageButton) findViewById(R.id.btnBack);
        back_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        return true;
    }

    public ArrayList<Fix_shop> getFixshops(Location location)
    {
        ArrayList<Fix_shop> fix_shops = new ArrayList<>();
        for(int i = 0 ; i<MainActivity.Fix_arr.size() ; i++)
        {
            Fix_shop fix_shop = MainActivity.Fix_arr.get(i);
            Location fix_location = new Location("fix_location");
            fix_location.setLongitude(fix_shop.getLon());
            fix_location.setLatitude(fix_shop.getLat());
            double distance = location.distanceTo(fix_location);
            if(distance < MainActivity.distance)     //1000미터 이내.
            {
                fix_shops.add(fix_shop);
            }
        }

        return fix_shops;
    }

}
