package com.main.climbingdiary.controller.button

import android.annotation.SuppressLint
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSelectedTabsTitle
import com.main.climbingdiary.dialog.DialogFactory
import com.main.climbingdiary.models.Tabs

@SuppressLint("StaticFieldLeak")
object AppFloatingActionButton: View.OnClickListener {

    val activity: AppCompatActivity by lazy {MainActivity.getMainActivity()}
    private val floatingActionButtonAdd: FloatingActionButton = activity
        .findViewById(R.id.floating_action_btn_add)
    private val floatingActionButtonLocate: FloatingActionButton = activity
        .findViewById(R.id.floating_action_btn_locate)

    init {
        floatingActionButtonAdd.setOnClickListener(this)
        floatingActionButtonLocate.setOnClickListener(this)
    }

    @JvmStatic
    fun show() {
        hide()
        if (getSelectedTabsTitle() === Tabs.MAP) {
            floatingActionButtonLocate.show()
        } else {
            floatingActionButtonAdd.show()
        }
    }

    @JvmStatic
    fun hide() {
        floatingActionButtonAdd.hide()
        floatingActionButtonLocate.hide()
    }


    override fun onClick(v: View?) {
        getSelectedTabsTitle()?.let { DialogFactory.openAddRouteDialog(it) }
    }
}