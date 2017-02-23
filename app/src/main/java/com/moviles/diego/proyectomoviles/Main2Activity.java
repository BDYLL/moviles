package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ListView list;

    private static final int ACT3=3;

    private List<String> activities;
    private ArrayAdapter<String> aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.list=(ListView)this.findViewById(R.id.list);

        this.activities=new ArrayList<>();

        this.aa = new ArrayAdapter<String>(this,R.layout.text,activities);

        list.setAdapter(aa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToForm(view);
            }
        });
    }

    public void sendToForm(View v){
        this.startActivityForResult(new Intent(this,Main3Activity.class),ACT3);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(Activity.RESULT_OK==resultCode && requestCode==ACT3){
            activities.add(data.getStringExtra("name"));
            this.aa.notifyDataSetChanged();
        }
    }
}
