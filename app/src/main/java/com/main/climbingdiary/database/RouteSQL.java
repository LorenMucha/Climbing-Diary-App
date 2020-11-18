package com.main.climbingdiary.database;

import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.models.Filter;
import com.main.climbingdiary.models.RouteSort;

public enum RouteSQL {
    ROUTELIST, PROJEKTLIST;

    public String getSQL() {
        String sql_set = "";
        String _filter_set = Filter.getFilter(true);
        if (!_filter_set.isEmpty()) {
            _filter_set = " where " + Filter.getFilter();
        }
        if (this == RouteSQL.ROUTELIST) {
            if ("date".equals(RouteSort.getSort())) {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r join gebiete g on g.id=r.gebiet join sektoren k on k.id=r.sektor " + _filter_set + " group by r.id Order By r.date DESC";
            } else if ("area".equals(RouteSort.getSort())) {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r join gebiete g on g.id=r.gebiet join sektoren k on k.id=r.sektor " + _filter_set + " group by r.id Order By g.name ASC";
            } else {
                sql_set = "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM routen r join gebiete g on g.id=r.gebiet join sektoren k on k.id=r.sektor " + _filter_set + " group by r.id Order By r.level DESC";
            }
        } else if (this == RouteSQL.PROJEKTLIST) {
            if (Filter.getSetter() == MenuValues.SORT_DATE) {
                _filter_set = "";
            }
            if ("area".equals(RouteSort.getSort())) {
                sql_set = "SELECT p.id, p.name,g.name as gebiet,p.level,p.rating, p.kommentar, k.name as sektor FROM projekte p join gebiete g on g.id=p.gebiet join sektoren k on k.id=p.sektor " + _filter_set + " group by p.id Order By g.name ASC";
            } else {
                sql_set = "SELECT p.id, p.name,g.name as gebiet,p.level, p.rating, p.kommentar, k.name as sektor FROM projekte p join gebiete g on g.id=p.gebiet join sektoren k on k.id=p.sektor" + _filter_set + " group by p.id Order By p.level DESC";
            }
        }
        return sql_set;
    }
}
