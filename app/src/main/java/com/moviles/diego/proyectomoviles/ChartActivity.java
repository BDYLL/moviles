package com.moviles.diego.proyectomoviles;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class ChartActivity extends AppCompatActivity {

    private PieChart chart;

    private List<PieEntry> entries;

    private List<String> axis=Arrays.asList("Yolo","Swag","BS","BAWLS");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        this.chart=(PieChart)this.findViewById(R.id.chart);
        this.entries=new ArrayList<>();

        float start=2.f;

        for(int i=0;i<4;i++){
            this.entries.add(new PieEntry(start,axis.get(i)));
            start+=2.f;
        }

        PieDataSet dataSet = new PieDataSet(this.entries,"BULLSHIT");
        dataSet.setSliceSpace(3.f);
        dataSet.setSelectionShift(5.f);

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


        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11.f);
        data.setValueTextColor(Color.GRAY);

        this.chart.setData(data);


        this.chart.highlightValues(null);

        Description desc =new Description();

        desc.setText("");

        this.chart.setDescription(desc);

        this.chart.invalidate();

    }
}
