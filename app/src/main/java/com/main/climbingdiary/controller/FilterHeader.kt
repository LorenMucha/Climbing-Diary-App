package com.main.climbingdiary.controller

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.main.climbingdiary.R
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getFilterSetter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.removeAllFilterPrefs
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilterSetter
import com.main.climbingdiary.fragments.RouteFragment
import com.main.climbingdiary.models.MenuValues
import java.util.*

class FilterHeader(val routeFragment: RouteFragment): View.OnClickListener {
    private val view: View = routeFragment.view
    private val layout: LinearLayout = view.findViewById(R.id.filter_header)
    private val textView: TextView = view.findViewById(R.id.filter_header_txt)
    private val imageView: ImageView = view.findViewById(R.id.filter_image)
    private var filterText: String = ""

    init {
        layout.setOnClickListener(this)
    }

    fun show() {
        show(filterText)
    }

    fun show(value: String) {
        layout.visibility = View.VISIBLE
        filterText = value
        textView.text = filterText
        setFilter(String.format("g.name like '%s'", filterText.toLowerCase(Locale.ROOT)))
        setFilterSetter(MenuValues.FILTER)
        imageView.setImageResource(R.drawable.ic_filter_active)
        routeFragment.refreshData()
    }

    fun hide() {
        layout.visibility = View.GONE
        if (getFilterSetter() === MenuValues.FILTER) {
            removeAllFilterPrefs()
        }
        imageView.setImageResource(R.drawable.ic_filter)
        filterText = ""
        routeFragment.refreshData()
    }

    override fun onClick(v: View?) {
        hide()
    }
}