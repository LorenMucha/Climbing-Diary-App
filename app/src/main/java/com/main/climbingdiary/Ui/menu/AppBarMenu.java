package com.main.climbingdiary.Ui.menu;

import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.RouteDoneFragment;
import com.main.climbingdiary.RouteProjectFragment;
import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.Ui.Tabs;
import com.main.climbingdiary.database.entities.Area;
import com.main.climbingdiary.database.entities.AreaRepository;

import java.util.ArrayList;
import java.util.List;

public class AppBarMenu implements SearchView.OnQueryTextListener{
    private final int searchId = R.id.action_search;
    private final int sortId = R.id.action_sort;
    private final int dateId = R.id.sort_date;
    private final int filterId = R.id.action_filter;
    private final SearchManager searchManager;
    private final List<Integer> idList = new ArrayList<>();
    private Menu menu;
    private SearchView searchview;
    private Context context;

    public AppBarMenu(Menu _menu){
        this.menu = _menu;
        context = MainActivity.getMainAppContext();
        searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        this.init();
    }

    private void init(){
        searchview = (SearchView) menu.findItem(searchId).getActionView();
        searchview.setSearchableInfo(searchManager
                .getSearchableInfo(MainActivity.getMainComponentName()));
        searchview.setMaxWidth(Integer.MAX_VALUE);
        searchview.setOnQueryTextListener(this);
        idList.add(searchId);
        idList.add(sortId);
        idList.add(filterId);
        // Fill the Menu for Filtering
        int i =2;
        for(String areaName: AreaRepository.getAreaNameList()){
            menu.findItem(R.id.filter_area).getSubMenu().add(R.id.filter_area+1,i+R.id.filter_area,Menu.NONE,areaName);
            i++;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Query",query);
        // filter recycler view when query submitted
        if(FragmentPager.getTabTitle().equals(Tabs.ROUTEN.getTitle())){
            RouteDoneFragment.getAdapter().getFilter().filter(query);
        }else{
            RouteProjectFragment.getAdapter().getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("Query",query);
        // filter recycler view when text is changed
        if(FragmentPager.getTabTitle().equals(Tabs.ROUTEN.getTitle())){
            RouteDoneFragment.getAdapter().getFilter().filter(query);
        }else{
            RouteProjectFragment.getAdapter().getFilter().filter(query);
        }
        return false;
    }

    public void setItemVisebility(MenuValues item, boolean state){
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
