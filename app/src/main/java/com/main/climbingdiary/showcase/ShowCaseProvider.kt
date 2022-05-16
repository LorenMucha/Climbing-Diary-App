package com.main.climbingdiary.showcase

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.erkutaras.showcaseview.ShowcaseManager
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.models.Alert


class ShowCaseProvider(private val context: Context) {

    private val mainView by lazy {
        MainActivity.getMainActivity().window
    }

    init {
        val tabLayout = mainView.findViewById<TabLayout>(R.id.tabLayout)
        val tab = tabLayout.getTabAt(1)
        tab!!.select()
    }

    fun createShowCase() {
        AppPreferenceManager
            .getUsedFirstTime()
            .run {
                //if (this) {
                setAlert()
                //}
            }.also {
                AppPreferenceManager.setIsUsedFirstTime(false)
            }
    }

    private fun viewWelcomeScreen() {
        ShowcaseManager.Builder()
            .context(context)
            .view(mainView.findViewById(R.id.toolbar))
            .developerMode(true)
            .descriptionImageRes(R.mipmap.ic_launcher_round)
            .descriptionTitle(getString(R.string.showcase_header))
            .descriptionText(getString(R.string.showcase_info))
            .buttonText(getString(R.string.showcase_further))
            .key("Welcome")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
            .view(mainView.findViewById(R.id.tabLayout))
            .descriptionImageRes(R.drawable.tabkey)
            .descriptionTitle("Auswahl Menü")
            .descriptionText("Zeige dir deine Statistik an, verwalte deine gekletterten Touren und füge Projekte hinzu.")
            .buttonText(getString(R.string.showcase_further))
            .key("Statistik")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
            .view(mainView.findViewById(R.id.floating_action_btn_add))
            .descriptionImageRes(R.drawable.ic_plus)
            .descriptionTitle(getString(R.string.showcase_addNewRouteHeader))
            .descriptionText(getString(R.string.showcase_addNewRouteText))
            .buttonText(getString(R.string.showcase_further))
            .key("addRoute")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
            .view(mainView.findViewById(R.id.action_search))
            .descriptionImageRes(android.R.drawable.ic_menu_search)
            .descriptionTitle(getString(R.string.showcase_searchBarHeader))
            .descriptionText(getString(R.string.showcase_searchBarText))
            .buttonText(getString(R.string.showcase_further))
            .key("searchRoute")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
            .view(mainView.findViewById(R.id.action_filter))
            .descriptionImageRes(R.drawable.ic_filter)
            .descriptionTitle(getString(R.string.showcase_filterBarHeader))
            .descriptionText(getString(R.string.showcase_filterBarText))
            .buttonText(getString(R.string.showcase_further))
            .key("filterRoute")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
            //Fixme
            .view(mainView.findViewById(R.id.app_settings))
            .descriptionImageRes(R.drawable.ellipsis)
            .descriptionTitle("Einstellungen")
            .descriptionText("Exportiere deine Routen oder stelle Sie wieder her, ein Wechsel der Sprache ist auch möglich.")
            .buttonText(getString(R.string.showcase_further))
            .key("Statistik")
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
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
            .setConfirmText(getString(R.string.showcase_gerne))
            .setConfirmClickListener {
                it.let {
                    it.cancel()
                    viewWelcomeScreen()
                }
            }
            .setCancelButton("Nein") { it.cancel() }
            .show()
    }
}