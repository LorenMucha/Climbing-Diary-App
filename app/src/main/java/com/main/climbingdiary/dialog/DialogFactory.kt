package com.main.climbingdiary.dialog

import androidx.fragment.app.FragmentManager
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.model.Tabs

object DialogFactory {

    fun openAddRouteDialog(tab: Tabs) {
        val manager: FragmentManager = MainActivity.getApplication()!!.supportFragmentManager
        if (tab === Tabs.PROJEKTE) {
            val addProjekt = AddProjektDialog("Neues Projekt")
            addProjekt.show(
                manager,
                "fragment_add_Projekt"
            )
        } else {
            val addRoute = AddRouteDialog("Neue Route")
            addRoute.show(
                manager,
                "fragment_add_Route"
            )
        }
    }

    fun openEditRouteDialog(tab: Tabs, _id: Int) {
        if (tab === Tabs.PROJEKTE) {
            val editDialog = EditProjektDialog("Projekt bearbeiten", _id)
            editDialog.show(MainActivity.getApplication()!!.supportFragmentManager, "edit")
        } else {
            val editDialog = EditRouteDialog("Route bearbeiten", _id)
            editDialog.show(MainActivity.getApplication()!!.supportFragmentManager, "edit")
        }
    }

    fun openEditRouteDialog(_route: Route) {
        val editDialog = EditRouteDialog("Route bearbeiten", _route)
        editDialog.show(MainActivity.getApplication()!!.supportFragmentManager, "edit")
    }
}