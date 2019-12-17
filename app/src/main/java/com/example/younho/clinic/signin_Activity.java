package com.example.younho.clinic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.younho.clinic.model.Fix_shop;
import com.example.younho.clinic.model.Laundry;
import com.example.younho.clinic.model.MyUser;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class signin_Activity extends AppCompatActivity {
    private String common_URL = "http://2ce05764.ngrok.io";
    private String user_URL = "/abc/customer";
    private SessionCallback callback;      //콜백 선언
    Button btn_login;

    EditText user_id;
    EditText user_pw;


    public static MyUser curuser;
    public static ArrayList<MyUser> myUsers = new ArrayList<>();
    //유저프로필
    String token = "";
    String name = "";
    JSONArray jsonArray;

    final static String TAG = "LoginActivityT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signin_layout);


        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        user_id = findViewById(R.id.signin_id);
        user_pw = findViewById(R.id.signin_pw);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String id = user_id.getText().toString();
                String pw = user_pw.getText().toString();
                for(int i = 0 ; i<myUsers.size() ; i++)
                {
                    if(myUsers.get(i).getID().equals(id))
                    {
                        if(myUsers.get(i).getPW().equals(pw))
                        {
                            curuser = myUsers.get(i);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            break;
                    }
                }
            }
        });
        //키값 알아내기(알아냈으면 등록하고 지워도 상관없다)
        getAppKeyHash();

        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
        loadShared();
        if(myUsers.size() ==0)
            getJSON();
    }

    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.signin_layout); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(final UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.d(TAG, "유저가입성공");
                // Create a new user with a first and last name
                // 유저 카카오톡 아이디 디비에 넣음(첫가입인 경우에만 디비에저장)

                Map<String, String> user = new HashMap<>();
                user.put("token", userProfile.getId() + "");
                user.put("name", userProfile.getNickname());
                redirectHomeActivity(); // 로그인 성공시 MainActivity로

            }
        });
    }

    private void redirectHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, signin_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    /*쉐어드에 입력값 저장*/
    private void saveShared( String id, String name) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", id);
        editor.putString("name", name);
        editor.apply();
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getString("token", "");
        name = pref.getString("name", "");

    }


    public void  getJSON() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                String Customer_URL = common_URL + user_URL;
                try {
                    URL url = new URL(Customer_URL);
                    Log.d(TAG, Customer_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(4000);
                    httpURLConnection.setConnectTimeout(4000);

                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();

                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d(TAG, Integer.toString(responseStatusCode));

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
                    Log.d(TAG, "파싱전");

                }
                try {
                    jsonArray = new JSONArray(result);
                    for(int i = 0 ; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("ID");
                        String PW = jsonObject.getString("PW");
                        String Phone = jsonObject.getString("Phone");
                        Boolean IsMale = jsonObject.getBoolean("IsMale");
                        String BirthDate = jsonObject.getString("BirthDate");
                        String Email = jsonObject.getString("Email");
                        String FavoriteLaundry = jsonObject.getString("FavoriteLaundry");
                        String FavoriteRepair = jsonObject.getString("FavoriteRepair");
                        ArrayList<Integer> FavoriteLaundries = new ArrayList<>();
                        ArrayList<Integer> FavoriteRepairs = new ArrayList<>();

                        for(int k = 0 ; k<FavoriteLaundry.length(); k++)
                        {
                            if(k%2 == 0)
                            {
                                FavoriteLaundries.add(Integer.parseInt(String.valueOf(FavoriteLaundry.charAt(k))));
                            }
                        }
                        for(int k = 0 ; k<FavoriteRepair.length(); k++)
                        {
                            if(k%2 == 0)
                            {
                                FavoriteRepairs.add(Integer.parseInt(String.valueOf(FavoriteRepair.charAt(k))));
                            }
                        }


                        MyUser myUser = new MyUser(id,PW, Phone, IsMale,
                                BirthDate, Email, FavoriteLaundries, FavoriteRepairs);
                        myUsers.add(myUser);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
            }

        });
        thread.start();
    }
}
