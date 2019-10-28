package com.main.climbingdiary.database;

import com.main.climbingdiary.models.Filter;
import com.main.climbingdiary.models.RouteSort;

public enum RouteLists {
    ROUTELIST, PROJEKTLIST;

    public String getSQL() {
        String sql_set = "";
        String _filter_set = "";
        if(Filter.getFilter()!=null){
            _filter_set = " and "+Filter.getFilter();
        }
        if (this == RouteLists.ROUTELIST) {
            if ("date".equals(RouteSort.getSort())) {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor " + _filter_set + " group by r.id Order By r.date DESC";
            } else if ("area".equals(RouteSort.getSort())) {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor " + _filter_set + " group by r.id Order By g.name ASC";
            } else {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor " + _filter_set + " group by r.id Order By r.level DESC";
            }
        }
        else if (this== RouteLists.PROJEKTLIST){
            if ("area".equals(RouteSort.getSort())) {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM projekte r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor  group by r.id Order By g.name ASC";
            } else {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level, r.rating, r.kommentar, k.name as sektor FROM projekte r, gebiete g, sektoren k where g.id=r.gebiet and k.id=r.sektor group by r.id Order By r.level DESC";
            }
        }
        return sql_set;
    }
}
