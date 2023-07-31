package com.main.climbingdiary.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Area
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.Sector
import com.main.climbingdiary.database.sql.SqlAreaSektoren
import com.main.climbingdiary.database.sql.SqlRouten
import com.main.climbingdiary.database.sql.SqlRouten.getInsertProjektTasks
import com.main.climbingdiary.database.sql.SqlRouten.getInsertRouteTasks
import com.main.climbingdiary.database.sql.SqlRouten.getRouteList
import com.main.climbingdiary.database.sql.SqlStatistic
import com.main.climbingdiary.models.RouteType
import java.nio.charset.StandardCharsets

object TaskRepository {
    private val TAG = "DataAdapter"
    private val mDb: SQLiteDatabase
    private val context: Context by lazy { MainActivity.getMainAppContext() }

    init {
        val mDbHelper = DatabaseHelper(context, null)
        mDbHelper.openDataBase()
        mDb = mDbHelper.writableDatabase
    }

    fun getAllRoutes(): Cursor {
        return getCursor(getRouteList(RouteType.ROUTE))
    }

    fun getAllProjekts(): Cursor {
        return getCursor(getRouteList(RouteType.PROJEKT))
    }

    fun getRoute(id: Int): Cursor {
        return getCursor(SqlRouten.getRoute(id))
    }

    fun getProjekt(id: Int): Cursor {
        return getCursor(SqlRouten.getProjekt(id))
    }

    fun getAllAreas(): Cursor {
        return getCursor(SqlAreaSektoren.getAllAreas())
    }

    fun getSectorListByAreaName(name: String): Cursor {
        return getCursor(SqlAreaSektoren.getSectorByAreaName(name))
    }

    fun getSectorIdByAreaNameAndSectorName(sectorName: String?, areaName: String?): Cursor {
        return getCursor(SqlAreaSektoren.getSectorIdBySectorNameAndAreaName(sectorName, areaName))
    }

    fun getAreaIdByAreaNameAndSectorName(sectorName: String?, areaName: String?): Cursor {
        return getCursor(SqlAreaSektoren.getAreaIdBySectorNameAndAreaName(sectorName, areaName))
    }

    fun getAreaByAreaName(areaName: String?): Cursor {
        return getCursor(SqlAreaSektoren.getAreaByName(areaName))
    }

    fun getTopTenRoutes(year: Int): Cursor {
        return getCursor(SqlRouten.getTopTenRoutes(year))
    }

    fun getYears(filterSet: Boolean): Cursor {
        return getCursor(SqlRouten.getYears(filterSet))
    }

    fun getTableValues(): Cursor {
        return getCursor(SqlStatistic.getTableValues())
    }

    fun getBarChartValues(): Cursor {
        return getCursor(SqlStatistic.getBarChartValues())
    }

    fun insertRoute(route: Route): Boolean {
        //create the transaction
        return executeSqlTasks(getInsertRouteTasks(route))
    }

    fun insertArea(area: Area): Boolean {
        return executeSqlTasks(arrayOf(SqlAreaSektoren.insertArea(area)))
    }

    fun insertSector(sector: Sector): Boolean {
        return executeSqlTasks(arrayOf(SqlAreaSektoren.insertSector(sector)))
    }

    fun insertProjekt(projekt: Projekt): Boolean {
        return executeSqlTasks(getInsertProjektTasks(projekt))
    }

    fun updateRoute(route: Route): Boolean {
        return executeSqlTasks(arrayOf(SqlRouten.updateRoute(route)))
    }

    fun deleteRoute(id: Int): Boolean {
        return executeSqlTasks(arrayOf(SqlRouten.deleteRoute(id)))
    }

    fun deleteProjekt(id: Int): Boolean {
        return executeSqlTasks(arrayOf(SqlRouten.deleteProjekt(id)))
    }

    fun cleanDatabase():Boolean{
        val tasks = arrayOf(
            "DELETE FROM routen_bouldern",
            "DELETE FROM routen_klettern",
            "DELETE FROM projekte_bouldern",
            "DELETE FROM projekte_klettern",
            "DELETE FROM gebiete_klettern",
            "DELETE FROM gebiete_bouldern",
            "DELETE FROM sektoren_bouldern",
            "DELETE FROM sektoren_klettern"
        )
        return executeSqlTasks(tasks)
    }

    @SuppressLint("Recycle")
    fun getCursor(sql: String): Cursor {
        return try {
            val mCur = mDb.rawQuery(sql, null)
            mCur.moveToNext()
            mCur
        } catch (mSQLExeption: SQLException) {
            Log.e(TAG, "Error >>$mSQLExeption")
            throw mSQLExeption
        }
    }

    fun executeSqlTasks(tasks: Array<String>): Boolean {
        mDb.beginTransaction()
        return try {
            for (x in tasks) {
                Log.d("Execute", x)
                val query = x.trimIndent()
                mDb.execSQL(String(query.toByteArray(), StandardCharsets.UTF_8))
            }
            mDb.setTransactionSuccessful()
            true
        } catch (exception: SQLiteException) {
            Log.e(TAG, "Error >>$exception")
            false
        } finally {
            mDb.endTransaction()
        }
    }
}