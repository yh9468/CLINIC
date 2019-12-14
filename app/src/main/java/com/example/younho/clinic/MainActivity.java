package com.example.younho.clinic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younho.clinic.model.Laundry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final int main_clean = 1000;
    private String REQUEST_URL = "http://9942b416.ngrok.io/abc/laundry/";

    public static Location picked_location = null;
    public static String picked_address = null;

    public static int distance = 500;
    public static final ArrayList<Laundry> Laundry_arr = new ArrayList<Laundry>();
    JSONArray jsonArray;

    ImageButton drawer_button;
    TextView first_title;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView mNavVIew;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //세탁소 정보 싹다 가져오기.
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


    public void  getJSON() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                try {
                    Log.d("URL", REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(4000);
                    httpURLConnection.setConnectTimeout(4000);
                    //httpURLConnection.setDoOutput(true);
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
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

                } catch (Exception e) {
                    result = e.toString();
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
