package com.kosmo.softlock;

import android.app.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mypage extends AppCompatActivity {

    ImageView btn_map;

    ImageView btn_after;
    ImageView btn_end;

    String mem_idx = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mem_idx = getIntent().getExtras().getString("mem_idx");
        Log.d("멤", mem_idx);


        btn_map = (ImageView)findViewById(R.id.btn_map);
        btn_after = (ImageView)findViewById(R.id.btn_after);
        btn_end = (ImageView)findViewById(R.id.btn_end);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        HpSearchMap.class);
                intent.putExtra("mem_idx", mem_idx);


                startActivity(intent);
            }
        });

        btn_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncHttpRequest2().execute(

                        "http://192.168.0.40:8080/softlock/Android/reservationlist"
                        , "mem_idx=" + mem_idx
                );
            }
        });



        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncHttpRequest().execute(

                        "http://192.168.0.40:8080/softlock/Android/reservationlist2"
                        , "mem_idx=" + mem_idx
                );
            }
        });






    }

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {
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
                // OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                    Log.i("AsyncTask Class", "HTTP_OK 됨");
                } else {
                    // 서버로부터 응답이 없는경우
                    Log.i("AsyncTask Class", "HTTP_OK 안됨");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }
            return sBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 진행대화창 닫기

            Log.d("야이따식아",s);
            // sBuffer를 SearchList로 넘김
            Intent intent = new Intent(getApplicationContext(), Myinfo.class);
            intent.putExtra("sBuffer", s);
            startActivity(intent);

        }
    }
    class AsyncHttpRequest2 extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {
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
                // OutputStream으로 파라미터를 전달하겠다는 설정
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                    Log.i("AsyncTask Class", "HTTP_OK 됨");
                } else {
                    // 서버로부터 응답이 없는경우
                    Log.i("AsyncTask Class", "HTTP_OK 안됨");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }
            return sBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 진행대화창 닫기

            Log.d("야이따식아",s);
            // sBuffer를 SearchList로 넘김
            Intent intent = new Intent(getApplicationContext(), Res.class);
            intent.putExtra("sBuffer", s);
            startActivity(intent);

        }
    }
}

