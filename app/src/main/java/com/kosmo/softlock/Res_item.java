package com.kosmo.softlock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Res_item extends LinearLayout {

    //TextView hpType;
    TextView hpName;
    TextView resvDate;
    TextView resvTime;
    Button cancel_reserv;
    TextView resvPerm;

    public Res_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_res_item, this, true);

        //hpType = (TextView) findViewById(R.id.hpType);
        hpName = (TextView) findViewById(R.id.hpName);
        resvDate = (TextView) findViewById(R.id.resvDate);
        resvTime = (TextView) findViewById(R.id.resvTime);
        resvPerm = (TextView) findViewById(R.id.resvPerm);
        cancel_reserv = (Button) findViewById(R.id.cancel_reserv);
    }

       // public void setHpType(String text){
           // hpType.setText(text);
        //}
        public void setHpName(String text){
            hpName.setText(text);
        }
        public void setResvDate(String text){
            resvDate.setText(text);
        }
        public void setResvTime(String text){
            resvTime.setText(text);
        }
        public void setResvPerm(String text){
            resvPerm.setText(text);
        }
}

