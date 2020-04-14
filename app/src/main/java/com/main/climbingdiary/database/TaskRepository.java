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
import com.main.climbingdiary.models.Filter;

public class TaskRepository {

    private static final String TAG = "DataAdapter";

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private static TaskRepository INSTANCE;

    private TaskRepository() {
        Context mContext = MainActivity.getMainAppContext();
        mDbHelper = new DatabaseHelper(mContext);
        try {
            mDbHelper.openDataBase();
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public static TaskRepository getInstance() {
        if(INSTANCE == null){
            INSTANCE = new TaskRepository();
        }
        return INSTANCE;
    }

    public Cursor getAllRoutes() {
        try {
            String sql = RouteSQL.ROUTELIST.getSQL();
            Log.d("SQL RouteList", sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getAllRoutes >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getAllProjekts() {
        try {
            String sql = RouteSQL.PROJEKTLIST.getSQL();
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getAllProjekts >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getRoute(int _id) {
        String sql = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor AND r.id=" + _id;
        Log.d("Get Route",sql);
        Cursor mCur = mDb.rawQuery(sql, null);
        if (mCur != null) {
            mCur.moveToNext();
        }
        return mCur;
    }

    public Cursor getProjekt(int _id) {
        String sql = "SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM projekte r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor AND r.id=" + _id;

        Cursor mCur = mDb.rawQuery(sql, null);
        if (mCur != null) {
            mCur.moveToNext();
        }
        return mCur;
    }

    public Cursor getAllAreas() {
        String sql = "SELECT * FROM gebiete Order By Name";

        Cursor mCur = mDb.rawQuery(sql, null);
        if (mCur != null) {
            mCur.moveToNext();
        }
        return mCur;
    }

    public Cursor getSectorByAreaName(String _name) {
        try {
            String sql = "SELECT s.name, s.gebiet,s.id FROM sektoren s, gebiete a where a.name Like '" + _name + "%' and s.gebiet=a.id GROUP BY s.id";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getSectorByAreaName >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getTopTenRoutes(int year) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select r.level, r.stil, r.name, r.gebiet FROM routen r")
                    .append(" where CAST(strftime('%Y',r.date) as int)==")
                    .append(year)
                    .append(" Order By r.level desc, r.stil asc Limit 10");
            Log.d("query LineChartValues", sql.toString());
            Cursor mCur = mDb.rawQuery(sql.toString(), null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;

        } catch (SQLException mSQLExeption) {
            Log.e(TAG, "getBarChartValues >>" + mSQLExeption.toString());
            throw mSQLExeption;
        }
    }

    public Cursor getYears() {
        try {
            String sql = "select DISTINCT(strftime('%Y',date)) as year from routen order by date DESC";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getYears >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getTableValues() {
        String filter = Filter.getFilter(true);
        String replacement_sort = "r.date";
        if (!filter.isEmpty()) {
            filter = "and " + Filter.getFilter();
        }
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT r.level,")
                    .append(String.format("(Select count(*) as OS from routen o join gebiete g on g.id=o.gebiet where r.level=o.level and o.stil='OS' %s) as OS,",filter.replace(replacement_sort,"o.date")))
                    .append(String.format("(Select count(*) as RP from routen x join gebiete g on g.id=x.gebiet where x.level=r.level and x.stil='RP' %s) as RP,",filter.replace(replacement_sort,"x.date")))
                    .append(String.format("(Select count(*) as FLASH from routen f join gebiete g on g.id=f.gebiet where r.level=f.level and f.stil='FLASH' %s) as FLASH,",filter.replace(replacement_sort,"f.date")))
                    .append(String.format("(Select count(*) as GESAMT from routen z join gebiete g on g.id=z.gebiet where r.level=z.level %s) as GESAMT",filter.replace(replacement_sort,"z.date")))
                    .append(" from routen r")
                    .append(" where os >0 or rp >0 or flash > 0")
                    .append(" group by r.level order by r.level DESC");
            Log.d("SQL Table: ", sql.toString());
            Cursor mCur = mDb.rawQuery(sql.toString(), null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;

        } catch (SQLException mSQLExeption) {
            Log.e(TAG, "getTableValues >>" + mSQLExeption.toString());
            throw mSQLExeption;
        }
    }

    public Cursor getBarChartValues() {
        String filter_set = Filter.getFilter(true);
        if (!filter_set.isEmpty()) {
            filter_set = " where " + Filter.getFilter();
        }
        try {
            String sql = String.format("select r.level,sum(r.stil='RP') as rp, sum(r.stil='OS') as os,sum(r.stil='FLASH') as flash from routen r join gebiete g on g.id=r.gebiet %s group by r.level",filter_set);
            Log.d("SQL getBarChartValues", sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;


        } catch (SQLException mSQLExeption) {
            Log.e(TAG, "getBarChartValues >>" + mSQLExeption.toString());
            throw mSQLExeption;
        }
    }

    public boolean inserRoute(Route route) {
        //create the transaction
        String[] tasks = {
                "INSERT OR IGNORE INTO gebiete (name) VALUES ('" + route.getArea() + "')",
                "INSERT OR IGNORE INTO sektoren (name,gebiet) " +
                        "SELECT '" + route.getSector() + "',id FROM gebiete WHERE name='" + route.getArea() + "'",
                "INSERT OR IGNORE INTO routen (date,name,level,stil,rating,kommentar,gebiet,sektor) " +
                        "SELECT '" + route.getDate() + "','" + route.getName() + "','" + route.getLevel() + "','" + route.getStyle() + "','" + route.getRating() + "','" + route.getComment() + "',a.id,s.id " +
                        "FROM gebiete a, sektoren s " +
                        "WHERE a.name = '" + route.getArea() + "'" +
                        "AND s.name='" + route.getSector() + "'"
        };
        boolean state;
        mDb.beginTransaction();
        try {
            for (String x : tasks) {
                mDb.execSQL(x);
            }
            mDb.setTransactionSuccessful();
            state = true;
        } catch (SQLiteException exception) {
            state = false;
        } finally {
            mDb.endTransaction();
        }
        return state;
    }

    public boolean inserProjekt(Projekt projekt) {
        //create the transaction
        String[] tasks = {
                "INSERT OR IGNORE INTO gebiete (name) VALUES ('" + projekt.getArea() + "')",
                "INSERT OR IGNORE INTO sektoren (name,gebiet) " +
                        "SELECT '" + projekt.getSector() + "',id FROM gebiete WHERE name='" + projekt.getArea() + "'",
                "INSERT OR IGNORE INTO projekte (name,level,rating,kommentar,gebiet,sektor) " +
                        "SELECT '" + projekt.getName() + "','" + projekt.getLevel() + "','" + projekt.getRating() + "','" + projekt.getComment() + "',a.id,s.id " +
                        "FROM gebiete a, sektoren s " +
                        "WHERE a.name = '" + projekt.getArea() + "'" +
                        "AND s.name='" + projekt.getSector() + "'"
        };
        boolean state;
        mDb.beginTransaction();
        try {
            for (String x : tasks) {
                Log.d("insert", x);
                mDb.execSQL(x);
            }
            mDb.setTransactionSuccessful();
            state = true;
        } catch (SQLiteException exception) {
            state = false;
        } finally {
            mDb.endTransaction();
        }
        return state;
    }

    public boolean deleteRoute(int id) {
        final String sql = "DELETE FROM routen WHERE id=" + id;
        State state = () -> {
            try {
                mDb.execSQL(sql);
                return true;
            } catch (SQLiteException exception) {
                return false;
            }
        };
        return state.getState();
    }

    public boolean deleteProjekt(int id) {
        Log.d("DELETE Projekt", Integer.toString(id));
        final String sql = "DELETE FROM projekte WHERE id=" + id;
        State state = () -> {
            try {
                mDb.execSQL(sql);
                return true;
            } catch (SQLiteException exception) {
                return false;
            }
        };
        return state.getState();
    }
}
