package com.main.climbingdiary.database.entities

import android.database.Cursor
import com.main.climbingdiary.database.TaskRepository
import org.chalup.microorm.MicroOrm
import java.util.*

object SectorRepository {

    private val uOrm = MicroOrm()

    fun getSectorByAreaNameAndSectorName(sectorName: String, areaName: String): Sector {
        val cursor: Cursor? =
            TaskRepository.getSectorIdByAreaNameAndSectorName(sectorName, areaName)
        return uOrm.fromCursor(cursor, Sector::class.java)
    }

    fun getSectorList(_area_name: String): ArrayList<String> {
        val sectorList = ArrayList<String>()
        val cursor: Cursor? = TaskRepository.getSectorListByAreaName(_area_name)
        if (cursor != null) {
            while (!cursor.isAfterLast) {
                val name = cursor.getString(0)
                sectorList.add(name)
                cursor.moveToNext()
            }
        }
        return sectorList
    }
}