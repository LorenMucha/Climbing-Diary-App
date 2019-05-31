package com.main.climbingdiary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.main.climbingdiary.Ui.AppBarMenu;
import com.main.climbingdiary.Ui.TableView;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.Ui.button.ShowTimeSlider;
import com.main.climbingdiary.Ui.charts.RouteBarChart;
import com.main.climbingdiary.Ui.charts.RouteLineChart;
import com.main.climbingdiary.models.Colors;

public class StatisticFragment extends Fragment {

    private static Button setLineChartBtn, setBarChartBtn, setTableBtn;
    static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.statistic_fragment, container, false);
        //set the bar barChart as Standard
        setBarChartBtn = (Button) view.findViewById(R.id.btn_stat);
        setLineChartBtn  = (Button) view.findViewById(R.id.btn_dev);
        setTableBtn =(Button) view.findViewById(R.id.btn_table);
        //set the views
        final RouteLineChart _routeLineChart = new RouteLineChart(view);
        final TableView _routeTable = new TableView(view.getContext(),view);
        final RouteBarChart _routeBarChart = new RouteBarChart(view);
        //the Button Click Listener
        setLineChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetButtonBackground();
                setLineChartBtn.setBackgroundColor(Colors.getActiveColor());
                _routeLineChart.show();
                _routeTable.hide();
                _routeBarChart.hide();
                _routeLineChart.createChart();
            }
        });
        setBarChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetButtonBackground();
                setBarChartBtn.setBackgroundColor(Colors.getActiveColor());
                _routeTable.hide();
                _routeLineChart.hide();
                _routeBarChart.show();
                _routeBarChart.createChart();
            }
        });
        setTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticFragment.resetButtonBackground();
                setTableBtn.setBackgroundColor(Colors.getActiveColor());
                _routeBarChart.hide();
                _routeLineChart.hide();
                _routeTable.show();
                _routeTable.createTableView();
            }
        });
        //default visualisation
        setBarChartBtn.callOnClick();
        setHasOptionsMenu(true);

       return view;
    }
    //XAxis Formatter
    public static void resetButtonBackground(){
        setLineChartBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
        setBarChartBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
        setTableBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
    }
    public static void refreshData(){
        final RouteLineChart _routeLineChart = new RouteLineChart(view);
        final TableView _routeTable = new TableView(view.getContext(),view);
        final RouteBarChart _routeBarChart = new RouteBarChart(view);
        _routeBarChart.createChart();
        _routeLineChart.createChart();
        _routeTable.createTableView();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.hideItem();
    }
}
