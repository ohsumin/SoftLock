package com.kosmo.softlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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



public class SearchList extends AppCompatActivity {

    ListView listView1;
    // 진료과, 병원이름, 병원주소, 상세주소, 별점
    String[] hpTypeArr = new String[100];
    String[] hpNameArr = new String[100];
    String[] hpAddrArr = new String[100];
    String[] hpAddr2Arr = new String[100];
    String[] hpPhoneArr = new String[100];
    //Integer[] starArr;
    int count = 5;

    String sBuffer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sBuffer = getIntent().getExtras().getString("sBuffer");
        Log.d("sBuffer", sBuffer);

        try {
            Log.d("얍", "1");
            JSONArray jsonArray = new JSONArray(sBuffer.toString());
            count = jsonArray.length();
            Log.d("얍", "2");
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("얍", "3");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("얍", "4");
                String hp_id = jsonObject.getString("hp_id");
                String hp_pw = jsonObject.getString("hp_pw");
                String hp_name = jsonObject.getString("hp_name");
                String hp_num = jsonObject.getString("hp_num");
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
                String hp_address2 = jsonObject.getString("hp_address2");
                String hp_type = jsonObject.getString("hp_type");

                hpTypeArr[i] = hp_type;
                hpNameArr[i] = hp_name;
                hpAddrArr[i] = hp_address;
                hpAddr2Arr[i] = hp_address2;
                hpPhoneArr[i] = hp_phone;
                Log.d("이름", hp_name);
                // starArr[i] = hp_score; // 후기 보류
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        final MyAdapter adapter = new MyAdapter();

        listView1 = (ListView) findViewById(R.id.listView1);

        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "선택한이름 : " + hpNameArr[position],
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return hpNameArr.length;
        }

        @Override
        public Object getItem(int position) {
            return hpNameArr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchList_item view = new SearchList_item(getApplicationContext());

            view.setHpType(hpTypeArr[position]);
            view.setHpName(hpNameArr[position]);
            view.setHpAddr(hpAddrArr[position]);
            view.setHpAddr2(hpAddr2Arr[position]);
            view.setHpPhone(hpPhoneArr[position]);

            return view;
        }
    }
}