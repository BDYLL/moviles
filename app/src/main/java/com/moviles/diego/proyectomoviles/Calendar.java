package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends AppCompatActivity {

    private CalendarView calendar;
    private Button calendarButton;
    private Date currentDate;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        this.calendar = (CalendarView)this.findViewById(R.id.calendarView);
        this.calendarButton = (Button)this.findViewById(R.id.calendarButton);

        this.currentDate=new Date(this.calendar.getDate());

        c=this;

        this.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(year+"-"+month+"-"+dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        this.calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(v);
            }
        });

    }

    public void getDate(View v){
        Intent i = new Intent(this,Main2Activity.class);
        i.putExtra("date",new SimpleDateFormat("yyyy-MM-dd").format(currentDate));
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}
