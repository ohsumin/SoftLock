package com.kosmo.softlock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class Mypage extends AppCompatActivity {

    ImageView btn_map;
    ImageView btn_res;
    ImageView btn_after;

    String mem_idx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);



        mem_idx = getIntent().getExtras().getString("mem_idx");
        Log.d("멤", mem_idx);

        btn_res = (ImageView)findViewById(R.id.btn_res);
        btn_map = (ImageView)findViewById(R.id.btn_map);
        btn_after = (ImageView)findViewById(R.id.btn_after);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        HpSearchMap.class);
                intent.putExtra("mem_idx", mem_idx);


                startActivity(intent);
            }
        });

        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        MyReservation.class);

                intent.putExtra("mem_idx", mem_idx);

                startActivity(intent);
            }
        });

        btn_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),
                        After.class);

                intent.putExtra("mem_idx", mem_idx);

                startActivity(intent);
            }
        });




    }
}