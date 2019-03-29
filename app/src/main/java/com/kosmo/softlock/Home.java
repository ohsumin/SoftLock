package com.kosmo.softlock;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/*import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;*/


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

//import com.facebook.accountkit.AccountKit;
//import com.facebook.accountkit.AccessToken;

public class Home extends AppCompatActivity {
    //Logcat을 사용하기 위한 태그 설정
    private static final int REQUEST_CODE = 999;

    String userPhone;//유심에서 전화번호 읽어와서 입력폼에 자동 입력시키기 위한 변수이다

    EditText etPhone;
    EditText year;
    Spinner sp_month;
    Spinner sp_day;
    String yearstr;
    String sp_month_str;
    String sp_day_str;
    String etPhonestr;
    String frontstr;

    TextView textView1;
    TextView textView2;
    TextView textView3;
    Button btnPhone, btnEmail;
    Button btnJoin;
    //클래스 전체에서 사용하기 위한 전역변수 및 객체선언
    Calendar calendar;//켈린더 클래스(시간, 날짜생성)
    TextView date_tv, time_tv;//텍스트뷰(시간, 날짜 표시)
    int yearStr, monthStr, dayStr;//현재 날짜
    int hourStr, minuteStr, secondStr;//현재시간

    // 회원 프로필정보
    String name = "";
    String email = "";
    String gender = "";
    String id = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        gender = getIntent().getExtras().getString("gender");
        if(gender.equals("F")){
            gender="w";
        }else if(gender.equals("M")){
            gender="m";
        }

        id = getIntent().getExtras().getString("id");
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();


        /*btnEmail = (Button) findViewById(R.id.emailLogin);
        btnPhone = (Button) findViewById(R.id.phoneLogin);*/
        btnJoin = (Button) findViewById(R.id.btnJoin);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView3 = (TextView) findViewById(R.id.textView3);
        year = (EditText) findViewById(R.id.year);
        sp_day = (Spinner) findViewById(R.id.sp_day);
        sp_month = (Spinner) findViewById(R.id.sp_month);
        etPhone = (EditText) findViewById(R.id.etPhone);




        // 전화번호 가져오기
        /*int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {     // 권한설정 안되어 있으면 권한요청
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
        } else {    // 권한이 있으면
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            userPhone = mgr.getLine1Number();//mgr.getLine1Number();
            userPhone = userPhone.replace("+82", "0");

            etPhone.setText(userPhone);
            etPhone.setEnabled(false);
        }*/



        //켈린더 객체 생성(생성자가 없는 클래스는 getInstance()로 객체생성)
        calendar = Calendar.getInstance();







       /* btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //결과보기 액티비티로 투표결과를 넘겨주기위해 인텐트 생성
                Intent intent = new Intent(v.getContext(),
                        Mypage.class);


                //화면전환. 결과값을 전달만 하고 돌려받지 않으므로
                //startActivityForResult()를 사용하지 않아도 됨.
                startActivity(intent);
            }
        });*/

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearstr = year.getText().toString();
                sp_month_str = sp_month.getSelectedItem().toString();
                sp_day_str = sp_day.getSelectedItem().toString();
                etPhonestr = etPhone.getText().toString();

                new AsyncHttpRequest().execute(
                        // 아이디, 성별, 이메일, 생년월일, 전화번호
                        "http://192.168.0.40:8080/softlock/Android/join"
                        , "mem_id=" + id
                        , "mem_pw=" + "NAVERLOGIN!"
                        , "mem_name=" + name
                        , "mem_phone=" + etPhonestr
                        , "mem_gender=" + gender
                        , "mem_email=" + email
                        , "mem_birth_year=" + yearstr
                        , "mem_birth_month=" + sp_month_str
                        , "mem_birth_day=" + sp_day_str
                        , "mem_auth=" + "y"

                );
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("회원가입 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");


        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.sp_month);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Spinner monthSpinner = (Spinner)findViewById(R.id.sp_day);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_day, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

       /* btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.EMAIL);
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });*/

    }////onCreate끝

   /* private void startLoginPage(LoginType loginType) {

        if(loginType == LoginType.EMAIL){
            Intent intent =  new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        }
        else if (loginType == LoginType.PHONE){
            Intent intent =  new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError() != null){
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            else if(result.wasCancelled()) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Toast.makeText(this, "Success ! %s"+result.getAuthorizationCode().substring(0,10), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Mypage.class));
            }
        }
    }*/

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
                out.write("&".getBytes());
                out.write(params[7].getBytes());
                out.write("&".getBytes());
                out.write(params[8].getBytes());
                out.write("&".getBytes());
                out.write(params[9].getBytes());
                out.write("&".getBytes());
                out.write(params[10].getBytes());
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