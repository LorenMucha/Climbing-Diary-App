package com.main.climbingdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.main.climbingdiary.R;
import com.main.climbingdiary.chart.RouteBarChartController;
import com.main.climbingdiary.chart.RouteLineChartController;
import com.main.climbingdiary.controller.AppBarMenu;
import com.main.climbingdiary.controller.TableView;
import com.main.climbingdiary.models.Colors;
import com.main.climbingdiary.models.MenuValues;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticFragment extends Fragment implements RouteFragment {

    private Button setLineChartBtn, setBarChartBtn, setTableBtn;
    private View view;

    private static StatisticFragment INSTANZ = null;

    public static StatisticFragment getInstance() {
        if (INSTANZ == null) {
            INSTANZ = new StatisticFragment();
        }
        return INSTANZ;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.statistic_fragment, container, false);
        //set the bar barChart as Standard
        setBarChartBtn = view.findViewById(R.id.btn_stat);
        setLineChartBtn = view.findViewById(R.id.btn_dev);
        setTableBtn = view.findViewById(R.id.btn_table);
        //set the views
        final RouteLineChartController _routeLineChart = new RouteLineChartController(view);
        final TableView _routeTable = new TableView(view.getContext(), view);
        final RouteBarChartController _routeBarChartController = new RouteBarChartController(view);
        //the Button Click Listener
        setLineChartBtn.setOnClickListener(v -> {
            resetButtonBackground();
            setLineChartBtn.setBackgroundColor(Colors.getActiveColor());
            _routeLineChart.show();
            _routeTable.hide();
            _routeBarChartController.hide();
            _routeLineChart.createChart();
        });
        setBarChartBtn.setOnClickListener(v -> {
            resetButtonBackground();
            setBarChartBtn.setBackgroundColor(Colors.getActiveColor());
            _routeTable.hide();
            _routeLineChart.hide();
            _routeBarChartController.show();
            _routeBarChartController.createChart();
        });
        setTableBtn.setOnClickListener(v -> {
            resetButtonBackground();
            setTableBtn.setBackgroundColor(Colors.getActiveColor());
            _routeBarChartController.hide();
            _routeLineChart.hide();
            _routeTable.show();
            _routeTable.createTableView();
        });
        //default visualisation
        setBarChartBtn.callOnClick();
        return view;
    }

    //XAxis Formatter
    public void resetButtonBackground() {
        setLineChartBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
        setBarChartBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
        setTableBtn.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.buttonColor));
    }

    @Override
    public void refreshData() {
        try {
            RouteLineChartController _routeLineChart = new RouteLineChartController(view);
            TableView _routeTable = new TableView(view.getContext(), view);
            RouteBarChartController _routeBarChartController = new RouteBarChartController(view);
            _routeBarChartController.createChart();
            _routeLineChart.createChart();
            _routeTable.createTableView();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.setItemVisebility(MenuValues.FILTER, true);
        appmenu.setItemVisebility(MenuValues.SEARCH, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        if (item.getGroupId() == R.id.filter_area + 1) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
