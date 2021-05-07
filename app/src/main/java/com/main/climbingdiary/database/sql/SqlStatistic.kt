package com.main.climbingdiary.database.sql

import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.database.sql.SqlAreaSektoren.getAreaTableName
import com.main.climbingdiary.database.sql.SqlRouten.getRoutenTableName
import com.main.climbingdiary.model.Styles
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
                        | from ${getRoutenTableName()} $startPoint 
                        | join ${getAreaTableName()} g on g.id=$startPoint.gebiet 
                        | where r.level=$startPoint.level 
                        | and $startPoint.stil='${x.toUpperCase(Locale.ROOT)}') 
                        | as ${x.toUpperCase(Locale.ROOT)},""".trimMargin()
            )
        }
        sql.append(
            """"(Select count(*) as GESAMT 
                    | FROM ${getRoutenTableName()} 
                    | z join ${getAreaTableName()} g on g.id=z.gebiet 
                    | where r.level=z.level) as GESAMT
                    | FROM ${getRoutenTableName()} r
                    | where os >0 or rp >0 or flash > 0
                    | group by r.level order by r.level DESC""".trimMargin()
        )
        return sql.toString()
    }

    fun getBarChartValues(): String {
        var filterSet: String = AppPreferenceManager.getFilter()
        if (filterSet.isNotEmpty()) {
            filterSet = " where $filterSet"
        }
        return """Select r.level,sum(r.stil='RP') as rp, sum(r.stil='OS') as os,
            | sum(r.stil='FLASH') as flash 
            | from ${getRoutenTableName()} r 
            | join ${getAreaTableName()} g on g.id=r.gebiet $filterSet 
            | group by r.level""".trimMargin()
    }
}