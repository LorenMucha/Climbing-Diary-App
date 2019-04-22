package com.example.climbingdiary.Ui.charts;

import android.database.Cursor;
import android.view.View;

import com.example.climbingdiary.R;
import com.example.climbingdiary.database.TaskRepository;
import com.example.climbingdiary.models.Colors;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class RouteLineChart {
    private LineChart lineChart;
    public RouteLineChart(View view){
        this.lineChart = (LineChart) view.findViewById(R.id.route_line_chart);
    }
    public void show(){
        this.lineChart.setVisibility(View.VISIBLE);
    }
    public void hide(){
        this.lineChart.setVisibility(View.GONE);
    }
    public void createLineChart(){
        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor cursor = taskRepository.getLineChartValues();
        int i =0;
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                int stat = cursor.getInt(0);
                float year = cursor.getFloat(2);
                labels.add(cursor.getString(2));
                entries.add(new Entry(i,stat));
                cursor.moveToNext();
                i++;
            }
        }
        taskRepository.close();
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(Colors.getMainColor());
        dataSet.setValueTextColor(Colors.getMainColor());
        dataSet.setValueTextSize(10);
        dataSet.setCircleHoleRadius(10);
        dataSet.setCircleColor(Colors.getMainColor());
        // Setting Data
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        //set thex axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisValueFormatter(labels));
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setSpaceBottom(1f);
        //refresh
        lineChart.invalidate();
    }

}
