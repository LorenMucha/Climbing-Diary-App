package com.main.climbingdiary.database.sql;

public class SqlCreateTable {

    public static String[] getCreateTableTasks() {
        final String gebiete_bouldern = "CREATE TABLE \"gebiete_bouldern\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\t\"lat\"\tREAL,\n" +
                "\t\"land\"\tTEXT,\n" +
                "\t\"lng\"\tREAL\n" +
                ")";
        final String gebiete_klettern = "CREATE TABLE \"gebiete_klettern\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\t\"lat\"\tREAL,\n" +
                "\t\"land\"\tTEXT,\n" +
                "\t\"lng\"\tREAL\n" +
                ")";
        final String projekte_bouldern = "CREATE TABLE \"projekte_bouldern\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"gebiet\"\tINTEGER,\n" +
                "\t\"level\"\tTEXT,\n" +
                "\t\"rating\"\tINTEGER,\n" +
                "\t\"kommentar\"\tTEXT,\n" +
                "\t\"sektor\"\tINTEGER\n" +
                ")";

        final String projekte_klettern = "CREATE TABLE \"projekte_klettern\" (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`name`\tTEXT,\n" +
                "\t`gebiet`\tINTEGER,\n" +
                "\t`level`\tTEXT,\n" +
                "\t`rating`\tINTEGER,\n" +
                "\t`kommentar`\tTEXT,\n" +
                "\t`sektor`\tINTEGER\n" +
                ")";

        final String routen_bouldern = "CREATE TABLE \"routen_bouldern\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"date\"\tDATE,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"gebiet\"\tINTEGER,\n" +
                "\t\"level\"\tTEXT,\n" +
                "\t\"stil\"\tTEXT,\n" +
                "\t\"rating\"\tINTEGER,\n" +
                "\t\"kommentar\"\tTEXT,\n" +
                "\t\"sektor\"\tINTEGER\n" +
                ")";

        final String routen_klettern = "CREATE TABLE \"routen_klettern\" (\n" +
                "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`date`\tDATE,\n" +
                "\t`name`\tTEXT,\n" +
                "\t`gebiet`\tINTEGER,\n" +
                "\t`level`\tTEXT,\n" +
                "\t`stil`\tTEXT,\n" +
                "\t`rating`\tINTEGER,\n" +
                "\t`kommentar`\tTEXT,\n" +
                "\t`sektor`\tINTEGER\n" +
                ")";

        final String sektoren_bouldern = "CREATE TABLE \"sektoren_bouldern\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\t\"lat\"\tREAL,\n" +
                "\t\"gebiet\"\tINTEGER,\n" +
                "\t\"lng\"\tREAL\n" +
                ")";

        final String sektoren_klettern = "CREATE TABLE \"sektoren_klettern\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\t\"lat\"\tREAL,\n" +
                "\t\"gebiet\"\tINTEGER,\n" +
                "\t\"lng\"\tREAL\n" +
                ")";

        final String sequence = "CREATE TABLE sqlite_sequence(name,seq)";
        final String metadata = "CREATE TABLE android_metadata (locale TEXT)";

        return new String[]{gebiete_bouldern, gebiete_klettern, projekte_bouldern, projekte_klettern,
                routen_bouldern, routen_klettern, sektoren_bouldern, sektoren_klettern, sequence, metadata};

    }
}
