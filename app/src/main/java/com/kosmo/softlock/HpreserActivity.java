package com.kosmo.softlock;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class HpreserActivity extends AppCompatActivity {

    int mYear, mMonth, mDay, mHour, mMinute;

    TextView textView2;
    Button button1;
    TextView mTxtDate;
    TextView hpname;
    TextView resv_date;
    TextView resv_week;
    TextView resv_time;
    EditText resv_symp;
    EditText resv_req;
    String resv_date_str;
    String resv_time_str;
    String resv_symp_str;
    String resv_req_str;

    String mem_idx="";
    String hp_idx="";
    String sBuffer = "";
    ProgressDialog dialog;
    String name = "";

    String monCl;
    String tueCl;
    String wenCl;
    String thuCl;
    String friCl;
    String satCl;
    String sunCl;
    String wek;


    RadioButton radio0;
    RadioButton radio1;
    RadioButton radio2;
    RadioButton radio3;
    RadioButton radio4;
    RadioButton radio5;
    RadioButton radio6;
    RadioButton radio7;
    RadioButton radio8;
    RadioButton radio9;
    RadioButton radio10;
    RadioButton radio11;

    int mon = 0;
    int tue = 1;
    int wen = 2;
    int thu = 3;
    int fri = 4;
    int sat = 5;
    int sun = 6;


    RadioGroup rg;
    int weekday;
    ArrayList<String> opentime;
    ArrayList<String> closetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpreser);
        resv_date = (TextView) findViewById(R.id.resv_date);
        resv_time = (TextView) findViewById(R.id.resv_time);
        resv_week = (TextView) findViewById(R.id.resv_week);
        resv_symp = (EditText) findViewById(R.id.resv_symp);
        resv_req = (EditText) findViewById(R.id.resv_req);




        mem_idx = getIntent().getExtras().getString("mem_idx");
        sBuffer = getIntent().getExtras().getString("sBuffer");

        opentime = getIntent().getStringArrayListExtra("opentime");
        closetime = getIntent().getStringArrayListExtra("closetime");

        //Log.d("시간", "onCreate: "+opentime.get(1));
       // Log.d("시간", "onCreate: "+closetime.get(1));
        Log.d("야", mem_idx);
        Log.d("야2", sBuffer);

        monCl = closetime.get(0);
        tueCl = closetime.get(1);
        wenCl = closetime.get(2);
        thuCl = closetime.get(3);
        friCl = closetime.get(4);
        satCl = closetime.get(5);
        sunCl = closetime.get(6);

        radio0 = (RadioButton)findViewById(R.id.radio0);
        radio1 = (RadioButton)findViewById(R.id.radio0);
        radio2 = (RadioButton)findViewById(R.id.radio0);
        radio3 = (RadioButton)findViewById(R.id.radio0);
        radio4 = (RadioButton)findViewById(R.id.radio0);
        radio5 = (RadioButton)findViewById(R.id.radio0);
        radio6 = (RadioButton)findViewById(R.id.radio0);
        radio7 = (RadioButton)findViewById(R.id.radio0);
        radio8 = (RadioButton)findViewById(R.id.radio0);
        radio9 = (RadioButton)findViewById(R.id.radio0);
        radio10 = (RadioButton)findViewById(R.id.radio0);
        radio11 = (RadioButton)findViewById(R.id.radio0);

         RadioButton[] rButtons = {radio0, radio1,radio2,radio3, radio4, radio5, radio6,radio7,radio8, radio9,radio10, radio11};

        wek = resv_week.getText().toString();

        //시간 라디오버튼
        rg = (RadioGroup) findViewById(R.id.spinner1);

        if(wek == "0" ){
            for(int i =0; i<=rButtons.length; i++){
                monCl.equals(rButtons[i].getText());

                if((monCl != rButtons[i].getText().toString())){

                    rButtons[i].setEnabled(false);

                }
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio_btn = (RadioButton) findViewById(checkedId);
                resv_time.setText(((RadioButton) findViewById(checkedId)).getText());

            }
        });

        //라디오버튼 값 가져오기

        RadioGroup rg = (RadioGroup)findViewById(R.id.spinner1); // 라디오그룹 객체 맵핑
        RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId()); // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑

       // String selectedValue = selectedRdo.getText().toString(); // 해당 라디오버튼 객체의 값 가져오기
       // Log.d("selectValue", selectedValue);

        try {
            JSONObject jsonObject = new JSONObject(sBuffer.toString());
            JSONObject jsonObject2 = jsonObject.getJSONObject("HospitalDTO");
            name = jsonObject2.getString("hp_name");
            hp_idx= jsonObject2.getString("hp_idx");


            hpname = (TextView) findViewById(R.id.hpname);
            ((TextView) hpname).setText(name);

        } catch (Exception e) {
            e.printStackTrace();
        }
            //텍스트뷰 2개 연결
            mTxtDate = (TextView) findViewById(R.id.resv_date);


            //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
            Calendar cal = new GregorianCalendar();
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
            mHour = cal.get(Calendar.HOUR_OF_DAY);
            weekday = cal.get(Calendar.DAY_OF_WEEK);
            resv_week.setText(String.valueOf(weekday));
            Log.d("weekday",String.valueOf(weekday));


            UpdateNow();//화면에 텍스트뷰에 업데이트 해줌.
            final TextView tv = (TextView) findViewById(R.id.resv_time);
           /* Spinner s = (Spinner) findViewById(R.id.spinner1);

            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tv.setText(" " + parent.getItemAtPosition(position));

               *//* String.format("%d:%d", parent.getItemAtPosition(position),

                        mMonth + 1, mDay)*//*

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });*/


            //버튼가져오기
            Button resvBtn = (Button) findViewById(R.id.resvBtn);
            //버튼누르면 http통신
            resvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    resv_req_str = resv_req.getText().toString();
                    resv_symp_str = resv_symp.getText().toString();
                    resv_time_str = resv_time.getText().toString();
                    resv_date_str = resv_date.getText().toString();

                    new AsyncHttpRequest().execute(
                            // 아이디, 성별, 이메일, 생년월일, 전화번호
                            "http://192.168.0.33:8080/softlock/Android/reservationAction"
                            , "hp_idx=" + hp_idx
                            , "mem_idx=" + mem_idx
                            , "resv_req=" + resv_req_str
                            , "resv_symp=" + resv_symp_str
                            , "resv_date=" + resv_date_str
                            , "resv_time=" + resv_time_str
                    );


                }
            });//infoBtn.onClick();

            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.setTitle("예약 처리중");
            dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");







        }






        public void mOnClick (View v){

            switch (v.getId()) {

                //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌

                case R.id.btnchangedate:

                    //여기서 리스너도 등록함

                    new DatePickerDialog(HpreserActivity.this, mDateSetListener, mYear,

                            mMonth, mDay).show();

                    break;


            }

        }








        //날짜 대화상자 리스너 부분

        DatePickerDialog.OnDateSetListener mDateSetListener =

                new DatePickerDialog.OnDateSetListener() {


                    @Override

                    public void onDateSet(DatePicker view, int year, int monthOfYear,

                                          int dayOfMonth) {

                        // TODO Auto-generated method stub

                        //사용자가 입력한 값을 가져온뒤

                        mYear = year;

                        mMonth = monthOfYear;

                        mDay = dayOfMonth;

                       // timeSpinner();

                        //텍스트뷰의 값을 업데이트함

                        UpdateNow();



                    }

                };


        /*//시간 대화상자 리스너 부분

        TimePickerDialog.OnTimeSetListener mTimeSetListener =

                new TimePickerDialog.OnTimeSetListener() {


                    @Override

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        // TODO Auto-generated method stub

                        //사용자가 입력한 값을 가져온뒤

                        mHour = hourOfDay;


                        //텍스트뷰의 값을 업데이트함

                        UpdateNow();


                    }

                };*/


        //텍스트뷰의 값을 업데이트 하는 메소드

        void UpdateNow () {

            mTxtDate.setText(String.format("%d/%d/%d", mYear,

                    mMonth + 1, mDay ));


        }


        class AsyncHttpRequest extends AsyncTask<String, Void, String> {
            // doInBackground 함수 호출전에 미리 호출하는 메소드
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!dialog.isShowing()) {
                    dialog.show();
                }

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
                    //connection.setDoInput(true);

                /*
                요청 파라미터를 OutputStream으로 조립후 전달한다.
                - 파라미터는 쿼리스트링 형태로 지정한다.
                - 한국어를 전송하는 경우에는 URLEncode를 해야한다.
                - 아래와 같이 처리하면 Request Body에 데이터를 담을 수 있다.
                 */
                    Log.i("얍얍", "얍1");
                    OutputStream out = connection.getOutputStream();
                    Log.i("얍얍", "얍2");
                    out.write(params[1].getBytes());
                    out.write("&".getBytes());
                    out.write(params[2].getBytes());
                    out.write("&".getBytes());
                    out.write(params[3].getBytes());
                    out.write("&".getBytes());
                    out.write(params[4].getBytes());
                    out.write("&".getBytes());
                    out.write(params[5].getBytes());
                    out.write("&".getBytes());
                    out.write(params[6].getBytes());
                /*out.write(params[7].getBytes());
                out.write("&".getBytes());
                out.write(params[8].getBytes());
                out.write("&".getBytes());
                out.write(params[9].getBytes());
                out.write("&".getBytes());
                out.write(params[10].getBytes());*/

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


                /*try {
                    JSONObject jsonObject = new JSONObject(sBuffer.toString());
                    mem_idx = jsonObject.getString("mem_idx");
                *//*for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    ;
                    //Log.d("성공여부", mem_idx);
                    //Toast.makeText(getApplicationContext(), isSuccess, Toast.LENGTH_LONG).show();
                }*//*
                } catch (Exception e) {
                    e.printStackTrace();

                }*/

                return sBuffer.toString();
            }

            /*
            doInBackground메소드가 정상적으로 완료되면 onPostExecute()실행.
            onPostExecute메소드가 doInBackground의 리턴값을 받음.
             */
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                dialog.dismiss();
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
