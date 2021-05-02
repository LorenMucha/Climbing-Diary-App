package com.main.climbingdiary.database

import com.main.climbingdiary.common.AppPreferenceManager

object SqlRouten {

    val type = AppPreferenceManager.getSportType().toString().toLowerCase()
    val projectTableNAme: String = "projekte_${type}"
    val routenTableName: String = "routen_${type}"

    fun getRouteList(routeType: RouteType): String? {
        val builder = StringBuilder()
        var filterSet = AppPreferenceManager.getFilter()
        filterSet = if (filterSet!!.isEmpty()) "" else String.format(" where %s", filterSet)
        val sort: RouteSort = AppPreferenceManager.getSort()
        if (routeType === RouteType.ROUTE) {
            if (sort === RouteSort.DATE) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(SqlRouten.getRoutenTableName())
                    .append(" r")
                    .append(
                        java.lang.String.format(
                            " join %s g on g.id=r.gebiet join %s k on k.id=r.sektor %s group by r.id Order By r.date DESC",
                            getAreaTableName(),
                            getSektorenTableName(),
                            filterSet
                        )
                    )
            } else if (sort === RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(SqlRouten.getRoutenTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By g.name ASC")
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                    .append(SqlRouten.getRoutenTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By r.level DESC")
            }
        } else if (routeType === RouteType.PROJEKT) {
            if (sort === RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM ")
                    .append(SqlRouten.getProjekteTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor ")
                    .append(filterSet)
                    .append(" group by r.id Order By g.name ASC")
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level, r.rating, r.kommentar, k.name as sektor FROM ")
                    .append(SqlRouten.getProjekteTableName())
                    .append(" r")
                    .append(" join ")
                    .append(getAreaTableName())
                    .append(" g on g.id=r.gebiet join ")
                    .append(getSektorenTableName())
                    .append(" k on k.id=r.sektor")
                    .append(filterSet)
                    .append(" group by r.id Order By r.level DESC")
            }
        }
        return builder.toString()
    }

    fun getRoute(id: Int): String? {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM " +
                SqlRouten.getRoutenTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id
    }

    fun getProjekt(id: Int): String? {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM " +
                SqlRouten.getProjekteTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id
    }

    fun getTopTenRoutes(year: Int): String? {
        return "Select r.level, r.stil, r.name, r.gebiet FROM " +
                SqlRouten.getRoutenTableName() +
                " r" +
                " where CAST(strftime('%Y',r.date) as int)==" +
                year +
                " Order By r.level desc, r.stil asc Limit 10"
    }

    fun getYears(filterSet: Boolean): String? {
        val filter = AppPreferenceManager.getFilter()
        var sql =
            "select DISTINCT(strftime('%Y',r.date)) as year from " + SqlRouten.getRoutenTableName() + " r order by r.date DESC"
        if (filterSet && !filter!!.isEmpty()) {
            sql =
                "select DISTINCT(strftime('%Y',r.date)) as year from " + SqlRouten.getRoutenTableName() + " r " + filter + " order by r.date DESC"
        }
        return sql
    }

    fun getInsertRouteTasks(route: Route): Array<String>? {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName().toString() + " (name) VALUES ('" + route.getArea()
                .toString() + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName().toString() + " (name,gebiet) " +
                    "SELECT '" + route.getSector()
                .toString() + "',id FROM " + getAreaTableName().toString() + " WHERE name='" + route.getArea()
                .toString() + "'"
        val insertRoute =
            "INSERT OR IGNORE INTO " + SqlRouten.getRoutenTableName() + " (date,name,level,stil,rating,kommentar,gebiet,sektor) " +
                    "SELECT '" + route.getDate() + "','" + route.getName() + "','" + route.getLevel() + "','" + route.getStyle() + "','" + route.getRating() + "','" + route.getComment() + "',a.id,s.id" +
                    " FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                    "WHERE a.name = '" + route.getArea() + "'" +
                    "AND s.name='" + route.getSector() + "'"
        return arrayOf(
            insertArea, insertSektor, insertRoute
        )
    }

    fun getInsertProjektTasks(projekt: Projekt): Array<String>? {
        val insertArea =
            "INSERT OR IGNORE INTO " + getAreaTableName().toString() + " (name) VALUES ('" + projekt.getArea()
                .toString() + "')"
        val insertSektor =
            "INSERT OR IGNORE INTO " + getSektorenTableName().toString() + " (name,gebiet) " +
                    "SELECT '" + projekt.getSector()
                .toString() + "',id FROM " + getAreaTableName().toString() + " WHERE name='" + projekt.getArea()
                .toString() + "'"
        val insertProjekt =
            "INSERT OR IGNORE INTO " + SqlRouten.getProjekteTableName() + " (name,level,rating,kommentar,gebiet,sektor) " +
                    "SELECT '" + projekt.getName() + "','" + projekt.getLevel() + "','" + projekt.getRating() + "','" + projekt.getComment() + "',a.id,s.id " +
                    "FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                    "WHERE a.name = '" + projekt.getArea() + "'" +
                    " AND s.name='" + projekt.getSector() + "'"
        return arrayOf(insertArea, insertSektor, insertProjekt)
    }

    fun deleteRoute(id: Int): String? {
        return "DELETE FROM " + SqlRouten.getRoutenTableName() + " WHERE id=" + id
    }

    fun deleteProjekt(id: Int): String? {
        return "DELETE FROM " + SqlRouten.getProjekteTableName() + " WHERE id=" + id
    }
}