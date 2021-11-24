package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.entities.Area
import com.main.climbingdiary.database.entities.Sector
import java.util.*

object SqlAreaSektoren {

    fun insertArea(area: Area): String {
        return "INSERT OR IGNORE INTO ${getAreaTableName()} (name) VALUES ('${area.name}')"
    }

    fun insertSector(sector: Sector): String {
        return """INSERT OR IGNORE INTO ${getSektorenTableName()} (name,gebiet)
               | SELECT '${sector.name}',id FROM ${getAreaTableName()} 
               | WHERE id=${sector.area}""".trimMargin()
    }

    fun getAreaTableName(): String {
        return "gebiete_${AppPreferenceManager.getSportType().toString().lowercase(Locale.ROOT)}"
    }

    fun getSektorenTableName(): String {
        return "sektoren_${AppPreferenceManager.getSportType().toString().toLowerCase(Locale.ROOT)}"
    }

    fun getAllAreas(): String {
        return "SELECT * FROM ${getAreaTableName()} Order By name"
    }

    fun getSectorIdBySectorNameAndAreaName(sectorName: String?, areaName: String?): String {
        return """SELECT s.* FROM ${getSektorenTableName()} s, 
            ${getAreaTableName()} a 
            where s.name='${sectorName}' AND a.name='${areaName}'""".trimMargin()
    }

    fun getAreaIdBySectorNameAndAreaName(sectorName: String?, areaName: String?): String {
        return """SELECT a.* FROM ${getSektorenTableName()} s, 
            ${getAreaTableName()} a 
            where s.name='${sectorName}' 
            AND a.name='${areaName}'""".trimMargin()
    }

    fun getSectorByAreaName(name: String): String {
        return """SELECT s.name, s.gebiet,s.id FROM ${getSektorenTableName()} s, 
            ${getAreaTableName()} a 
            where a.name Like '${name}' and s.gebiet=a.id GROUP BY s.id"""
    }

    fun getAreaByName(areaName: String?): String {
        return "SELECT * from ${getAreaTableName()} where name='${areaName}'"
    }
}