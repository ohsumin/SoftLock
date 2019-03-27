package com.kosmo.softlock;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyReservation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);

        //TabHost를 이용하여 탭메뉴 구성하기

        //1.FragmentTabHost 위젯 가져오기
        FragmentTabHost fth = (FragmentTabHost) findViewById(android.R.id.tabhost);

        fth.setup(this, getSupportFragmentManager(), R.id.realcontent);


        Bundle bundle = new Bundle();
        bundle.putString("msgStr", "메인엑티비티에서 번들객체를 통해" + " 전달하는 데이터입니다.");
        //4.탭메뉴 추가 : 전달할 데이터는 마지막 매개변수를 사용하고 없으면 null을 쓰면 됨
        fth.addTab(fth.newTabSpec("tab1").setIndicator("접수현황"), Myres.class, bundle);
        fth.addTab(fth.newTabSpec("tab2").setIndicator("진료내역"), Myinfo.class, bundle);
        fth.addTab(fth.newTabSpec("tab3").setIndicator("스크랩"), Searchmap.class, bundle);


        fth.setCurrentTab(0);
    }
}