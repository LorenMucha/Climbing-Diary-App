package com.main.climbingdiary.showcase

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.models.Alert
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity


class ShowCaseProvider(private val context: Context) {

    private val mainView by lazy { MainActivity.getMainActivity().window }
    private var tabLayout: TabLayout = mainView.findViewById(R.id.tabLayout)

    init {
        tabLayout.getTabAt(1)!!.select()
    }

    fun createShowCase() {
        /*AppPreferenceManager
            .getUsedFirstTime()
            .run {
                if (this) {
                    setAlert()
                }
            }.also {
                AppPreferenceManager.setIsUsedFirstTime(false)
            }*/
        setAlert()
    }

    private fun showIntro(title: Int, text: Int, viewId: Int, type: Int = 0) {
        GuideView.Builder(context)
            .setTargetView(mainView.findViewById(viewId))
            .setTitle(StringManager.getStringForId(title))
            .setContentText(StringManager.getStringForId(text))
            .setDismissType(DismissType.outside)
            .setGuideListener {
                when (type) {
                    1 -> {
                        showIntro(
                            R.string.showcase_addNewRouteHeader,
                            R.string.showcase_addNewRouteText,
                            R.id.floating_action_btn_add,
                            2
                        )
                    }
                    2-> {
                        showIntro(
                            R.string.showcase_searchBarHeader,
                            R.string.showcase_searchBarText,
                            R.id.action_search,
                            3
                        )
                    }
                    3->{
                        showIntro(
                            R.string.showcase_filterBarHeader,
                            R.string.showcase_filterBarText,
                            R.id.action_filter,
                            0
                        )
                    }
                }
            }
            .build()
            .show()
    }

    private fun setAlert() {
        val alert = AlertFactory.getAlert(
            context, Alert(
                title = getString(R.string.showcase_app_welcome),
                message = getString(R.string.showcase_question_text_show_case),
                dialogType = SweetAlertDialog.CUSTOM_IMAGE_TYPE
            )
        )
        alert.setCustomImage(R.drawable.waving_hand)
            .setConfirmText(StringManager.getStringForId(R.string.showcase_gerne))
            .setConfirmClickListener {
                it.let {
                    it.cancel()
                    showIntro(
                        R.string.showcase_header,
                        R.string.showcase_info,
                        R.id.toolbar,
                        1
                    )
                }
            }
            .setCancelButton(StringManager.getStringForId(R.string.app_no)) { it.cancel() }
            .show()
    }
}