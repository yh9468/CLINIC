package com.example.younho.clinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class shop_take_activity extends AppCompatActivity {

    int many;
    int mYear, mMonth, mDay, mHour, mMinute;

    TextView title;
    ImageButton back_button;
    Button select_take_button, select_deliver_button, select_many_button, confirm_laundry_button;
    TextView show_take_view, show_deliver_view, show_many_view;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_shop_take);

        final DatePickerDialog.OnDateSetListener mDateSetListener_take =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //사용자가 입력한 값을 가져온뒤
                        mYear = year;
                        mMonth = monthOfYear +1;
                        mDay = dayOfMonth;

                        String string = show_take_view.getText().toString();
                        string = Integer.toString(mYear) +"년 " + Integer.toString(mMonth)+"월 " + Integer.toString(mDay) + "일" + string;
                        show_take_view.setText(string);
                        //텍스트뷰의 값을 업데이트함
                    }
                };

        final DatePickerDialog.OnDateSetListener mDateSetListener_deliver =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //사용자가 입력한 값을 가져온뒤
                        mYear = year;
                        mMonth = monthOfYear +1;
                        mDay = dayOfMonth;

                        String string = Integer.toString(mYear) +"년 " + Integer.toString(mMonth)+"월 " + Integer.toString(mDay) + "일";
                        show_deliver_view.setText(string);
                        //텍스트뷰의 값을 업데이트함
                    }
                };

        final TimePickerDialog.OnTimeSetListener mTimeSetListener_take =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        mHour = hourOfDay;
                        mMinute = minute;

                        String string = Integer.toString(mHour) + "시 "+ Integer.toString(mMinute) + "분";
                        show_take_view.setText(string);
                    }
                };
        final TimePickerDialog.OnTimeSetListener mTimeSetListener_deliver =
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        mHour = hourOfDay;
                        mMinute = minute;
                        String string = Integer.toString(mHour) + "시 "+ Integer.toString(mMinute) + "분";
                        string = show_deliver_view.getText().toString() + string;
                        show_deliver_view.setText(string);
                    }
                };

        select_take_button = findViewById(R.id.select_take_button);
        select_many_button = findViewById(R.id.select_many_button);
        select_deliver_button = findViewById(R.id.select_deliver_button);
        confirm_laundry_button = findViewById(R.id.confirm_laundry_button);
        select_take_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(shop_take_activity.this, mDateSetListener_take,2019, 12,18).show();
                new TimePickerDialog(shop_take_activity.this,mTimeSetListener_take , 10, 0,true).show();
            }
        });

        select_deliver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(shop_take_activity.this,mTimeSetListener_deliver , mHour, mMinute,false).show();
                new DatePickerDialog(shop_take_activity.this, mDateSetListener_deliver,mYear, mMonth,mDay).show();
            }
        });

        select_many_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        confirm_laundry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(shop_take_activity.this, "예약이 신청되었습니다!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        show_take_view = findViewById(R.id.show_take_view);
        show_deliver_view = findViewById(R.id.show_deliver_view);
        show_many_view = findViewById(R.id.show_many_view);




    }



    void show()
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("1");
        ListItems.add("2");
        ListItems.add("3");
        ListItems.add("4");
        ListItems.add("5");
        ListItems.add("6");
        ListItems.add("7");
        ListItems.add("8");
        ListItems.add("9");
        ListItems.add("10");



        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수량을 입력해 주세요");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                many = Integer.parseInt(selectedText);
                selectedText = selectedText + " 개";
                show_many_view.setText(selectedText);
            }
        });
        builder.show();
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
        title.setText("수거 신청");
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
