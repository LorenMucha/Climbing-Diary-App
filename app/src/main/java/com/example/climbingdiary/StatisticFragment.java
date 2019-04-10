package com.example.climbingdiary;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.climbingdiary.database.TaskRepository;
import com.example.climbingdiary.models.Colors;
import com.example.climbingdiary.models.Levels;
import com.example.climbingdiary.models.Route;
import com.example.climbingdiary.models.RouteSort;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statistic_fragment, container, false);
        //set the bar chart
        this.createBarChart(view);
        return view;
    }
    private void createBarChart(View view){
        List<BarEntry> entriesGroup = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        BarChart chart = (BarChart) view.findViewById(R.id.route_bar_chart);
        //get the cjart entries
        TaskRepository taskRepository = new TaskRepository(view.getContext());
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getBarChartValues();
        float i =1;
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                String level = cursor.getString(0);
                float sum_rp = cursor.getFloat(1);
                float sum_os = cursor.getFloat(2);
                float sum_flash = cursor.getFloat(3);
                labels.add(level);
                entriesGroup.add(new BarEntry(i, new float[] { sum_flash, sum_rp, sum_os }));
                cursor.moveToNext();
                i++;
            }
        }
        taskRepository.close();
        BarDataSet set = new BarDataSet(entriesGroup,"");
        set.setColors(Colors.getStyleColor("flash"),Colors.getStyleColor("rp"),Colors.getStyleColor("os"));
        set.setStackLabels(new String[]{"FLASH", "RP", "OS"});
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        //set thex axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        chart.invalidate(); // refresh
    }
}
