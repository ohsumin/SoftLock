package com.kosmo.softlock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = "KOSMO";

    private GoogleMap mMap;


    /////////////////////////////////////////////////////////////////////////////////////////////////
    private OAuthLoginButton naverLogInButton;
    private static OAuthLogin naverLoginInstance;
    Button btnGetApi, btnLogout;
    Button btn_login;
    static final String CLIENT_ID = "BzBQXoe0lFvJXPug54xl";
    static final String CLIENT_SECRET = "pNCmumc1Nz";
    static final String CLIENT_NAME = "임현주";
    // 회원 프로필정보
    String name = "";
    String email = "";
    String gender = "";
    String id = "";
    String pw = "";


    TextView tv_mail;
    TextView tv_name;
    TextView tv_gender;
    static Context context;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 네이버로 로그인하기
        init();
        init_View();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과보기 액티비티로 투표결과를 넘겨주기위해 인텐트 생성
                Intent intent = new Intent(v.getContext(),
                        Mypage.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void  onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng gasan = new LatLng(37.56, 126.97);
        mMap.addMarker(new MarkerOptions().position(gasan).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gasan));
    }


    //초기화
    private void init(){
        context = this;
        naverLoginInstance = OAuthLogin.getInstance();
        naverLoginInstance.init(this,CLIENT_ID,CLIENT_SECRET,CLIENT_NAME);
    }
    //변수 붙이기
    private void init_View(){
        naverLogInButton = (OAuthLoginButton)findViewById(R.id.buttonNaverLogin);
       /* name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        gender = getIntent().getExtras().getString("gender");
        if(gender.equals("F")){
            gender="w";
        }else if(gender.equals("M")){
            gender="m";
        }

        id = getIntent().getExtras().getString("id");*/

        //로그인 핸들러
        final OAuthLoginHandler naverLoginHandler  = new OAuthLoginHandler() {


            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공
                    // 회원 프로필정보 받기
                    new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
                   /* new AsyncHttpRequest().execute(
                            // 아이디, 성별, 이메일, 생년월일, 전화번호
                            "http://192.168.0.33:8080/softlock/Android/join"
                            , "mem_id=" + id
                            , "mem_pw=" + "NAVERLOGIN!"
                            , "mem_name=" + name
                            , "mem_gender=" + gender
                            , "mem_email=" + email
                            , "mem_auth=" + "y"

                    );*/

                } else {//로그인 실패
                    String errorCode = naverLoginInstance.getLastErrorCode(context).getCode();
                    String errorDesc = naverLoginInstance.getLastErrorDesc(context);
                    Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }

            }

        };

        /*dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("회원가입 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");*/


        naverLogInButton.setOAuthLoginHandler(naverLoginHandler);
        tv_mail = (TextView)findViewById(R.id.tv_mailaddress);
       tv_name = (TextView)findViewById(R.id.tv_name);
        tv_gender = (TextView)findViewById(R.id.tv_gender);
        btnGetApi = (Button)findViewById(R.id.btngetapi);
        btnGetApi.setOnClickListener(this);
        btnLogout = (Button)findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(this);
        btn_login = (Button)findViewById(R.id.btn_login);

        tv_mail.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_gender.setVisibility(View.GONE);
        btnGetApi.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        btn_login.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        /*if(v.getId() == R.id.btngetapi){
            new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
        }*/
        if(v.getId() == R.id.btnlogout){
            naverLoginInstance.logout(context);
            tv_mail.setText((String) "");//메일 란 비우기
            tv_name.setText((String) "");//이름 란 비우기
            tv_gender.setText((String) "");//성별 란 비우기
        }
    }




    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {//작업이 실행되기 전에 먼저 실행.
            tv_mail.setText((String) "");//메일 란 비우기
            tv_mail.setText((String) "");//이름 란 비우기
            tv_mail.setText((String) "");//성별 란 비우기
        }

        @Override
        protected String doInBackground(Void... params) {//네트워크에 연결하는 과정이 있으므로 다른 스레드에서 실행되어야 한다.
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = naverLoginInstance.getAccessToken(context);
            return naverLoginInstance.requestApi(context, at, url);//url, 토큰을 넘겨서 값을 받아온다.json 타입으로 받아진다.
        }

        protected void onPostExecute(String content) {//doInBackground 에서 리턴된 값이 여기로 들어온다.
            try {


                JSONObject jsonObject = new JSONObject(content);
                JSONObject response = jsonObject.getJSONObject("response");
                email = response.getString("email");
                name = response.getString("name");
                id = response.getString("id");
                gender = response.getString("gender");




                // 회원가입폼으로 화면전환1
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("name", name);
                intent.putExtra("gender", gender);
                intent.putExtra("email", email);
                intent.putExtra("id", id);
                startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }





}
