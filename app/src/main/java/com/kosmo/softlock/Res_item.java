package com.kosmo.softlock;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Res_item extends LinearLayout   {

    String resv_idxstr;

    //TextView hpType;
    TextView hpName;
    TextView resvDate;
    TextView resvTime;
    Button cancel_reserv;
    TextView resvPerm;
    EditText resv_idx;
    LinearLayout noneed;

    public Res_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_res_item, this, true);

        //resv_idx = new Res().resv_idx;
        //resv_idx = intent.getExtras().getString("resv_idx");
        //Log.d("히힛", resv_idx);

        //hpType = (TextView) findViewById(R.id.hpType);
        hpName = (TextView) findViewById(R.id.hpName);
        resvDate = (TextView) findViewById(R.id.resvDate);
        resvTime = (TextView) findViewById(R.id.resvTime);
        resvPerm = (TextView) findViewById(R.id.resvPerm);
        cancel_reserv = (Button) findViewById(R.id.cancel_reserv);
        resv_idx = (EditText) findViewById(R.id.resv_idx);
        noneed = (LinearLayout) findViewById(R.id.noneed);

        noneed.setVisibility(View.GONE);

        cancel_reserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resv_idxstr = resv_idx.getText().toString();

                new AsyncHttpRequest3().execute(
                     "http://192.168.0.38:8080/softlock/Android/reserdelete"
                        , "resv_idx=" + resv_idxstr

                );



                Toast.makeText(getContext(), "예약이 취소되었습니다.",
                        Toast.LENGTH_SHORT).show();



            }
        });

    }


       // public void setHpType(String text){
           // hpType.setText(text);
        //}
        public void setHpName(String text){
            hpName.setText(text);
        }
        public void setResvDate(String text){
            resvDate.setText(text);
        }
        public void setResvTime(String text){
            resvTime.setText(text);
        }
        public void setResvPerm(String text){
            resvPerm.setText(text);
        }
        public void setResv_idx(String text) { resv_idx.setText(text);  }

}

class AsyncHttpRequest3 extends AsyncTask<String, Void, String> {
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


    }
}