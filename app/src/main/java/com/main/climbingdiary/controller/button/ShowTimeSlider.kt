package com.main.climbingdiary.controller.button

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity

@SuppressLint("StaticFieldLeak")
object ShowTimeSlider : View.OnClickListener {

    private val application = MainActivity.getApplication()
    private val imageButton: ImageButton = application!!.findViewById(R.id.showTimeSlider)
    private val layout: RelativeLayout = application!!.findViewById(R.id.showTimeLayout)
    private val container: LinearLayout = application!!.findViewById(R.id.sliderLayout)
    private var clickSet = 0

    init {
        imageButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (clickSet == 0) {
            layout.visibility = View.VISIBLE
            imageButton.setImageResource(android.R.drawable.arrow_down_float)
            container.z = 1000f
            clickSet++
        } else {
            layout.visibility = View.GONE
            imageButton.setImageResource(android.R.drawable.arrow_up_float)
            clickSet = 0
        }
    }

    fun show() {
        container.visibility = View.VISIBLE
        buttonIsVisible()
    }

    fun hide() {
        container.visibility = View.GONE
        buttonIsHidden()
    }

    fun buttonIsVisible() {
        imageButton.visibility = View.VISIBLE
    }

    fun buttonIsHidden() {
        imageButton.visibility = View.GONE
    }
}