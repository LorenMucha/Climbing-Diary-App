package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.preferences.AppPreferenceManager
import java.util.*

object SqlAreaSektoren {

    fun getAreaTableName(): String {
        return "gebiete_${AppPreferenceManager.getSportType().toString().toLowerCase(Locale.ROOT)}"
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
}