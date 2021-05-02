package com.main.climbingdiary.dialog

import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.model.Tabs

object DialogFactory {

    fun openAddRouteDialog(tab: Tabs){
        if (tab === Tabs.PROJEKTE) {
            val addProjekt = AddProjektDialog("Neues Projekt")
            addProjekt.show(
                MainActivity.getMainActivity().getSupportFragmentManager(),
                "fragment_add_Projekt"
            )
        } else {
            val addRoute = AddRouteDialog("Neue Route")
            addRoute.show(
                MainActivity.getMainActivity().getSupportFragmentManager(),
                "fragment_add_Route"
            )
        }
    }
}