package com.main.climbingdiary.database.sql;

import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.models.Styles;

import static com.main.climbingdiary.database.sql.SqlAreaSektoren.getAreaTableName;
import static com.main.climbingdiary.database.sql.SqlRouten.getRoutenTableName;

public class SqlStatistic {

    public static String getTableValues() {
        final StringBuilder sql = new StringBuilder();
        String[] styles = Styles.getStyle(false);

        sql.append("SELECT r.level,");
        for (String x : styles) {
            char startPoint = x.charAt(1);
            sql.append(String.format("(Select count(*) as %s from %s %s join %s g on g.id=%s.gebiet where r.level=%s.level and %s.stil='%s') as %s,",
                    x.toUpperCase(), getRoutenTableName(), startPoint, getAreaTableName(), startPoint, startPoint, startPoint, x.toUpperCase(), x.toUpperCase()));
        }
        sql.append(String.format("(Select count(*) as GESAMT from %s z join %s g on g.id=z.gebiet where r.level=z.level) as GESAMT",
                getRoutenTableName(), getAreaTableName())).append(" from ")
                .append(getRoutenTableName())
                .append(" r")
                .append(" where os >0 or rp >0 or flash > 0")
                .append(" group by r.level order by r.level DESC");
        return sql.toString();
    }

    public static String getBarChartValues() {
        String filter_set = AppPreferenceManager.getFilter();
        if (!filter_set.isEmpty()) {
            filter_set = " where " + filter_set;
        }
        return String.format("select r.level,sum(r.stil='RP') as rp, sum(r.stil='OS') as os,sum(r.stil='FLASH') as flash from %s r join %s g on g.id=r.gebiet %s group by r.level",
                getRoutenTableName(), getAreaTableName(), filter_set);

    }
}
