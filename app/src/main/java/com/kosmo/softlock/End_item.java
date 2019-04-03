package com.kosmo.softlock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class End_item extends LinearLayout {



    TextView hpName;
    TextView restime;
    TextView resvDate;
    TextView resvTime;




    public End_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_end_item, this, true);



        hpName = (TextView) findViewById(R.id.hpName);
        restime = (TextView) findViewById(R.id.restime);
        resvDate = (TextView) findViewById(R.id.resvDate);
        resvTime = (TextView) findViewById(R.id.resvTime);



    }


    public void setHpName(String text){
        hpName.setText(text);
    }
    public void setResvDate(String text){
        resvDate.setText(text);
    }
    public void setResvTime(String text){
        resvTime.setText(text);
    }


}