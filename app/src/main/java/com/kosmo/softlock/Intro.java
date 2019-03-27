package com.kosmo.softlock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Intro extends AppCompatActivity {

    /*
    메니페스트 파일을 수정하여 MainActivity보다 Intro를 먼저 실행할 수 있도록 한다.
    */

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(새롭게 실행되는 액티비티,
            //                    종료되는 액티비티);
            //overridePendingTransition(R.anim.slide_in_right,
            //         R.anim.slide_in_up);
            //overridePendingTransition(R.anim.slide_in_right,
            //        R.anim.fade_out);
            overridePendingTransition(R.anim.fade_in,
                    R.anim.fade_out);

            finish();//현재 액티비티를 종료한다.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    //수명주기 함수 중 아래 2개만 오버라이딩 한다.
    //참조 : 액티비티의 수명주기(LifeCycle)메소드
    //          액티비티시작 - > onCreate() -> onStart() -> onResume()
    //          액티비티 실행중에 다른 액티비티를 실행하면 onPause() -> onStop() -> onDestroy() -> 액티비티 종료료
    @Override
    protected void onResume() {
        super.onResume();
        /*
        화면에 진입했을때 예약 걸어주기.
        2초후에 Runnable 객체 수행함.
        */
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        화면을 벗어났을때 handler에 예약해놓은 작업을 취소한다.
        */
        handler.removeCallbacks(runnable);
    }
}