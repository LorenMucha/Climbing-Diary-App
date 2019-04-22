package com.example.climbingdiary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.climbingdiary.database.TaskRepository;
import com.example.climbingdiary.models.Colors;
import com.example.climbingdiary.models.Route;
import com.example.climbingdiary.models.Styles;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StatisticFragment extends Fragment {

    static View view;
    private static Button setLineChartBtn, setBarChartBtn, setTableBtn;
    static BarChart barChart;
    static LineChart lineChart;
    static ScrollView tableScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.statistic_fragment, container, false);
        //set the bar barChart as Standard
        setBarChartBtn = (Button) view.findViewById(R.id.btn_stat);
        setLineChartBtn  = (Button) view.findViewById(R.id.btn_dev);
        setTableBtn =(Button) view.findViewById(R.id.btn_table);
        //set the views
        barChart = (BarChart) view.findViewById(R.id.route_bar_chart);
        lineChart = (LineChart) view.findViewById(R.id.route_line_chart);
        tableScrollView = (ScrollView) view.findViewById(R.id.TableScrollView);
        //default visualisation
        this.createBarChart();
        //the Button Click Listener
        setLineChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetButtonBackground();
                StatisticFragment.resetStatisticViews();
                StatisticFragment.createLineChart();
                barChart.setVisibility(View.GONE);
            }
        });
        setBarChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetButtonBackground();
                StatisticFragment.resetStatisticViews();
                StatisticFragment.createBarChart();
                lineChart.setVisibility(View.GONE);
            }
        });
        setTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetStatisticViews();
                StatisticFragment.resetButtonBackground();
                StatisticFragment.createTableView();
            }
        });

        return view;
    }
    public static void createBarChart(){
        setBarChartBtn.setBackgroundColor(Colors.getActiveColor());
        List<BarEntry> entriesGroup = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        barChart.setVisibility(View.VISIBLE);
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
    //Todo funktioniert nicht
    public static void createLineChart(){
        setLineChartBtn.setBackgroundColor(Colors.getActiveColor());
        final ArrayList<String> labels = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        lineChart.setVisibility(View.VISIBLE);
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
    //XAxis Formatter
    public static class XAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues;

        private XAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            try {
                return mValues.get((int) value);
            }catch(Exception e){
                return "False";
            }
        }
    }
    public static void createTableView(){
        //Variables
        Context context = view.getContext();
        String[] styles = Styles.getStyle(true);
        TaskRepository taskRepository = new TaskRepository();
        taskRepository.open();
        Cursor cursor = taskRepository.getTableValues();
        //tree et because this sort the values
        Set<String> mLevels = new TreeSet<>();
        setTableBtn.setBackgroundColor(Colors.getActiveColor());
        tableScrollView.setVisibility(View.VISIBLE);
        TableLayout stk = (TableLayout) view.findViewById(R.id.route_table);
        //clear view
        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(context);
        TextView tv0 = new TextView(context);
        tv0.setText("Grad");
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);
        for(int x=0; x<=styles.length-1;x++){
            TextView tv_style = new TextView(context);
            tv_style.setText(styles[x]);
            tv_style.setTextColor(Color.BLACK);
            tv_style.setPadding(20,10,20,10);
            tbrow0.addView(tv_style);
        }
        TextView tv3 = new TextView(context);
        tv3.setText("GESAMT");
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        //variables
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                //get the values
                String level = cursor.getString(0);
                String os = cursor.getString(1);
                String rp = cursor.getString(2);
                String flash = cursor.getString(3);
                String gesamt = cursor.getString(4);
                //append the text view rows
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText(level);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(context);
                t2v.setText(os);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(context);
                t3v.setText(rp);
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                TextView t4v = new TextView(context);
                t4v.setText(flash);
                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);
                TextView t5v = new TextView(context);
                t5v.setText(gesamt);
                t5v.setTextColor(Color.BLACK);
                t5v.setGravity(Gravity.CENTER);
                tbrow.addView(t5v);
                stk.addView(tbrow);
                cursor.moveToNext();
            }
        }
    }
    public static void resetButtonBackground(){
        setLineChartBtn.setBackgroundColor(Colors.getMainColor());
        setBarChartBtn.setBackgroundColor(Colors.getMainColor());
        setTableBtn.setBackgroundColor(Colors.getMainColor());
    }
    public static void resetStatisticViews(){
        lineChart.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        tableScrollView.setVisibility(View.GONE);
    }
}
