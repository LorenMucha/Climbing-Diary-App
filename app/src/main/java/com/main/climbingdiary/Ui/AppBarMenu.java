package com.main.climbingdiary.Ui;

import android.app.SearchManager;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.RoutesFragment;

public class AppBarMenu implements SearchView.OnQueryTextListener{
    private final int searchId = R.id.action_search;
    private final int sortId = R.id.action_sort;
    private Menu menu;
    private SearchView searchview;
    private Context context;

    public AppBarMenu(Menu _menu){
        this.menu = _menu;
        context = MainActivity.getMainAppContext();
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchview = (SearchView) menu.findItem(searchId).getActionView();
        searchview.setSearchableInfo(searchManager
                .getSearchableInfo(MainActivity.getMainComponentName()));
        searchview.setMaxWidth(Integer.MAX_VALUE);
        searchview.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Query",query);
        // filter recycler view when query submitted
        RoutesFragment.getAdapter().getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // filter recycler view when text is changed
        RoutesFragment.getAdapter().getFilter().filter(query);
        return false;
    }

    public void hideItem(String item){
        switch(item){
            case "search":
                menu.findItem(searchId).setVisible(false);
                break;
            case "sort":
                menu.findItem(sortId).setVisible(false);
                break;
        }
    }
    //overloading to hide all elements
    public void hideItem(){
        menu.findItem(searchId).setVisible(false);
        menu.findItem(sortId).setVisible(false);
    }

    public void showItem(String item){
        switch(item){
            case "search":
                menu.findItem(searchId).setVisible(true);
                break;
            case "sort":
                menu.findItem(sortId).setVisible(true);
                break;
        }
    }
    public void showItem(){
        menu.findItem(searchId).setVisible(true);
        menu.findItem(sortId).setVisible(true);
    }
}
