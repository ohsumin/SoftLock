package com.kosmo.softlock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class Reserv_item extends LinearLayout {

    Button imageView2;
    TextView endhpname;
    TextView restime;
    TextView endreservtime;
    Button cancel_reserv;
    Button write;


    public Reserv_item(Context context){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_reserv_item, this, true);

        imageView2 = (Button)findViewById(R.id.imageView2);
        endhpname = (TextView) findViewById(R.id.endhpname);
        restime = (TextView) findViewById(R.id.restime);
        endreservtime = (TextView) findViewById(R.id.endreservtime);
        cancel_reserv = (Button)findViewById(R.id.cancel_reserv);
        write = (Button)findViewById(R.id.write);

    }

    public void imageView2(String text){
        imageView2.setText(text);
    }

    public void endhpname(String text){
        endhpname.setText(text);
    }

    public void restime(String text){
        restime.setText(text);
    }

    public void endreservtime(String text){
        endreservtime.setText(text);
    }

    public void cancel_reserv(String text){
        cancel_reserv.setText(text);
    }

    public void write(String text){
        write.setText(text);
    }

}