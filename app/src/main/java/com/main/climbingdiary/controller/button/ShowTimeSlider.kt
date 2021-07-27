package com.main.climbingdiary.controller.button

import android.R
import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.main.climbingdiary.activities.MainActivity

@SuppressLint("StaticFieldLeak")
object ShowTimeSlider : View.OnClickListener {

    val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val imageButton: ImageButton =
        activity.findViewById(com.main.climbingdiary.R.id.showTimeSlider)
    private val layout: RelativeLayout =
        activity.findViewById(com.main.climbingdiary.R.id.showTimeLayout)
    private val container: LinearLayout =
        activity.findViewById(com.main.climbingdiary.R.id.sliderLayout)
    private var clickSet = 0

    init {
        imageButton.setOnClickListener(this)
    }

    @JvmStatic
    fun show() {
        container.visibility = View.VISIBLE
        showButton()
    }

    @JvmStatic
    fun hide() {
        container.visibility = View.GONE
    }

    @JvmStatic
    fun hideButton() {
        imageButton.visibility = View.GONE
    }

    @JvmStatic
    fun showButton() {
        imageButton.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        if (clickSet == 0) {
            layout.visibility = View.VISIBLE
            imageButton.setImageResource(R.drawable.arrow_down_float)
            container.z = 1000f
            clickSet++
        } else {
            layout.visibility = View.GONE
            imageButton.setImageResource(R.drawable.arrow_up_float)
            clickSet = 0
        }
    }
}