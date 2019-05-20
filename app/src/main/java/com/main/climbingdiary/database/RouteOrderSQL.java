package com.main.climbingdiary.database;

import com.main.climbingdiary.models.RouteSort;

public enum RouteOrderSQL {
    ROUTELIST;

    public String getSQL() {
        String sql_set = null;
        if (this == RouteOrderSQL.ROUTELIST) {
            switch (RouteSort.getSort()) {
                case "date":
                    sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor  group by r.id Order By r.date DESC";
                    break;
                case "area":
                    sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor  group by r.id Order By g.name ASC";
                    break;
                default:
                    sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor group by r.id Order By r.level DESC";
                    break;
            }
        }
        return sql_set;
    }
}
