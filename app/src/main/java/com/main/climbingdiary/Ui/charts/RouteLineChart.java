package com.main.climbingdiary.Ui.charts;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.YAxis;
import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.abstraction.RouteChart;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Alerts;
import com.main.climbingdiary.models.Colors;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RouteLineChart extends RouteChart {
    private LineChart lineChart;
    private Context context;
    public RouteLineChart(View view){
        this.lineChart = (LineChart) view.findViewById(R.id.route_line_chart);
        this.context = view.getContext();
    }
    public void show(){
        this.lineChart.setVisibility(View.VISIBLE);
    }
    public void hide(){
        this.lineChart.setVisibility(View.GONE);
    }
    public void createChart(){
        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor yearCursor = taskRepository.getYears();
        //transform the cursor to an array list
        ArrayList<Integer> yearList = new ArrayList<Integer>();
        try {
            int i = 1;
            if (yearCursor != null) {
                while (!yearCursor.isAfterLast()) {
                    yearList.add(yearCursor.getInt(0));
                    yearCursor.moveToNext();
                }
            }
            Collections.sort(yearList);
            for (Integer yearVal : yearList) {
                Cursor cursor = taskRepository.getLineChartValues(yearVal);
                if (cursor != null) {
                    while (!cursor.isAfterLast()) {
                        int stat = cursor.getInt(0);
                        labels.add(cursor.getString(2));
                        entries.add(new Entry(i, stat));
                        cursor.moveToNext();
                    }
                    i++;
                }
            }
            Collections.sort(labels);
            taskRepository.close();
            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setColor(R.color.main_blue_color);
            dataSet.setCircleHoleColor(Colors.getActiveColor());
            dataSet.setCircleColor(Colors.getMainColor());
            dataSet.setLineWidth(5);
            dataSet.setCircleRadius(15);
            dataSet.setValueTextSize(15);
            // Setting Data
            LineData data = new LineData(dataSet);
            lineChart.setData(data);
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            //set thex axis
            XAxis xAxis = lineChart.getXAxis();
            //xAxis.setLabelCount(yearList.size(),true);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new XAxisValueFormatter(labels));
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setDrawGridLines(false);
            yAxis.setSpaceBottom(1f);
            //refresh
            lineChart.invalidate();
        }catch(Exception ex){
            Alerts.setErrorAlert(this.context);
            Log.d("Erstellung Line chart:",ex.getLocalizedMessage());
        }
    }

}
