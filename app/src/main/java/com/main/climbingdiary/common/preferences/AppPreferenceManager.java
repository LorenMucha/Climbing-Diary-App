package com.main.climbingdiary.common.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.FileUtil;
import com.main.climbingdiary.models.MenuValues;
import com.main.climbingdiary.models.RouteSort;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.models.Tabs;

import java.util.Objects;

public class AppPreferenceManager {

    private static final SharedPreferences PREFS = PreferenceManager.getDefaultSharedPreferences(MainActivity.getMainAppContext());
    private static final SharedPreferences.Editor EDITOR = PREFS.edit();


    public static synchronized void setOutputPath(String path){
        Uri treeUri = Uri.parse(path);
        String pathSet = FileUtil.getInstance().getFullPathFromTreeUri(treeUri);
        EDITOR.putString(PreferenceKeys.DB_OUTPUT_PATH,pathSet);
        EDITOR.apply();
    }
    public static synchronized String getOutputPath(){
        return PREFS.getString(PreferenceKeys.DB_OUTPUT_PATH,"not set");
    }

    public static synchronized void setSportType(SportType type){
        String _type = type.typeToString();
        EDITOR.putString(PreferenceKeys.SPORT, _type);
        EDITOR.apply();
    }

    public static synchronized SportType getSportType(){
        return SportType.stringToSportType(Objects.requireNonNull(PREFS.getString(PreferenceKeys.SPORT, "klettern")));
    }

    public static synchronized void setFilter(String filter){
        EDITOR.putString(PreferenceKeys.FILTER, filter);
        EDITOR.apply();
    }

    public static synchronized String getFilter(){
        return PREFS.getString(PreferenceKeys.FILTER,"");
    }

    public static synchronized RouteSort getSort(){
        return RouteSort.stringToSportType(PREFS.getString(PreferenceKeys.SORT,"date"));
    }

    public static synchronized void setSort(RouteSort sort){
        EDITOR.putString(PreferenceKeys.SORT, sort.typeToString());
        EDITOR.apply();
    }

    public static synchronized void setFilterSetter(MenuValues value){
        EDITOR.putString(PreferenceKeys.FILTER_MENU, value.typeToString());
        EDITOR.apply();
    }

    public static synchronized MenuValues getFilterSetter(){
        return MenuValues.stringToSportType(Objects.requireNonNull(PREFS.getString(PreferenceKeys.FILTER_MENU, "date")));
    }

    public static synchronized void removeAllFilterPrefs(){
        PREFS.edit().remove(PreferenceKeys.FILTER).apply();
        PREFS.edit().remove(PreferenceKeys.FILTER_MENU).apply();
    }

    public static synchronized void removeAllTempPrefs(){
        removeAllFilterPrefs();
        PREFS.edit().remove(PreferenceKeys.SORT).apply();
        PREFS.edit().remove(PreferenceKeys.TAB).apply();
        PREFS.edit().remove(PreferenceKeys.SPORT).apply();
    }

    public static synchronized void setSelectedTabsTitle(Tabs tab){
        EDITOR.putString(PreferenceKeys.TAB, tab.typeToString());
        EDITOR.apply();
    }

    public static synchronized Tabs getSelectedTabsTitle(){
        return Tabs.stringToTabs(PREFS.getString(PreferenceKeys.TAB,""));
    }
}
