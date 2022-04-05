package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.models.Styles
import java.util.*

object SqlStatistic {

    fun getTableValues(): String {
        val sql: StringBuilder = StringBuilder()
        val styles: Array<String> = Styles.getStyle(false)

        sql.append("SELECT r.level,")
        for (x in styles) {
            val startPoint = x[1]
            sql.append(
                """(Select count(*) as ${x.toUpperCase(Locale.ROOT)} 
                        | from ${SqlRouten.getRoutenTableName()} $startPoint 
                        | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=$startPoint.gebiet 
                        | where r.level=$startPoint.level 
                        | and $startPoint.stil='${x.toUpperCase(Locale.ROOT)}') 
                        | as ${x.toUpperCase(Locale.ROOT)},""".trimMargin()
            )
        }
        sql.append(
            """(Select count(*) as GESAMT 
                    | FROM ${SqlRouten.getRoutenTableName()} 
                    | z join ${SqlAreaSektoren.getAreaTableName()} g on g.id=z.gebiet 
                    | where r.level=z.level) as GESAMT
                    | FROM ${SqlRouten.getRoutenTableName()} r
                    | where os >0 or rp >0 or flash > 0
                    | group by r.level order by r.level DESC""".trimMargin()
        )
        return sql.toString()
    }

    fun getBarChartValues(): String {
        var filterSet: String = AppPreferenceManager.getFilter()!!
        if (filterSet.isNotEmpty()) {
            filterSet = " where $filterSet"
        }
        return """Select r.level,sum(r.stil='RP') as rp, sum(r.stil='OS') as os,
            | sum(r.stil='FLASH') as flash 
            | from ${SqlRouten.getRoutenTableName()} r 
            | join ${SqlAreaSektoren.getAreaTableName()} g on g.id=r.gebiet $filterSet 
            | group by r.level""".trimMargin()
    }
}