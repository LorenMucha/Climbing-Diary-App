package com.main.climbingdiary.chart;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Colors;

import java.util.ArrayList;
import java.util.List;

public class RouteBarChartControllerController extends RouteChartController {
    private static Context context;
    private BarChart barChart;

    public RouteBarChartControllerController(View _view) {
        this.barChart = _view.findViewById(R.id.route_bar_chart);
        context = _view.getContext();
    }

    public void show() {
        this.barChart.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.barChart.setVisibility(View.GONE);
    }

    public void createChart() {
        try {
            List<BarEntry> entriesGroup = new ArrayList<>();
            final ArrayList<String> labels = new ArrayList<>();
            //get the cjart entries
            TaskRepository taskRepository = TaskRepository.getInstance();
            //String Sort = (Menu) getA
            Cursor cursor = taskRepository.getBarChartValues();
            int i = 0;
            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    String level = cursor.getString(0);
                    float sum_rp = cursor.getFloat(1);
                    float sum_os = cursor.getFloat(2);
                    float sum_flash = cursor.getFloat(3);
                    labels.add(level);
                    entriesGroup.add(new BarEntry(i, new float[]{sum_flash, sum_rp, sum_os}));
                    cursor.moveToNext();
                    i++;
                }
            }
            BarDataSet set = new BarDataSet(entriesGroup, "");
            set.setColors(Colors.getStyleColor("flash"), Colors.getStyleColor("rp"), Colors.getStyleColor("os"));
            set.setDrawValues(false);
            set.setStackLabels(new String[]{"FLASH", "RP", "OS"});
            BarData data = new BarData(set);
            barChart.setData(data);
            barChart.getDescription().setEnabled(false);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.getAxisRight().setEnabled(false);
            //set thex axis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(labels.size());
            xAxis.setValueFormatter(new XAxisValueFormatter(labels));
            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setSpaceBottom(1f);
            barChart.invalidate(); // refresh

            //set the on click Listener
            this.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    //ToDo
                    float x = e.getX();
                    int y = (int) e.getY();
                    String style = set.getStackLabels()[h.getStackIndex()];
                    float sum = entriesGroup.get((int) x).getYVals()[h.getStackIndex()];
                    Toast.makeText(context, String.format("Stil: %s\nAnzahl: %s", style, sum), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        } catch (Exception ex) {
            AlertManager.setErrorAlert(context);
            Log.d("Erstellung Barchart:", ex.getLocalizedMessage());
        }
    }
}
