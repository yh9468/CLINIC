package com.example.younho.clinic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younho.clinic.model.Fix_shop;
import com.example.younho.clinic.model.Laundry;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static final int main_clean = 1000;
    private String common_URL = "http://2ce05764.ngrok.io";
    private String Laundry_URL = common_URL + "/abc/laundry/";
    private String Fixshop_URL = common_URL + "/abc/repair";


    public static Location picked_location = null;
    public static String picked_address = null;

    public static int distance = 500;
    public static final ArrayList<Laundry> Laundry_arr = new ArrayList<Laundry>();
    public static final ArrayList<Laundry> prefer_arr = new ArrayList<>();
    public static final ArrayList<Fix_shop> Fix_arr = new ArrayList<>();
    public static final ArrayList<Fix_shop> prefer_fix_arr = new ArrayList<>();

    JSONArray jsonArray;

    ImageButton drawer_button;
    TextView first_title;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView mNavVIew;
    Toolbar toolbar;

    Button logout_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppKeyHash();

        logout_btn = findViewById(R.id.Logout_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CLINIC");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        drawerLayout = findViewById(R.id.drawer_layout);
        mNavVIew = (NavigationView) findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        mNavVIew.setNavigationItemSelectedListener(this);

        Button cleanbutton = (Button) findViewById(R.id.clean_button);
        cleanbutton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), CleanActivity.class);
                //intent.putExtra()
                startActivity(intent);
            }
        });

        Button fixbutton = (Button) findViewById(R.id.fix_button);
        fixbutton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), FixActivity.class);
                startActivity(intent);
            }
        });

        Button shopbutton = (Button) findViewById(R.id.shop_button);
        shopbutton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });


        logout_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(getApplicationContext(), signin_Activity.class);
                        signin_Activity.curuser = null;
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        //세탁소 정보 싹다 가져오기.
        if(Laundry_arr.size() == 0)
            getJSON();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), private_data_Activity.class);
                startActivity(intent);
                break;
            case R.id.nav_listview:
                Intent intent1 = new Intent(getApplicationContext(), history_activity.class);
                startActivity(intent1);
                break;
            case R.id.nav_radius:
                show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    void show()
    {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("250");
        ListItems.add("500");
        ListItems.add("1000");
        ListItems.add("1500");

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("찾으실 범위를 설정해 주세요");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                distance = Integer.parseInt(selectedText);
                Toast.makeText(MainActivity.this, selectedText + "으로 설정되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    // 프로젝트의 해시키를 반환

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    public void  getJSON() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                String result_fix;
                try {
                    URL url = new URL(Laundry_URL);
                    URL url_fix = new URL(Fixshop_URL);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    HttpURLConnection httpURLConnection_fix = (HttpURLConnection) url_fix.openConnection();

                    httpURLConnection.setReadTimeout(4000);
                    httpURLConnection.setConnectTimeout(4000);

                    httpURLConnection_fix.setReadTimeout(4000);
                    httpURLConnection_fix.setConnectTimeout(4000);

                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection_fix.setRequestMethod("GET");

                    httpURLConnection.setUseCaches(false);
                    httpURLConnection_fix.setUseCaches(false);

                    httpURLConnection.connect();
                    int responseStatusCode = httpURLConnection.getResponseCode();
                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    result = sb.toString().trim();

                    //여기서부터는 수선소

                    httpURLConnection_fix.connect();
                    int responseStatusCode_fix = httpURLConnection_fix.getResponseCode();
                    InputStream inputStream_fix;
                    if (responseStatusCode_fix == HttpURLConnection.HTTP_OK) {
                        inputStream_fix = httpURLConnection_fix.getInputStream();
                    } else {
                        inputStream_fix = httpURLConnection_fix.getErrorStream();
                    }

                    InputStreamReader inputStreamReader_fix = new InputStreamReader(inputStream_fix, "UTF-8");
                    BufferedReader bufferedReader_fix = new BufferedReader(inputStreamReader_fix);

                    StringBuilder sb_fix = new StringBuilder();
                    String line_fix;

                    while ((line_fix = bufferedReader_fix.readLine()) != null) {
                        sb_fix.append(line_fix);
                    }
                    bufferedReader_fix.close();
                    httpURLConnection_fix.disconnect();
                    result_fix = sb_fix.toString().trim();

                } catch (Exception e) {
                    result = e.toString();
                    result_fix = e.toString();
                }
                try {
                    jsonArray = new JSONArray(result);
                    for(int i = 0 ; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String Name = jsonObject.getString("Name");
                        double Lat = jsonObject.getDouble("Latitude");
                        double Lon = jsonObject.getDouble("Longitude");
                        String Address = jsonObject.getString("Address");
                        Laundry laundry = new Laundry(id, Name,Lat,Lon,Address);
                        Laundry_arr.add(laundry);
                        if(id == 1 || id ==4 || id == 5)
                        {
                            prefer_arr.add(laundry);
                        }

                    }

                    jsonArray = new JSONArray(result_fix);
                    for(int i = 0 ; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String Name = jsonObject.getString("Name");
                        double Lat = jsonObject.getDouble("Latitude");
                        double Lon = jsonObject.getDouble("Longitude");
                        String Address = jsonObject.getString("Address");
                        Fix_shop fix_shop = new Fix_shop(id, Name,Lat,Lon,Address);
                        Fix_arr.add(fix_shop);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }
}
