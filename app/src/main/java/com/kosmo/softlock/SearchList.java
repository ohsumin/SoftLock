package com.kosmo.softlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.Level;


public class SearchList extends Fragment {

    ListView listView;
    String[] imageView1 = {};;
    String[] hpname = {};
    String[] hpaddress = {};
    String[] hpadr2 = {};
    Integer[] star = {};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_search_list, container, false);

        return v;
    }
}