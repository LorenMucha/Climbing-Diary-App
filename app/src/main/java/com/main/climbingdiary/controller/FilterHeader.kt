package com.main.climbingdiary.controller

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.fragments.RouteFragment
import com.main.climbingdiary.model.MenuValues

class FilterHeader(private val fragment: RouteFragment) : View.OnClickListener {

    private val layout:LinearLayout
    private val text:TextView
    private val image: ImageView
    private var filterText:String = ""

    init{
        val view = fragment.getView()
        layout = view.findViewById(R.id.filter_header)
        image = view.findViewById(R.id.filter_image)
        text = view.findViewById(R.id.filter_header_txt)
        layout.setOnClickListener(this@FilterHeader)
    }

    fun show() {
        show(filterText)
    }

    fun show(value: String) {
        layout.visibility = View.VISIBLE
        filterText = value
        text.text = filterText
        AppPreferenceManager.setFilter(String.format("g.name like '%s'", filterText.toLowerCase()))
        AppPreferenceManager.setFilterSetter(MenuValues.FILTER)
        image.setImageResource(R.drawable.ic_filter_active)
        fragment.refreshData()
    }

    fun hide() {
        layout.visibility = View.GONE
        if (AppPreferenceManager.getFilterSetter() === MenuValues.FILTER) {
            AppPreferenceManager.removeAllFilterPrefs()
        }
        image.setImageResource(R.drawable.ic_filter)
        filterText = ""
        fragment.refreshData()
    }

    override fun onClick(v: View?) {
        hide()
    }


}