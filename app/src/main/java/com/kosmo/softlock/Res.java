package com.kosmo.softlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Res extends AppCompatActivity {

    ListView listView;

    int count = 5;
    String sBuffer = "";

    //String[] hp_typeArr = new String[100];
    String[] hp_nameArr = new String[100];
    String[] resv_dateArr = new String[100];
    String[] resv_timeArr = new String[100];
    String[] resv_permArr = new String[100];
    String[] resv_idxArr = new String[100];

    String  mem_idx ="";
    String resv_idx="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);

        resv_idx = getIntent().getExtras().getString("resv_idx");
        sBuffer = getIntent().getExtras().getString("sBuffer");



        Log.d("멍", sBuffer);

        try {
            Log.d("얍", "1");
            JSONArray jsonArray = new JSONArray(sBuffer.toString());
            //JSONObject jsonObject = new JSONObject(sBuffer.toString());
            count = jsonArray.length();
            Log.d("얍", "2");
            for (int i = 0; i < jsonArray.length(); i++) {

                Log.d("얍", "3");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("얍", "4");
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
                //String hp_type = jsonObject.getString("hp_type");
                String resv_date = jsonObject.getString("resv_date");
                String resv_time = jsonObject.getString("resv_time");
                String resv_perm = jsonObject.getString("resv_perm");
                String resv_idx = jsonObject.getString("resv_idx");

                //hp_typeArr[i] = hp_type;
                hp_nameArr[i] = hp_name;
                resv_dateArr[i] = resv_date;
                resv_timeArr[i] = resv_time;
                resv_permArr[i] = resv_perm;
                resv_idxArr[i] = resv_idx;
                Log.d("이름", hp_name);
                Log.d("예약idx", resv_idx);
                // starArr[i] = hp_score; // 후기 보류
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final MyAdapter adapter = new MyAdapter();

        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "선택한이름 : " + hp_nameArr[position],
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends BaseAdapter {



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
            Res_item view = new Res_item(getApplicationContext());

            //view.setHpType(hp_typeArr[position]);
            view.setHpName(hp_nameArr[position]);
            view.setResvDate(resv_dateArr[position]);
            view.setResvTime(resv_timeArr[position]);
            view.setResvPerm(resv_permArr[position]);
            view.setResv_idx(resv_idxArr[position]);



            return view;
        }
    }
}
