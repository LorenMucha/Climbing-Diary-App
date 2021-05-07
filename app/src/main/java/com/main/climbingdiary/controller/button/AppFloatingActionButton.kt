package com.main.climbingdiary.controller.button

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.dialog.DialogFactory
import com.main.climbingdiary.model.Tabs

object AppFloatingActionButton : View.OnClickListener {

    private val application = MainActivity.getApplication()
    private val addButton: FloatingActionButton =
        application!!.findViewById(R.id.floating_action_btn_add)
    private val locateButton: FloatingActionButton =
        application!!.findViewById(R.id.floating_action_btn_locate)

    init {
        addButton.setOnClickListener(this)
        locateButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        DialogFactory.openAddRouteDialog(AppPreferenceManager.getSelectedTabsTitle())
    }

    fun show() {
        hide()
        if (AppPreferenceManager.getSelectedTabsTitle() === Tabs.MAP) {
            locateButton.show()
        } else {
            addButton.show()
        }
    }

    fun hide() {
        locateButton.hide()
        addButton.show()
    }
}