package com.main.climbingdiary.dialog

import android.support.v4.app.FragmentManager
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.models.Tabs

object DialogFactory {

    private val manager: FragmentManager by lazy { MainActivity.getMainActivity().supportFragmentManager }

    @JvmStatic
    fun openAddRouteDialog(tab: Tabs) {
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
    @JvmStatic
    fun openEditRouteDialog(tab: Tabs, id: Int) {
        if (tab === Tabs.PROJEKTE) {
            val editDialog = EditProjektDialog("Projekt bearbeiten", id)
            editDialog.show(manager, "edit")
        } else {
            val editDialog = EditRouteDialog("Route bearbeiten", id)
            editDialog.show(manager, "edit")
        }
    }
    @JvmStatic
    fun openEditRouteDialog(route: Route) {
        val editDialog = EditRouteDialog("Route bearbeiten", route.id)
        editDialog.show(manager, "edit")
    }
}