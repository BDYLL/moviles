package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ListView list;
    private static final int ACT3=3,CALENDAR=4;
    private List<String> activities;
    private ArrayAdapter<String> aa;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.list=(ListView)this.findViewById(R.id.list);

        this.db = new DBHelper(this);

        this.activities=this.db.getAll();

        this.aa = new ArrayAdapter<String>(this,R.layout.text,activities);

        list.setAdapter(aa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToCalendar(view);
            }
        });

    }

    public void sendToForm(View v){
        this.startActivityForResult(new Intent(this,Main3Activity.class),ACT3);
    }

    public void sendToCalendar(View v){
        Intent i = new Intent(this,Calendar.class);
        this.startActivityForResult(i,CALENDAR);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(Activity.RESULT_OK==resultCode && requestCode==ACT3){
            String activity = data.getStringExtra("name");
            String date = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
            this.db.add(activity,date,-1);
            activities.add(activity+","+date);

            this.aa.notifyDataSetChanged();
        }
        if(Activity.RESULT_OK==resultCode && requestCode==CALENDAR){
            String date = data.getStringExtra("date");
            List<String> result = this.db.find(date);
            this.activities.clear();
            this.activities.addAll(result);
            this.aa.notifyDataSetChanged();
        }
    }
}