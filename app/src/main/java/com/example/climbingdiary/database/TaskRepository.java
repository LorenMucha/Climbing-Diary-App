package com.example.climbingdiary.database;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskRepository {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    public TaskRepository (Context context)
    {
        this.mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public TaskRepository open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public Cursor getAllRoutes(String _order,String _filter)
    {
        String order_value =  "r.level";
        String filter = "";
        if(_order != null && !_order.isEmpty()){
            order_value = "r."+_order;
        }
        if(_filter != null && !_filter.isEmpty()){
            filter = "and "+_filter;
        }
        try
        {
            String sql ="SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor "+filter+" group by r.id Order By "+order_value+" DESC";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getAllRoutes >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getAllAreas(){
        try
        {
            String sql ="SELECT * FROM gebiete GROUP BY id";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getAllRoutes >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    public Cursor getSectorByAreaName(String _name){
        try
        {
            String sql ="SELECT s.name,s.koordinaten as koordinaten_sektor,a.koordinaten as koordinaten_area,s.gebiet,s.id FROM sektoren s, gebiete a where a.name Like '"+_name+"%' and s.gebiet=a.id GROUP BY s.id";
            Log.d("task",sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getAllRoutes >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
