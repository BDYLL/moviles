package com.moviles.diego.proyectomoviles;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private final Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes/60;
            seconds     = seconds % 60;


            timer.setText(String.format("%02d : %02d : %02d",hours,minutes,seconds));

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

        this.timer.setText("00 : 00 : 00");

        this.name.setText(i.getStringExtra("activity"));


        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    startTime = System.currentTimeMillis();
                    t = new Timer();
                    t.schedule(new UpdateTimer(), 0, 1000);
                    isRunning = true;
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
                }
            }
        });
    }
}
