package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Main4Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private ListView list;
    private static final int ACT3=3,CALENDAR=4,TIMER=5;
    private List<String> activities;
    private ArrayAdapter<String> aa;
    private DBHelper db;
    private Button goToCalendar;

    private ListView sideList;
    private ArrayAdapter<String> sideMenuOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToForm(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.list=(ListView)this.findViewById(R.id.list3);


        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendToTimer(((TextView)view).getText().toString());
            }
        });

        this.db = new DBHelper(this);

        this.activities=this.db.find(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Collections.reverse(this.activities);

        this.aa = new ArrayAdapter<String>(this,R.layout.text,activities);

        list.setAdapter(aa);

        /*
        this.goToCalendar=(Button)this.findViewById(R.id.goToCalendar);

        this.goToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToCalendar(v);
            }
        });
        */

    }


    public void sendToTimer(String s){
        Intent i = new Intent(this,TimerActivity.class);
        i.putExtra("activity",s);
        this.startActivity(i);
    }

    public void sendToForm(View v){
        this.startActivityForResult(new Intent(this,Main3Activity.class),ACT3);
    }

    public void sendToCalendar(View v){
        Intent i = new Intent(this,Calendar.class);
        i.putExtra("currentDate",(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()));
        this.startActivityForResult(i,CALENDAR);
    }

    public void sendToChart(View v){
        this.startActivity(new Intent(this,ChartActivity.class));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(Activity.RESULT_OK==resultCode && requestCode==ACT3){
            String activity = data.getStringExtra("name");
            String date = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
            this.db.add(activity,date,-1);
            activities.add(0,activity+","+date);

            this.aa.notifyDataSetChanged();
        }
        if(Activity.RESULT_OK==resultCode && requestCode==CALENDAR){
            String date = data.getStringExtra("date");
            List<String> result = this.db.find(date);
            this.activities.clear();
            this.activities.addAll(result);
            Collections.reverse(this.activities);
            this.aa.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.toCalendar:
                sendToCalendar(null);
                break;
            case R.id.toChart:
                sendToChart(null);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
