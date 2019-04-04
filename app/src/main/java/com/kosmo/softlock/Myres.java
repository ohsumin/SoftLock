package com.kosmo.softlock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class Myres extends Fragment {

    private Context context;

    ListView listView;
/*    String[] hp_type;
    String[] hp_name;
    TextView restime;
    String[] resv_date;
    String[] resv_time;
    Button cancel_reserv;
    String[] resv_perm;
*/
    int count = 5;
    String sBuffer = "";

    String[] hp_typeArr = new String[100];
    String[] hp_nameArr = new String[100];
    String[] resv_dateArr = new String[100];
    String[] resv_timeArr = new String[100];
    String[] resv_permArr = new String[100];

    //ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_myres, container, false);

        context = container.getContext();


        new AsyncHttpRequest().execute(
                // 아이디, 성별, 이메일, 생년월일, 전화번호
                "http://192.168.0.38:8080/softlock/Android/reservationlist"
        );



       /* dialog = new ProgressDialog(v.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("회원님의 예약 정보를 불러오고 있습니다.");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");*/

        final MyAdapter adapter = new MyAdapter();

        listView = (ListView) v.findViewById(R.id.listView);

        listView.setAdapter(adapter);

        return v;
    }

    class AsyncHttpRequest extends AsyncTask<String, Void, String> {
        // doInBackground 함수 호출전에 미리 호출하는 메소드
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*// 서버로 요청하는 시점에 프로그레스 대화창을 띄워준다.
            if (!dialog.isShowing()) {
                dialog.show();
            }*/
            Log.d("진입", "onPreExecute()");
        }

        // execute()가 호출되면 자동으로 호출되는 메소드(실제동작을 처리)
        @Override
        protected String doInBackground(String... params) {
            // execute()를 호출할때 전달한 3개의 파라미터를 가변인자로 전달받는다.
            // 함수 내부에서는 배열로 사용한다.
            Log.d("진입", "doInBackground()");
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

                /*
                요청 파라미터를 OutputStream으로 조립후 전달한다.
                - 파라미터는 쿼리스트링 형태로 지정한다.
                - 한국어를 전송하는 경우에는 URLEncode를 해야한다.
                - 아래와 같이 처리하면 Request Body에 데이터를 담을 수 있다.
                 */
                OutputStream out = connection.getOutputStream();
                out.write(params[1].getBytes());
                out.flush();
                out.close();

                /*
                getResponseCode()를 호출하면 서버로 요청이 전달된다.
                 */
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 서버로부터 응답이 온 경우
                    Log.i("AsyncTask Class", "HTTP_OK 대씸");
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
                    Log.i("AsyncTask Class", "HTTP_OK 안됨");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 눌러진 버튼이 로그인이면 파싱후 결과를 출력함
            /*
            [{"pass":"1234","regidate":2018-11-20,"name":"오수민","id":"test1"}, ... ]
             */
            try {
                JSONArray jsonArray = new JSONArray(sBuffer.toString());

                // sBuffer 초기화
                sBuffer.setLength(0);
                count = jsonArray.length();
                //Toast.makeText(get, jsonArray.length(), Toast.LENGTH_LONG).show();
                Log.d("얍", "1");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //String hp_id = jsonObject.getString("hp_id");
                    //String hp_pw = jsonObject.getString("hp_pw");
                    String hp_name = jsonObject.getString("hp_name");
                    /*String hp_num = jsonObject.getString("hp_num");
                    String hp_username = jsonObject.getString("hp_username");
                    String hp_email = jsonObject.getString("hp_email");
                    String hp_phone = jsonObject.getString("hp_phone");
                    String hp_address = jsonObject.getString("hp_address");
                    String hp_night = jsonObject.getString("hp_night");
                    String hp_wkend = jsonObject.getString("hp_wkend");
                    String hp_intro = jsonObject.getString("hp_intro");
                    String hp_notice = jsonObject.getString("hp_notice");
                    String hp_ori_filename = jsonObject.getString("hp_ori_filename");
                    String hp_perm = jsonObject.getString("hp_perm");
                    String hp_regidate = jsonObject.getString("hp_regidate");
                    String hp_hpphone = jsonObject.getString("hp_hpphone");
                    String hp_address2 = jsonObject.getString("hp_address2");*/
                    String hp_type = jsonObject.getString("hp_type");
                    String resv_date = jsonObject.getString("resv_date");
                    String resv_time = jsonObject.getString("resv_time");
                    String resv_perm = jsonObject.getString("resv_perm");


                    hp_typeArr[i] =hp_type;
                    hp_nameArr[i] = hp_name;
                    resv_dateArr[i] = resv_date;
                    resv_timeArr[i] = resv_time;
                    resv_permArr[i] = resv_perm;
                    Log.d("이름", hp_name);
                }
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
            Log.d("진입", "onPostExecute()");
            // 진행대화창 닫기
            //dialog.dismiss();

            // sBuffer를 SearchList로 넘김
            //Intent intent = new Intent(getApplicationContext(), SearchList.class);
            //Myres.putExtra("sBuffer", s);
            //startActivity(intent);
        }
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return hp_nameArr.length;
        }

        @Override
        public Object getItem(int position) {
            return hp_nameArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Reserv_item view = new Reserv_item(getContext());

            view.setHpType(hp_typeArr[position]);
            view.setHpName(hp_nameArr[position]);
            view.setResv_date(resv_dateArr[position]);
            view.setResv_time(resv_timeArr[position]);
            view.setResv_perm(resv_permArr[position]);

            return view;
        }
    }


}
