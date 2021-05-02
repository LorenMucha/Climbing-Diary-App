package com.main.climbingdiary.database.sql;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.SectorRepository;
import com.main.climbingdiary.models.RouteSort;
import com.main.climbingdiary.models.RouteType;

import java.util.regex.Pattern;

import static com.main.climbingdiary.database.sql.SqlAreaSektoren.getAreaTableName;
import static com.main.climbingdiary.database.sql.SqlAreaSektoren.getSektorenTableName;


public class SqlRouten {

    public static String getProjekteTableName() {
        return "projekte_" + AppPreferenceManager.getSportType().typeToString();
    }

    public static String getRoutenTableName() {
        return "routen_" + AppPreferenceManager.getSportType().typeToString();
    }

    public static String getRouteList(RouteType routeType) {
        final StringBuilder builder = new StringBuilder();
        String filterSet = AppPreferenceManager.getFilter();
        filterSet = filterSet.isEmpty() ? "" : String.format(" where %s", filterSet);
        RouteSort sort = AppPreferenceManager.getSort();
        if (routeType == RouteType.ROUTE) {
            if (sort == RouteSort.DATE) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                        .append(getRoutenTableName())
                        .append(" r")
                        .append(String.format(" join %s g on g.id=r.gebiet join %s k on k.id=r.sektor %s group by r.id Order By r.date DESC", getAreaTableName(), getSektorenTableName(), filterSet));
            } else if (sort == RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                        .append(getRoutenTableName())
                        .append(" r")
                        .append(" join ")
                        .append(getAreaTableName())
                        .append(" g on g.id=r.gebiet join ")
                        .append(getSektorenTableName())
                        .append(" k on k.id=r.sektor ")
                        .append(filterSet)
                        .append(" group by r.id Order By g.name ASC");
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM ")
                        .append(getRoutenTableName())
                        .append(" r")
                        .append(" join ")
                        .append(getAreaTableName())
                        .append(" g on g.id=r.gebiet join ")
                        .append(getSektorenTableName())
                        .append(" k on k.id=r.sektor ")
                        .append(filterSet)
                        .append(" group by r.id Order By r.level DESC");
            }
        } else if (routeType == RouteType.PROJEKT) {
            if (sort == RouteSort.AREA) {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM ")
                        .append(getProjekteTableName())
                        .append(" r")
                        .append(" join ")
                        .append(getAreaTableName())
                        .append(" g on g.id=r.gebiet join ")
                        .append(getSektorenTableName())
                        .append(" k on k.id=r.sektor ")
                        .append(filterSet)
                        .append(" group by r.id Order By g.name ASC");
            } else {
                builder.append("SELECT r.id, r.name,g.name as gebiet,r.level, r.rating, r.kommentar, k.name as sektor FROM ")
                        .append(getProjekteTableName())
                        .append(" r")
                        .append(" join ")
                        .append(getAreaTableName())
                        .append(" g on g.id=r.gebiet join ")
                        .append(getSektorenTableName())
                        .append(" k on k.id=r.sektor")
                        .append(filterSet)
                        .append(" group by r.id Order By r.level DESC");
            }
        }
        return builder.toString();
    }

    public static String getRoute(int id) {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.stil,r.rating, r.kommentar, strftime('%d.%m.%Y',r.date) as date, k.name as sektor FROM " +
                getRoutenTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id;
    }

    public static String getProjekt(int id) {
        return "SELECT r.id, r.name,g.name as gebiet,r.level,r.rating, r.kommentar, k.name as sektor FROM " +
                getProjekteTableName() +
                " r, " +
                getAreaTableName() +
                " g, " +
                getSektorenTableName() +
                " k" +
                " where g.id=r.gebiet and k.id=r.sektor AND r.id=" +
                id;
    }

    public static String getTopTenRoutes(int year) {
        return "Select r.level, r.stil, r.name, r.gebiet FROM " +
                getRoutenTableName() +
                " r" +
                " where CAST(strftime('%Y',r.date) as int)==" +
                year +
                " Order By r.level desc, r.stil asc Limit 10";
    }

    public static String getYears(boolean filterSet) {
        final String filter = AppPreferenceManager.getFilter();
        String sql = "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r order by r.date DESC";
        if (filterSet && !filter.isEmpty()) {
            sql = "select DISTINCT(strftime('%Y',r.date)) as year from " + getRoutenTableName() + " r " + filter + " order by r.date DESC";
        }
        return sql;
    }

    public static String[] getInsertRouteTasks(Route route) {
        final String insertArea = "INSERT OR IGNORE INTO " + getAreaTableName() + " (name) VALUES ('" + route.getArea() + "')";
        final String insertSektor = "INSERT OR IGNORE INTO " + getSektorenTableName() + " (name,gebiet) " +
                "SELECT '" + route.getSector() + "',id FROM " + getAreaTableName() + " WHERE name='" + route.getArea() + "'";
        final String insertRoute = "INSERT OR IGNORE INTO " + getRoutenTableName() + " (date,name,level,stil,rating,kommentar,gebiet,sektor) " +
                "SELECT '" + route.getDate() + "','" + route.getName() + "','" + route.getLevel() + "','" + route.getStyle() + "','" + route.getRating() + "','" + route.getComment() + "',a.id,s.id" +
                " FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                "WHERE a.name = '" + route.getArea() + "'" +
                "AND s.name='" + route.getSector() + "'";

        return new String[]{
                insertArea, insertSektor, insertRoute

        };
    }

    public static String[] getInsertProjektTasks(Projekt projekt) {
        final String insertArea = "INSERT OR IGNORE INTO " + getAreaTableName() + " (name) VALUES ('" + projekt.getArea() + "')";
        final String insertSektor = "INSERT OR IGNORE INTO " + getSektorenTableName() + " (name,gebiet) " +
                "SELECT '" + projekt.getSector() + "',id FROM " + getAreaTableName() + " WHERE name='" + projekt.getArea() + "'";
        final String insertProjekt = "INSERT OR IGNORE INTO " + getProjekteTableName() + " (name,level,rating,kommentar,gebiet,sektor) " +
                "SELECT '" + projekt.getName() + "','" + projekt.getLevel() + "','" + projekt.getRating() + "','" + projekt.getComment() + "',a.id,s.id " +
                "FROM " + getAreaTableName() + " a, " + getSektorenTableName() + " s " +
                "WHERE a.name = '" + projekt.getArea() + "'" +
                " AND s.name='" + projekt.getSector() + "'";
        return new String[]{insertArea, insertSektor, insertProjekt};
    }

    public static String updateRoute(Route route) {
        String areaName = route.getArea();
        String sectorName = route.getSector();
        if (checkIfNumeric(areaName) && checkIfNumeric(sectorName)) {
            route.setArea(Integer.toString(AreaRepository.getInstance().getAreaByAreaNameAndSectorName(sectorName, areaName).getId()));
            route.setSector(Integer.toString(SectorRepository.getInstance().getSectorByAreaNameAndSectorName(sectorName, areaName).getId()));
        }
        return String.format("UPDATE %s SET date='%s', name='%s', level='%s', stil='%s', rating='%s', kommentar='%s',  gebiet='%s', sektor='%s' where id=%s",
                getRoutenTableName(), route.getDate(), route.getName(), route.getLevel(), route.getStyle(), route.getRating(),
                route.getComment(), route.getArea(),route.getSector(), route.getId());
    }

    public static String deleteRoute(int id) {
        return "DELETE FROM " + getRoutenTableName() + " WHERE id=" + id;
    }

    public static String deleteProjekt(int id) {
        return "DELETE FROM " + getProjekteTableName() + " WHERE id=" + id;
    }

    private static boolean checkIfNumeric(String toCheck) {
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (toCheck == null) {
            return true;
        }
        return !pattern.matcher(toCheck).matches();
    }
}