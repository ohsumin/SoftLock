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

        //로그인 핸들러
        final OAuthLoginHandler naverLoginHandler  = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {//로그인 성공
                    // 회원 프로필정보 받기
                    new RequestApiTask().execute();//static이 아니므로 클래스 만들어서 시행.
                    /*new AsyncHttpRequest().execute(
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
                String mem_idx = "";

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

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 서버로 요청하는 시점에 프로그레스 대화창을 띄워준다.
           // if (!dialog.isShowing()) {
              //  dialog.show();
            //}
        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {

            String mem_idx = "";
            // execute()를 호출할때 전달한 3개의 파라미터를 가변인자로 전달받는다.
            // 함수 내부에서는 배열로 사용한다.

            // 파라미터 확인용
            for (String s : params) {
                Log.i("AsyncTask Class", "파라미터 : " + s);
            }

            // 서버의 응답데이터를 저장할 변수(디버깅용)
            StringBuffer sBuffer = new StringBuffer();

            try {
                // 요청주소로 URL객체 생성
                URL url = new URL(params[0]);
                // 위 참조변수로 URL연결
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 전송방식은 POST로 설정한다. (디폴트는 GET방식)
                connection.setRequestMethod("POST");
                //connection.setRequestMethod("");
                // OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);
                connection.setDoInput(true);

                /*
                요청 파라미터를 OutputStream으로 조립후 전달한다.
                - 파라미터는 쿼리스트링 형태로 지정한다.
                - 한국어를 전송하는 경우에는 URLEncode를 해야한다.
                - 아래와 같이 처리하면 Request Body에 데이터를 담을 수 있다.
                 */
                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                //out.write("&".getBytes());
                out.write(params[2].getBytes());
                //out.write("&".getBytes());
                out.write(params[3].getBytes());
                //out.write("&".getBytes());
                out.write(params[4].getBytes());
                //out.write("&".getBytes());
                out.write(params[5].getBytes());
                //out.write("&".getBytes());
                out.write(params[6].getBytes());
                //out.write("&".getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    Log.i("AsyncTask Class", "HTTP_OK 대씸");


                    // 서버로부터 응답이 온 경우
                    // 응답데이터를 StringBuffer변수에 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8")
                    );
                    String responseData;
                    while ((responseData = reader.readLine()) != null) {
                        sBuffer.append(responseData + "\n\r");
                    }
                    reader.close();
                } else {
                    // 서버로부터 응답이 없는경우
                    Log.i("AsyncTask Class", "HTTP_OK 안대씸");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 눌러진 버튼이 로그인이면 파싱후 결과를 출력함
            /*
            [{"pass":"1234","regidate":2018-11-20,"name":"오수민","id":"test1"}, ... ]

             */


            try {
                JSONObject jsonObject = new JSONObject(sBuffer.toString());
                mem_idx = jsonObject.getString("mem_idx");
                /*for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    ;
                    //Log.d("성공여부", mem_idx);
                    //Toast.makeText(getApplicationContext(), isSuccess, Toast.LENGTH_LONG).show();
                }*/
            } catch (Exception e) {
                e.printStackTrace();

            }
            Log.d("맴", mem_idx);
            return mem_idx;
        }

        /*
        doInBackground메소드가 정상적으로 완료되면 onPostExecute()실행.
        onPostExecute메소드가 doInBackground의 리턴값을 받음.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 진행대화창 닫기
            dialog.dismiss();
            // 서버의 응답데이터 파싱후 텍스트뷰에 출력
            //textResult.setText(s);
            //Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Mypage.class);
            intent.putExtra("mem_idx", s);
            startActivity(intent);
        }
    }



}
