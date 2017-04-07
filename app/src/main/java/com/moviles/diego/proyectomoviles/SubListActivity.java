package com.moviles.diego.proyectomoviles;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class SubListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list);

        ListView list = (ListView)this.findViewById(R.id.sub_list);

        DBHelper db = new DBHelper(this);

        Intent i = this.getIntent();

        int id = i.getIntExtra("id",-1);

        List<String> source=db.getAllSubActivities(id);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.text,source);

        list.setAdapter(adapter);
    }

}
