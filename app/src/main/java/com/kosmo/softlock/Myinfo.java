package com.kosmo.softlock;

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

public class Myinfo extends AppCompatActivity {

    ListView listView2;

    int count = 5;
    String sBuffer = "";


    String[] hp_nameArr = new String[100];
    String[] resv_dateArr = new String[100];
    String[] resv_timeArr = new String[100];


    String  mem_idx ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sBuffer = getIntent().getExtras().getString("sBuffer");

        Log.d("멍", sBuffer);

        try {
            Log.d("얍", "1");
            JSONArray jsonArray = new JSONArray(sBuffer.toString());
            count = jsonArray.length();
            Log.d("얍", "2");
            for (int i = 0; i < jsonArray.length(); i++) {

                Log.d("얍", "3");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("얍", "4");

                String hp_name = jsonObject.getString("hp_name");
                String resv_date = jsonObject.getString("resv_date");
                String resv_time = jsonObject.getString("resv_time");


                hp_nameArr[i] = hp_name;
                resv_dateArr[i] = resv_date;
                resv_timeArr[i] = resv_time;

                Log.d("이름", hp_name);
                // starArr[i] = hp_score; // 후기 보류
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final MyAdapter adapter = new MyAdapter();

        listView2 = (ListView) findViewById(R.id.listView2);

        listView2.setAdapter(adapter);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            End_item view = new End_item(getApplicationContext());


            view.setHpName(hp_nameArr[position]);
            view.setResvDate(resv_dateArr[position]);
            view.setResvTime(resv_timeArr[position]);


            return view;
        }
    }
}
