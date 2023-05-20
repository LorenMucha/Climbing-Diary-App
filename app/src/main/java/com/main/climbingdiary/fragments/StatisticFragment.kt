package com.main.climbingdiary.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.main.climbingdiary.R
import com.main.climbingdiary.chart.RouteBarChartController
import com.main.climbingdiary.chart.RouteLineChartController
import com.main.climbingdiary.controller.AppBarMenu
import com.main.climbingdiary.chart.TableView
import com.main.climbingdiary.models.Colors
import com.main.climbingdiary.models.MenuValues

class StatisticFragment: Fragment(), RouteFragment {

    private lateinit var setLineChartBtn: Button
    private lateinit var setBarChartBtn: Button
    private lateinit var setTableBtn: Button
    private lateinit var view: View
    private lateinit var routeLineChartController: RouteLineChartController

    override fun getView(): View {
        return view
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.statistic_fragment, container, false)
        //set the bar barChart as Standard
        setBarChartBtn = view.findViewById(R.id.btn_stat)
        setLineChartBtn = view.findViewById<Button>(R.id.btn_dev)
        setTableBtn = view.findViewById(R.id.btn_table)
        //set the views
        routeLineChartController = RouteLineChartController(view)
        val tableView = TableView(view.context, view)
        val routeBarChartController = RouteBarChartController(view)
        //the Button Click Listener
        setLineChartBtn.setOnClickListener {
            resetButtonBackground()
            setLineChartBtn.setBackgroundColor(Colors.activeColor)
            routeLineChartController.show()
            tableView.hide()
            routeBarChartController.hide()
            routeLineChartController.createChart()
        }
        setBarChartBtn.setOnClickListener {
            resetButtonBackground()
            setBarChartBtn.setBackgroundColor(Colors.activeColor)
            tableView.hide()
            routeLineChartController.hide()
            routeBarChartController.show()
            routeBarChartController.createChart()
        }
        setTableBtn.setOnClickListener {
            resetButtonBackground()
            setTableBtn.setBackgroundColor(Colors.activeColor)
            routeBarChartController.hide()
            routeLineChartController.hide()
            tableView.show()
            tableView.createTableView()
        }
        //default visualisation
        setBarChartBtn.callOnClick()
        return view
    }

    //XAxis Formatter
    fun resetButtonBackground() {
        setLineChartBtn.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                R.color.buttonColor
            )
        )
        setBarChartBtn.setBackgroundColor(ContextCompat.getColor(view.context, R.color.buttonColor))
        setTableBtn.setBackgroundColor(ContextCompat.getColor(view.context, R.color.buttonColor))
    }

    override fun refreshData() {
        try {
            val routeLineChart = RouteLineChartController(view)
            val routeTable = TableView(view.context, view)
            val routeBarChartController = RouteBarChartController(view)
            routeBarChartController.createChart()
            routeLineChart.createChart()
            routeTable.createTableView()
        } catch (ignored: Exception) {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater)
        val appmenu = AppBarMenu(menu)
        appmenu.setItemVisebility(MenuValues.FILTER, true)
        appmenu.setItemVisebility(MenuValues.SEARCH, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        return if (item.groupId == R.id.filter_area + 1) {
            true
        } else super.onOptionsItemSelected(item)
    }
}