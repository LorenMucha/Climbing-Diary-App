package com.main.climbingdiary.dialog;
import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.abstraction.Tabs;
import com.main.climbingdiary.models.Route;

public class DialogFactory {
    public static void openAddRouteDialog(String _type){
        if(_type.equals(Tabs.PROJEKTE.getTitle())){
            AddProjektDialog addProjekt = AddProjektDialog.newInstance("Neues Projekt");
            addProjekt.show(MainActivity.getMainActivity().getSupportFragmentManager(), "fragment_add_Projekt");
        }else {
            AddRouteDialog addRoute = AddRouteDialog.newInstance("Neue Route");
            addRoute.show(MainActivity.getMainActivity().getSupportFragmentManager(), "fragment_add_Route");
        }
    }
    public static void openEditRouteDialog(String _type, int _id){
        if(_type.equals(Tabs.PROJEKTE.getTitle())){
            EditProjektDialog editDialog = EditProjektDialog.newInstance("Projekt bearbeiten", _id);
            editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
        }else {
            EditRouteDialog editDialog = EditRouteDialog.newInstance("Route bearbeiten", _id);
            editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
        }
    }
    public static void openEditRouteDialog(Route _route){
            EditRouteDialog editDialog = EditRouteDialog.newInstance("Route bearbeiten", _route);
            editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(), "edit");
    }
}
