package com.example.younho.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.younho.clinic.model.Fix_shop;
import com.example.younho.clinic.model.Laundry;
import com.example.younho.clinic.model.MyListAdapter;
import com.example.younho.clinic.model.MyListAdapterList_Fix_Prefer;
import com.example.younho.clinic.model.MyListAdapterPrefer;

import java.util.ArrayList;

public class history_activity extends AppCompatActivity {

    MyListAdapterPrefer adapter;
    ListView listView;
    ArrayList<Laundry> items;

    MyListAdapterList_Fix_Prefer adapter_fix;
    ListView listView_fix;
    ArrayList<Fix_shop> items_fix;

    TextView title;
    ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        listView_fix = (ListView) findViewById(R.id.prefer_fix_shop_list);
        listView = (ListView)findViewById(R.id.prefer_shop_list);

        items_fix = new ArrayList<>();
        items = new ArrayList<Laundry> ();

        adapter_fix = new MyListAdapterList_Fix_Prefer(history_activity.this, items_fix);
        adapter = new MyListAdapterPrefer(history_activity.this, items);

        listView.setAdapter(adapter);
        listView_fix.setAdapter(adapter_fix);

        items.addAll(MainActivity.prefer_arr);
        items_fix.addAll(MainActivity.prefer_fix_arr);

        adapter.notifyDataSetChanged();
        adapter_fix.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                intent1.putExtra("id", items.get(position).getId());
                intent1.putExtra("is_clean",true);
                startActivity(intent1);
            }
        });

        listView_fix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(getApplicationContext(), shop_detail_activity.class);
                intent1.putExtra("id", items_fix.get(position).getId());
                intent1.putExtra("is_clean",false);
                startActivity(intent1);
            }
        });



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
        title.setText("세탁 시켰던 기록");
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
}
