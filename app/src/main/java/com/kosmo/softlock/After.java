package com.kosmo.softlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class After extends AppCompatActivity {


    ProgressDialog dialog;
    Button hp_after;

    String  mem_idx="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after);

        mem_idx = getIntent().getExtras().getString("mem_idx");

        Log.d("밈", mem_idx);

        hp_after = (Button)findViewById(R.id.hp_after);

        hp_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncHttpRequest().execute(
                        // 아이디, 성별, 이메일, 생년월일, 전화번호
                        "http://192.168.0.40:8080/softlock/Android/reservationlist"
                        , "mem_idx=" + mem_idx
                );

            }
        });

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("회원가입 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");

    }//onCreate

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 서버로 요청하는 시점에 프로그레스 대화창을 띄워준다.
            if (!dialog.isShowing()) {
                dialog.show();
            }
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

        /*
        doInBackground메소드가 정상적으로 완료되면 onPostExecute()실행.
        onPostExecute메소드가 doInBackground의 리턴값을 받음.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 진행대화창 닫기
            dialog.dismiss();
            Log.d("야이따식아",s);
            // sBuffer를 SearchList로 넘김
            Intent intent = new Intent(getApplicationContext(), Res.class);
            intent.putExtra("sBuffer", s);
            startActivity(intent);

        }
    }
}
