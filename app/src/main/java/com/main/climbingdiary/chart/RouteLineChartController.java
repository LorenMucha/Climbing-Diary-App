package com.main.climbingdiary.chart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.models.Colors;
import com.main.climbingdiary.models.Levels;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.models.Styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

public class RouteLineChartController extends RouteChartController {
    private final LineChart lineChart;
    private final Context context;
    private Map<Integer, List<InfoObject>> routeResults = new LinkedHashMap<>();
    public RouteLineChartController(View view){
        this.lineChart = view.findViewById(R.id.route_line_chart);
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
        TaskRepository taskRepository = TaskRepository.getInstance();
        Cursor yearCursor = taskRepository.getYears(true);
        //transform the cursor to an array list
        ArrayList<Integer> yearList = new ArrayList<Integer>();
        routeResults.clear();
        try {
            int i = 0;
            if (yearCursor != null) {
                while (!yearCursor.isAfterLast()) {
                    yearList.add(yearCursor.getInt(0));
                    yearCursor.moveToNext();
                }
            }
            Collections.sort(yearList);
            for (Integer yearVal : yearList) {
                Cursor cursor = taskRepository.getTopTenRoutes(yearVal);
                List<InfoObject> routesYear = new ArrayList<>();
                int sum = 0;
                if (cursor != null) {
                    while (!cursor.isAfterLast()) {
                        String level = cursor.getString(0);
                        String stil = cursor.getString(1);
                        int points = (Levels.getLevelRating(level)+ Styles.getStyleRatingFactor(stil));
                        InfoObject route = new InfoObject();
                        route.points = points;
                        route.route_level = level;
                        route.routeStil = stil;
                        route.routeName = cursor.getString(2);
                        routesYear.add(route);
                        sum += points;
                        cursor.moveToNext();
                    }
                }
                labels.add(Integer.toString(yearVal));
                routeResults.put(yearVal,routesYear);
                entries.add(new Entry(i, sum));
                i++;
            }
            Collections.sort(labels);
            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setColor(R.color.main_blue_color);
            dataSet.setCircleHoleColor(Colors.getActiveColor());
            dataSet.setCircleColor(Colors.getMainColor());
            dataSet.setHighLightColor(Color.RED);
            dataSet.setLineWidth(5);
            dataSet.setCircleRadius(15);
            dataSet.setValueTextSize(15);
            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //to enable the cubic density : if 1 then it will be sharp curve
            dataSet.setCubicIntensity(0.2f);
            //to fill the below of smooth line in graph
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.BLACK);
            //set the transparency
            dataSet.setFillAlpha(80);

            //set the gradiant then the above draw fill color will be replace
            Drawable drawable = ContextCompat.getDrawable(MainActivity.getMainAppContext(), R.drawable.radiant_linechart);
            dataSet.setFillDrawable(drawable);
            // Setting Data
            LineData data = new LineData(dataSet);
            lineChart.setData(data);
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.moveViewToX(labels.size());
            //set thex axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new XAxisValueFormatter(labels));
            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setDrawGridLines(false);
            yAxis.setSpaceBottom(5f);
            yAxis.setSpaceTop(5f);
            yAxis.setAxisMaximum(data.getYMax()+200);
            //refresh
            lineChart.zoom(+3f,0f,0,0);
            lineChart.invalidate();
            this.lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    int year = yearList.get((int)e.getX());
                    showDialog(Objects.requireNonNull(routeResults.get(year)),year);
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }catch(Exception ex){
            AlertManager.setErrorAlert(this.context);
            Log.d("Erstellung Line chart:",ex.getLocalizedMessage());
        }
    }

    private void showDialog(List<InfoObject> objectList, int year){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        @SuppressLint("InflateParams") View alertView = inflater.inflate(R.layout.dialog_line_chart_top_ten, null);
        builder.setView(alertView);
        builder.setTitle("Die 10 schwersten Touren - "+year);
        TableLayout tableLayout = alertView.findViewById(R.id.tableLayoutTopTen);
        for(InfoObject infoObject: objectList ){

            TableRow tableRow = new TableRow(dialogContext);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(params);

            TextView textViewName = new TextView(dialogContext);
            textViewName.setText(infoObject.getRouteName());
            textViewName.setTypeface(null,Typeface.BOLD);
            textViewName.setTextColor(this.context.getResources().getColor(R.color.black));
            textViewName.setPadding(20,0,0,0);
            tableRow.addView(textViewName);

            TextView textViewLevel = new TextView(dialogContext);
            textViewLevel.setText(infoObject.getRoute_level());
            textViewLevel.setTypeface(null, Typeface.BOLD);
            textViewLevel.setTextColor(Colors.getGradeColor(infoObject.getRoute_level()));
            textViewLevel.setPadding(20,0,0,0);
            tableRow.addView(textViewLevel);

            TextView textViewStil = new TextView(dialogContext);
            textViewStil.setText(infoObject.getRouteStil());
            textViewStil.setTypeface(null, Typeface.BOLD);
            textViewStil.setTextColor(Colors.getStyleColor(infoObject.getRouteStil()));
            textViewStil.setPadding(20,0,0,0);
            tableRow.addView(textViewStil);

            tableLayout.addView(tableRow);
        }

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Data
    private static class InfoObject{
        private String route_level;
        private String routeName;
        private String routeStil;
        private int points;
    }

}
