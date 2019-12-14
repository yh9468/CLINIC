package com.example.younho.clinic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younho.clinic.model.Laundry;
import com.example.younho.clinic.model.MyListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.GET;

public class CleanActivity extends AppCompatActivity {

    public final static int REQUEST_ACT = 1000;

    //listview 어뎁터 및 리스트뷰
    MyListAdapter adapter;
    ListView listView;
    ArrayList<Laundry> items;

    Button address_button;
    TextView title;
    ImageButton back_button;

    LatLng picked_latlng = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        Intent intent = new Intent(this.getIntent());
        listView = (ListView)findViewById(R.id.shop_list);

        items = new ArrayList<Laundry> ();
        adapter = new MyListAdapter(CleanActivity.this, items);
        listView.setAdapter(adapter);


        address_button = (Button) findViewById(R.id.select_address_btn);

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
            ArrayList<Laundry> laundries = getLaundries(MainActivity.picked_location);       //요게 얻은 세탁소들..
            items.addAll(laundries);
            adapter.notifyDataSetChanged();
            address_button.setText(MainActivity.picked_address);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                    intent1.putExtra("id", items.get(position).getId());
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

            ArrayList<Laundry> laundries = getLaundries(location_picked);       //요게 얻은 세탁소들..

            items.addAll(laundries);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                    intent1.putExtra("id", items.get(position).getId());
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
        title.setText("세탁물 배달 신청");
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

    public ArrayList<Laundry> getLaundries(Location location)
    {
        ArrayList<Laundry> laundries = new ArrayList<>();
        for(int i = 0 ; i<MainActivity.Laundry_arr.size() ; i++)
        {
            Laundry laundry = MainActivity.Laundry_arr.get(i);
            Location laundry_location = new Location("laundry_location");
            laundry_location.setLongitude(laundry.getLon());
            laundry_location.setLatitude(laundry.getLat());
            double distance = location.distanceTo(laundry_location);
            if(distance < MainActivity.distance)     //1000미터 이내.
            {
                laundries.add(laundry);
            }
        }

        return laundries;
    }

}
