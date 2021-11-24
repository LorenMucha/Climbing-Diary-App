package com.main.climbingdiary.database.entities

import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import com.main.climbingdiary.database.TaskRepository
import org.chalup.microorm.MicroOrm
import java.util.*

object SectorRepository {

    private val uOrm = MicroOrm()

    fun insertSector(sector: Sector): Boolean {
        return TaskRepository.insertSector(sector)
    }

    fun getSectorByAreaNameAndSectorName(sectorName: String, areaName: String): Sector? {
        //Fixme: return null if not exists
        try {
            val cursor: Cursor =
                TaskRepository.getSectorIdByAreaNameAndSectorName(sectorName, areaName)
            return uOrm.fromCursor(cursor, Sector::class.java)
        } catch (ex: CursorIndexOutOfBoundsException) {
            return null
        }
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