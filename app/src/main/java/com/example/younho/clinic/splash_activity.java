package com.example.younho.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000);  //1초 이후에 hd handler 실행.
    }
    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class));     //로딩이 끝난 후, mainactivity로 넘어감.
            splash_activity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}
