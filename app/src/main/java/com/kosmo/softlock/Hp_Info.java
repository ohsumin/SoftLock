package com.kosmo.softlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Hp_Info extends AppCompatActivity {

    ///String hp_name, hp_phone, hp_address, hp_intro, hp_notice;
    TextView thp_name;
    TextView thp_address;
    TextView thp_intro;
    TextView thp_notice;
    TextView thp_phone;
    TextView thp_address2;

    TextView thp_opentimeMon;
    TextView thp_opentimeTue;
    TextView thp_opentimeWed;
    TextView thp_opentimeThur;
    TextView thp_opentimeFri;
    TextView thp_opentimeSat;
    TextView thp_opentimeSun;

    TextView thp_closetimeMon;
    TextView thp_closetimeTue;
    TextView thp_closetimeWed;
    TextView thp_closetimeThur;
    TextView thp_closetimeFri;
    TextView thp_closetimeSat;
    TextView thp_closetimeSun;

    String hp_opentime;
    String hp_closetime;
    String sBuffer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hp_info);

        thp_name = (TextView) findViewById(R.id.thp_name);
        thp_address = (TextView) findViewById(R.id.thp_address);
        thp_intro = (TextView) findViewById(R.id.thp_intro);
        thp_notice = (TextView) findViewById(R.id.thp_notice);
        thp_phone = (TextView) findViewById(R.id.thp_phone);
        thp_address2 = (TextView) findViewById(R.id.thp_address2);

        thp_opentimeMon =  (TextView) findViewById(R.id.thp_opentimeMon);
        thp_opentimeTue =  (TextView) findViewById(R.id.thp_opentimeTue);
        thp_opentimeWed =  (TextView) findViewById(R.id.thp_opentimeWed);
        thp_opentimeThur =  (TextView) findViewById(R.id.thp_opentimeThur);
        thp_opentimeFri =  (TextView) findViewById(R.id.thp_opentimeFri);
        thp_opentimeSat =  (TextView) findViewById(R.id.thp_opentimeSat);
        thp_opentimeSun =  (TextView) findViewById(R.id.thp_opentimeSun);

        thp_closetimeMon =  (TextView) findViewById(R.id.thp_closetimeMon);
        thp_closetimeTue =  (TextView) findViewById(R.id.thp_closetimeTue);
        thp_closetimeWed =  (TextView) findViewById(R.id.thp_closetimeWed);
        thp_closetimeThur =  (TextView) findViewById(R.id.thp_closetimeThur);
        thp_closetimeFri =  (TextView) findViewById(R.id.thp_closetimeFri);
        thp_closetimeSat =  (TextView) findViewById(R.id.thp_closetimeSat);
        thp_closetimeSun =  (TextView) findViewById(R.id.thp_closetimeSun);



        TextView [] dy = {thp_opentimeMon,thp_opentimeTue,thp_opentimeWed,thp_opentimeThur,thp_opentimeFri,thp_opentimeSat,thp_opentimeSun};
        TextView [] dyc = {thp_closetimeMon, thp_closetimeTue, thp_closetimeWed, thp_closetimeThur, thp_closetimeFri, thp_closetimeSat, thp_closetimeSun};


        sBuffer = getIntent().getExtras().getString("sBuffer");
        Log.d("sBuffer", sBuffer);

        try{
            JSONObject jsonObject = new JSONObject(sBuffer.toString());
            JSONObject jsonObject2 = jsonObject.getJSONObject("HospitalDTO");

            JSONArray jsonArray = jsonObject.getJSONArray("tDTO"); //"dataSet"의 jsonObject들을 배열로 저장한다.


            // 병원정보뿌리기


            // 시간뿌리기
            for(int i=0; i<7; i++) { //jsonObject에 담긴 두 개의 jsonObject를 jsonArray를 통해 하나씩 호출한다.
                  /*JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                  //Log.d("sBuffer", jsonObject3.toString());
                  hp_opentime = jsonObject.getString("treat_open");
                  //Log.d("sBuffer", hp_opentime);
                  */

                JSONObject d = jsonArray.getJSONObject(i);
                hp_opentime = d.getString("treat_open");
                hp_closetime = d.getString("treat_close");

                if(hp_opentime.equals("오픈시간")){
                    hp_opentime="  휴무";
                }
                if(hp_closetime.equals("마감시간")){
                    hp_closetime="휴무";
                }


                dy[i].setText(hp_opentime+" - ");
                dyc[i].setText(""+hp_closetime);
                //Log.d("sBuffer", d.toString()); 7줄 받아옴
            }

            Log.d("sBuffer", jsonObject2.getString("hp_name")); //찍힘
            String hp_name = jsonObject2.getString("hp_name");
            String hp_phone = jsonObject2.getString("hp_phone");
            String hp_address = jsonObject2.getString("hp_address");
            String hp_address2 = jsonObject2.getString("hp_address2");
            String hp_intro = jsonObject2.getString("hp_intro");
            String hp_notice = jsonObject2.getString("hp_notice");

            Log.d("이름", hp_name);
            Log.d("주소", hp_address);
            Log.d("시간", hp_opentime);

            thp_name.setText(hp_name);
            thp_address.setText(hp_address);
            thp_address2.setText(hp_address2);
            thp_phone.setText(hp_phone);
            thp_intro.setText(hp_intro);
            thp_notice.setText(hp_notice);



        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
