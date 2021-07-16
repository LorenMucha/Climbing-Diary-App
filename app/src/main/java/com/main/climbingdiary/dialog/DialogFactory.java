package com.main.climbingdiary.dialog;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.models.Tabs;

public class DialogFactory {

    public static void openAddRouteDialog(Tabs tab) {
        if (tab == Tabs.PROJEKTE) {
            AddProjektDialog addProjekt = new AddProjektDialog("Neues Projekt");
            addProjekt.show(MainActivity.getMainActivity().getSupportFragmentManager(), "fragment_add_Projekt");
        } else {
            AddRouteDialog addRoute = new AddRouteDialog("Neue Route");
            addRoute.show(MainActivity.getMainActivity().getSupportFragmentManager(), "fragment_add_Route");
        }
    }

    public static void openEditRouteDialog(Tabs tab, int _id) {
        if (tab == Tabs.PROJEKTE) {
            EditProjektDialog editDialog = new EditProjektDialog("Projekt bearbeiten", _id);
            editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
        } else {
            EditRouteDialog editDialog = new EditRouteDialog("Route bearbeiten", _id);
            editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
        }
    }

    public static void openEditRouteDialog(Route _route) {
        EditRouteDialog editDialog = new EditRouteDialog("Route bearbeiten", _route);
        editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
    }
}
