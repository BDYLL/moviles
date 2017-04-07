package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {

    private Button start,stop;
    private TextView name,timer;

    private long startTime=0l;

    private boolean isRunning=false;

    private Timer t;

    private NotificationCompat.Builder builder;

    private NotificationManager manager;

    private static final int ID_NOTIFICATION=0;

    private int time=-1;

    private int id;

    private final Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes/60;
            seconds     = seconds % 60;
            minutes = minutes%60;

            time+=1;

            String text = String.format("%02d : %02d : %02d",hours,minutes,seconds);

            builder.setContentText(text);

            manager.notify(ID_NOTIFICATION,builder.build());

            timer.setText(text);

            return false;
        }
    });

    class UpdateTimer extends TimerTask{
       public void run(){
           h.sendEmptyMessage(0);
       }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        this.start=(Button)this.findViewById(R.id.start);
        this.stop=(Button)this.findViewById(R.id.stop);

        this.name=(TextView)this.findViewById(R.id.activityName);
        this.timer=(TextView)this.findViewById(R.id.timerView);

        this.startTime=System.currentTimeMillis();

        Intent i = this.getIntent();

        this.id=i.getIntExtra("id",-1);

        this.timer.setText("00 : 00 : 00");

        this.name.setText(i.getStringExtra("activity"));

        this.builder=new NotificationCompat.Builder(this);

        this.builder
                .setSmallIcon(R.drawable.timer)
                .setContentTitle("Timer")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true);


        this.manager=(NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    startTime = System.currentTimeMillis();
                    t = new Timer();
                    t.schedule(new UpdateTimer(), 0, 1000);
                    isRunning = true;
                    notification();
                }
            }
        });

        this.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning) {
                    t.cancel();
                    t.purge();
                    isRunning=false;
                    stopNotification();
                }
            }
        });
    }

    private void notification(){
        this.manager.notify(ID_NOTIFICATION,this.builder.build());
    }
    private void stopNotification(){
        this.manager.cancel(ID_NOTIFICATION);
    }


    @Override
    public void onBackPressed() {


        if(time>0) {

            Intent i = new Intent();
            i.putExtra("time", time);
            i.putExtra("id",id);
            this.setResult(Activity.RESULT_OK,i);

        }
        super.onBackPressed();
    }
}
