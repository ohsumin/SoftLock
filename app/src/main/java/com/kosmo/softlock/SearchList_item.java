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

    Button imageView1;
    TextView hpname;
    TextView hpaddress;
    TextView hpadr2;
    RatingBar star;


    public SearchList_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_search_list_item, this, true);

        imageView1 = (Button)findViewById(R.id.imageView1);
        hpname = (TextView) findViewById(R.id.hpname);
        hpadr2 = (TextView) findViewById(R.id.hpadr2);
        hpaddress = (TextView) findViewById(R.id.hpaddress);
        star = (RatingBar) findViewById(R.id.star);
    }

    public void setImageView1(String text){
        imageView1.setText(text);
    }

    public void setHpname(String text){
        hpname.setText(text);
    }

    public void setHpaddress(String text){
        hpaddress.setText(text);
    }

    public void setHpadr2(String text){
        hpadr2.setText(text);
    }

    public void setStar(int starNum){
        star.setRating(starNum);
    }

}
