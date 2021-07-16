package com.main.climbingdiary.database.entities

import android.database.Cursor
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getAllAreas
import org.chalup.microorm.MicroOrm
import java.util.ArrayList

object AreaRepository {

    private val uOrm = MicroOrm()

    fun getAreaByAreaNameAndSectorName(sectorName: String?, areaName: String?): Area {
        val cursor: Cursor = TaskRepository.getAreaIdByAreaNameAndSectorName(sectorName, areaName)
        return uOrm.fromCursor(cursor, Area::class.java)
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