package com.example.climbingdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.climbingdiary.database.TaskRepository;
import com.example.climbingdiary.models.Colors;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.statistic_fragment, container, false);
        //set the bar chart
        this.createBarChart();
        return view;
    }
    public static void createBarChart(){
        List<BarEntry> entriesGroup = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        BarChart chart = (BarChart) view.findViewById(R.id.route_bar_chart);
        //get the cjart entries
        TaskRepository taskRepository = new TaskRepository(view.getContext());
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
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        //set thex axis
        Log.d("labels", TextUtils.join(",",labels));
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new XAxisValueFormatter(labels));
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setSpaceBottom(0);
        chart.invalidate(); // refresh
    }
    //XAxis Formatter
    public static class XAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues = new ArrayList<>();

        private XAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            try {
                return mValues.get((int) value);
            }catch(Exception e){
                return "";
            }
        }
    }
}
