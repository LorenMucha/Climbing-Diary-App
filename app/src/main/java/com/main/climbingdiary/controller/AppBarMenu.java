package com.main.climbingdiary.controller;

import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.models.Tabs;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.models.MenuValues;

import java.util.ArrayList;
import java.util.List;

public class AppBarMenu implements SearchView.OnQueryTextListener{
    private final int searchId = R.id.action_search;
    private final int sortId = R.id.action_sort;
    private final int filterId = R.id.action_filter;
    private final SearchManager searchManager;
    private final List<Integer> idList = new ArrayList<>();
    private final Menu menu;

    public AppBarMenu(Menu _menu){
        this.menu = _menu;
        Context context = MainActivity.getMainAppContext();
        searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        this.init();
    }

    private void init(){
        SearchView searchview = (SearchView) menu.findItem(searchId).getActionView();
        AreaRepository repo = AreaRepository.getInstance();
        searchview.setSearchableInfo(searchManager
                .getSearchableInfo(MainActivity.getMainComponentName()));
        searchview.setMaxWidth(Integer.MAX_VALUE);
        searchview.setOnQueryTextListener(this);
        idList.add(searchId);
        idList.add(sortId);
        idList.add(filterId);
        // Fill the Menu for Filtering
        int i =2;
        for(String areaName: repo.getAreaNameList()){
            menu.findItem(R.id.filter_area).getSubMenu().add(R.id.filter_area+1,i+R.id.filter_area,Menu.NONE,areaName);
            i++;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // filter recycler view when query submitted
        if(AppPreferenceManager.getSelectedTabsTitle() ==Tabs.PROJEKTE ){
            RouteProjectFragment.getAdapter().getFilter().filter(query);
        }else{
            RouteDoneFragment.getAdapter().getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("Query",query);
        // filter recycler view when text is changed
        if(AppPreferenceManager.getSelectedTabsTitle() ==Tabs.PROJEKTE ){
            RouteProjectFragment.getAdapter().getFilter().filter(query);
        }else{
            RouteDoneFragment.getAdapter().getFilter().filter(query);
        }
        return false;
    }

    public void setItemVisebility(MenuValues item, boolean state){
        int dateId = R.id.sort_date;
        switch(item.toString()){
            case "date":
                menu.findItem(dateId).setVisible(state);
                break;
            case "search":
                menu.findItem(searchId).setVisible(state);
                break;
            case "sort":
                menu.findItem(sortId).setVisible(state);
                break;
            case "filter":
                menu.findItem(filterId).setVisible(state);
                break;
        }
    }
    //overloading to hide all elements
    public void setItemVisebility(boolean state){
        for(Integer id:idList){
            menu.findItem(id).setVisible(state);
        }
    }
}
