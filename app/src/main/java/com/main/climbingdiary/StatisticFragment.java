package com.main.climbingdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.main.climbingdiary.R;
import com.main.climbingdiary.Ui.TableView;
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
                _routeLineChart.createLineChart();
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
                _routeBarChart.createBarChart();
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
       return view;
    }
    //XAxis Formatter
    public static void resetButtonBackground(){
        setLineChartBtn.setBackgroundColor(Colors.getMainColor());
        setBarChartBtn.setBackgroundColor(Colors.getMainColor());
        setTableBtn.setBackgroundColor(Colors.getMainColor());
    }
    public static void refreshData(){
        final RouteLineChart _routeLineChart = new RouteLineChart(view);
        final TableView _routeTable = new TableView(view.getContext(),view);
        final RouteBarChart _routeBarChart = new RouteBarChart(view);
        _routeBarChart.createBarChart();
        _routeLineChart.createLineChart();
        _routeTable.createTableView();
    }
}
