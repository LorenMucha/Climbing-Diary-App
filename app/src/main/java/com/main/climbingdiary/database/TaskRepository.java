package com.main.climbingdiary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.sql.SqlAreaSektoren;
import com.main.climbingdiary.database.sql.SqlRouten;
import com.main.climbingdiary.database.sql.SqlStatistic;
import com.main.climbingdiary.models.RouteType;

public class TaskRepository {

    private static final String TAG = "DataAdapter";

    private final SQLiteDatabase mDb;
    private static TaskRepository INSTANCE = null;

    private TaskRepository() {
        DatabaseHelper mDbHelper = DatabaseHelper.getInstance();
        try {
            mDbHelper.openDataBase();
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public static synchronized TaskRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskRepository();
        }
        return INSTANCE;
    }

    public Cursor getAllRoutes() {
        return getCursor(SqlRouten.getRouteList(RouteType.ROUTE));
    }

    public Cursor getAllProjekts() {
        return getCursor(SqlRouten.getRouteList(RouteType.PROJEKT));
    }

    public Cursor getRoute(int id) {
        return getCursor(SqlRouten.getRoute(id));
    }

    public Cursor getProjekt(int id) {
        return getCursor(SqlRouten.getProjekt(id));
    }

    public Cursor getAllAreas() {
        return getCursor(SqlAreaSektoren.getAllAreas());
    }

    public Cursor getSectorListByAreaName(String name) {
        return getCursor(SqlAreaSektoren.getSectorByAreaName(name));
    }

    public Cursor getSectorIdByAreaNameAndSectorName(String sectorName, String areaName){
        return getCursor(SqlAreaSektoren.getSectorIdBySectorNameAndAreaName(sectorName,areaName));
    }

    public Cursor getAreaIdByAreaNameAndSectorName(String sectorName, String areaName){
        return getCursor(SqlAreaSektoren.getAreaIdBySectorNameAndAreaName(sectorName,areaName));
    }

    public Cursor getTopTenRoutes(int year) {
        return getCursor(SqlRouten.getTopTenRoutes(year));
    }

    public Cursor getYears(boolean filterSet) {
        return getCursor(SqlRouten.getYears(filterSet));
    }

    public Cursor getTableValues() {
        return getCursor(SqlStatistic.getTableValues());
    }

    public Cursor getBarChartValues() {
        return getCursor(SqlStatistic.getBarChartValues());
    }

    public boolean insertRoute(Route route) {
        //create the transaction
        return executeSqlTasks(SqlRouten.getInsertRouteTasks(route));
    }

    public boolean insertProjekt(Projekt projekt) {
        return executeSqlTasks(SqlRouten.getInsertProjektTasks(projekt));
    }

    public boolean updateRoute(Route route){
        return executeSqlTasks(new String[]{SqlRouten.updateRoute(route)});
    }

    public boolean deleteRoute(int id) {
        return executeSqlTasks(new String[]{SqlRouten.deleteRoute(id)});
    }

    public boolean deleteProjekt(int id) {
        return executeSqlTasks(new String[]{SqlRouten.deleteProjekt(id)});
    }

    public Cursor getCursor(String sql) {
        try {
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLExeption) {
            Log.e(TAG, "Error >>" + mSQLExeption.toString());
            throw mSQLExeption;
        }
    }

    public boolean executeSqlTasks(String[] tasks) {
        mDb.beginTransaction();
        try {
            for (String x : tasks) {
                Log.d("Execute", x);
                mDb.execSQL(x);
            }
            mDb.setTransactionSuccessful();
            return true;
        } catch (SQLiteException exception) {
            Log.e(TAG, "Error >>" + exception.toString());
            return false;
        } finally {
            mDb.endTransaction();
        }
    }
}
