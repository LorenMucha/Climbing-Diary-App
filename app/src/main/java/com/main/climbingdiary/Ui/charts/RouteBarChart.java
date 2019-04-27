package com.main.climbingdiary.Ui.charts;

import android.database.Cursor;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Colors;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class RouteBarChart {
    public BarChart barChart;
    public RouteBarChart(View _view){
        this.barChart = (BarChart) _view.findViewById(R.id.route_bar_chart);
    }
    public void show(){
        this.barChart.setVisibility(View.VISIBLE);
    }
    public void hide(){
        this.barChart.setVisibility(View.GONE);
    }
    public void createBarChart(){
        List<BarEntry> entriesGroup = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        //get the cjart entries
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        //String Sort = (Menu) getA
        Cursor cursor = taskRepository.getBarChartValues();
        int i =0;
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
        set.setDrawValues(false);
        set.setStackLabels(new String[]{"FLASH", "RP", "OS"});
        BarData data = new BarData(set);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        //set thex axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new XAxisValueFormatter(labels));
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setSpaceBottom(1f);
        barChart.invalidate(); // refresh
    }
}
