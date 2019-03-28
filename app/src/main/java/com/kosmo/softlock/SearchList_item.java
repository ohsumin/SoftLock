package com.kosmo.softlock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class SearchList_item extends LinearLayout {

    TextView hpType;
    TextView hpName;
    TextView hpAddr;
    TextView hpAddr2;
    TextView hpPhone;
    //RatingBar star;

    public SearchList_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_search_list_item, this, true);

        hpType = (TextView) findViewById(R.id.hpType);
        hpName = (TextView) findViewById(R.id.hpName);
        hpAddr = (TextView) findViewById(R.id.hpAddr);
        hpAddr2 = (TextView) findViewById(R.id.hpAddr2);
        hpPhone = (TextView) findViewById(R.id.hpPhone);
        //star = (RatingBar) findViewById(R.id.star);
    }

    public void setHpType(String text){
        hpType.setText(text);
    }
    public void setHpName(String text){
        hpName.setText(text);
    }
    public void setHpAddr(String text){
        hpAddr.setText(text);
    }
    public void setHpAddr2(String text){
        hpAddr2.setText(text);
    }
    public void setHpPhone(String text){
        hpPhone.setText(text);
    }
}
