package com.main.climbingdiary.database.entities

import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import com.main.climbingdiary.database.TaskRepository
import org.chalup.microorm.MicroOrm

object AreaRepository {

    private val uOrm = MicroOrm()

    fun getAreaByAreaNameAndSectorName(sectorName: String?, areaName: String?): Area? {
        try {
            val cursor: Cursor =
                TaskRepository.getAreaIdByAreaNameAndSectorName(sectorName, areaName)
            return uOrm.fromCursor(cursor, Area::class.java)
        } catch (ex: CursorIndexOutOfBoundsException) {
            return null
        }
    }

    fun insertArea(area: Area): Boolean {
        return TaskRepository.insertArea(area)
    }

    fun getAreaByName(name: String): Area? {
        //Fixme: return null if not exists
        try {
            val cursor: Cursor = TaskRepository.getAreaByAreaName(name)
            return uOrm.fromCursor(cursor, Area::class.java)
        } catch (ex: CursorIndexOutOfBoundsException) {
            return null
        }
    }

    fun getAreaList(): ArrayList<Area> {
        val area_list = ArrayList<Area>()
        val cursor: Cursor = TaskRepository.getAllAreas()
        while (cursor.moveToNext()) {
            area_list.add(
                uOrm.fromCursor(
                    cursor,
                    Area::class.java
                )
            )
        }
        return area_list
    }

    fun getAreaNameList(): ArrayList<String> {
        val area_list = ArrayList<String>()
        val cursor: Cursor = TaskRepository.getAllAreas()
        while (!cursor.isAfterLast) {
            val name = cursor.getString(1)
            area_list.add(name)
            cursor.moveToNext()
        }
        return area_list
    }
}