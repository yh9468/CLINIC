package com.example.younho.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.younho.clinic.model.Cloth;
import com.example.younho.clinic.model.Fix_shop;
import com.example.younho.clinic.model.Laundry;
import com.example.younho.clinic.model.MyListAdapter_Cloth;

import java.util.ArrayList;

public class shop_detail_activity extends AppCompatActivity {

    int shop_id;
    boolean is_clean;
    Laundry shop;
    Fix_shop fix_shop;

    TextView shop_name;
    TextView shop_address;
    CircleImageView shop_image;
    ListView listView;
    ArrayList<Cloth> items;
    MyListAdapter_Cloth adapter;
    Button deliver_button;
    ImageButton like_btn;

    TextView title;
    ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_layout);
        Intent intent = new Intent(this.getIntent());
        shop_id = intent.getIntExtra("id", 0);
        is_clean = intent.getBooleanExtra("is_clean",true);
        like_btn = findViewById(R.id.like_button);

        //세탁소인 경우
        if(is_clean) {
            if (shop_id != 0) {
                for (int i = 0; i < MainActivity.Laundry_arr.size(); i++) {
                    if (shop_id == MainActivity.Laundry_arr.get(i).getId()) {
                        shop = MainActivity.Laundry_arr.get(i);
                        break;
                    }
                }
            }

            if(MainActivity.prefer_arr.contains(shop))
            {
                like_btn.setSelected(true);
            }
            else
                like_btn.setSelected(false);

            like_btn.setOnClickListener(new ImageButton.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(MainActivity.prefer_arr.contains(shop)) {
                        like_btn.setSelected(!like_btn.isSelected());
                        MainActivity.prefer_arr.remove(shop);
                    }
                    else {
                        like_btn.setSelected(!like_btn.isSelected());
                        MainActivity.prefer_arr.add(shop);
                    }
                }
            });

            listView = findViewById(R.id.price_detail);
            shop_image = findViewById(R.id.shop_image);
            shop_name = findViewById(R.id.shop_name);
            shop_address = findViewById(R.id.shop_address);
            deliver_button = findViewById(R.id.deliver_button);
            deliver_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), shop_take_activity.class);
                    //intent.putExtra()
                    startActivity(intent);
                }
            });

            shop_name.setText(shop.getName());
            shop_address.setText(shop.getAddress());
            if (shop.getName().contains("크린토피아"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_1, getApplicationContext().getTheme()));
            else if (shop.getName().contains("워시엔조이"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_2, getApplicationContext().getTheme()));
            else if (shop.getName().contains("펭귄하우스"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_3, getApplicationContext().getTheme()));
            else
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_button_icon, getApplicationContext().getTheme()));

            items = new ArrayList<Cloth>();
            adapter = new MyListAdapter_Cloth(shop_detail_activity.this, items);
            listView.setAdapter(adapter);

            items.add(new Cloth("와이셔츠", 5000));
            items.add(new Cloth("패딩", 20000));
            items.add(new Cloth("가디건", 15000));
            items.add(new Cloth("니트", 15000));
            items.add(new Cloth("자켓", 15000));
            items.add(new Cloth("이불", 40000));
            items.add(new Cloth("운동화", 20000));
            items.add(new Cloth("바지", 6000));
            adapter.notifyDataSetChanged();
        }




        //수선소인 경우.
        else
        {
            if (shop_id != 0) {
                for (int i = 0; i < MainActivity.Fix_arr.size(); i++) {
                    if (shop_id == MainActivity.Fix_arr.get(i).getId()) {
                        fix_shop = MainActivity.Fix_arr.get(i);
                        break;
                    }
                }
            }

            if(MainActivity.prefer_fix_arr.contains(fix_shop))
            {
                like_btn.setSelected(true);
            }
            else
                like_btn.setSelected(false);


            like_btn.setOnClickListener(new ImageButton.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(MainActivity.prefer_fix_arr.contains(fix_shop)) {
                        like_btn.setSelected(!like_btn.isSelected());
                        MainActivity.prefer_fix_arr.remove(fix_shop);

                    }
                    else {
                        like_btn.setSelected(!like_btn.isSelected());
                        MainActivity.prefer_fix_arr.add(fix_shop);
                    }
                }
            });

            listView = findViewById(R.id.price_detail);
            shop_image = findViewById(R.id.shop_image);
            shop_name = findViewById(R.id.shop_name);
            shop_address = findViewById(R.id.shop_address);
            deliver_button = findViewById(R.id.deliver_button);
            deliver_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), shop_take_activity.class);
                    //intent.putExtra()
                    startActivity(intent);
                }
            });

            shop_name.setText(fix_shop.getName());
            shop_address.setText(fix_shop.getAddress());

            if (fix_shop.getName().contains("크린토피아"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_1, getApplicationContext().getTheme()));
            else if (fix_shop.getName().contains("워시엔조이"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_2, getApplicationContext().getTheme()));
            else if (fix_shop.getName().contains("펭귄하우스"))
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_no_3, getApplicationContext().getTheme()));
            else
                shop_image.setImageDrawable(getResources().getDrawable(R.drawable.shop_button_icon, getApplicationContext().getTheme()));

            items = new ArrayList<Cloth>();
            adapter = new MyListAdapter_Cloth(shop_detail_activity.this, items);
            listView.setAdapter(adapter);

            items.add(new Cloth("자켓 어깨", 80000));
            items.add(new Cloth("자켓 어깨핸드", 90000));
            items.add(new Cloth("자켓 총기장", 50000));
            items.add(new Cloth("자켓 품", 50000));
            items.add(new Cloth("자켓 소매기장", 50000));
            items.add(new Cloth("자켓 소매손자수", 50000));
            items.add(new Cloth("자켓 앞품", 40000));
            items.add(new Cloth("자켓 안감교체", 200000));
            items.add(new Cloth("코트 어깨", 90000));
            items.add(new Cloth("코트 품", 70000));
            items.add(new Cloth("코트 총기장", 60000));
            items.add(new Cloth("코트 소매기장", 50000));
            items.add(new Cloth("니트 총기장", 100000));
            items.add(new Cloth("니트 전체품", 40000));
            items.add(new Cloth("니트 소매통", 40000));
            items.add(new Cloth("바지 허리,힙", 50000));
            items.add(new Cloth("바지 지퍼교체", 50000));
            items.add(new Cloth("바지 밑통", 30000));
            items.add(new Cloth("바지 기장", 10000));
            adapter.notifyDataSetChanged();
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
        title.setText("가게 정보");
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
