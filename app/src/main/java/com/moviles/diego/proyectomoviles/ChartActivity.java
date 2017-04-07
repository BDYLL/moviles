package com.moviles.diego.proyectomoviles;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.*;


public class ChartActivity extends AppCompatActivity {

    private BarChart chart;

    private List<BarEntry> entries;

    private List<String> axis= Collections.unmodifiableList(
            Arrays.asList("Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        this.chart=(BarChart)this.findViewById(R.id.chart);

        this.entries=new ArrayList<>();

        /*
        float start=2.f;

        for(int i=0;i<4;i++){
            this.entries.add(new PieEntry(start,axis.get(i)));
            start+=2.f;
        }
        */

        Intent i =this.getIntent();

        List<Integer> seconds=i.getIntegerArrayListExtra("com.moviles.diego.proyectomoviles.Days");

        int index=0;
        for(Integer second : seconds){
            this.entries.add(new BarEntry(index,second.floatValue(),axis.get(index)));
            index++;
        }

        BarDataSet dataSet = new BarDataSet(this.entries,"Ultimos 7 dias");

        List<Integer> colors = new ArrayList<>();


        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        BarData data =new BarData(dataSet);

        data.setValueTextSize(11.f);
        data.setValueTextColor(Color.GRAY);

        this.chart.setData(data);


        this.chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return ChartActivity.this.axis.get((int)value);
            }
        });


        this.chart.highlightValues(null);

        Description desc =new Description();

        desc.setText("");

        this.chart.setDescription(desc);

        this.chart.invalidate();

    }
}
