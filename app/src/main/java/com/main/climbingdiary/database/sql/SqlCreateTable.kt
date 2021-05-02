package com.main.climbingdiary.database.sql

object SqlCreateTable {
    fun getCreateTableTasks(): Array<String>? {
        val gebieteBouldern = """CREATE TABLE "gebiete_bouldern" (
                "id"	INTEGER PRIMARY KEY AUTOINCREMENT,
                "name"	TEXT UNIQUE,
                "lat"	REAL,
                "land"	TEXT,
                "lng"	REAL
            )"""
        val gebieteKlettern = """CREATE TABLE "gebiete_klettern" (
                "id"	INTEGER PRIMARY KEY AUTOINCREMENT,
                "name"	TEXT UNIQUE,
                "lat"	REAL,
                "land"	TEXT,
                "lng"	REAL
            )"""
        val projekteBouldern = """CREATE TABLE "projekte_bouldern" (
                "id"	INTEGER PRIMARY KEY AUTOINCREMENT,
                "name"	TEXT,
                "gebiet"	INTEGER,
                "level"	TEXT,
                "rating"	INTEGER,
                "kommentar"	TEXT,
                "sektor"	INTEGER
            )"""
        val projekteKlettern = """CREATE TABLE "projekte_klettern" (
                `id`	INTEGER PRIMARY KEY AUTOINCREMENT,
                `name`	TEXT,
                `gebiet`	INTEGER,
                `level`	TEXT,
                `rating`	INTEGER,
                `kommentar`	TEXT,
                `sektor`	INTEGER
            )"""
        val routenBouldern = """CREATE TABLE "routen_bouldern" (
                "id"	INTEGER PRIMARY KEY AUTOINCREMENT,
                "date"	DATE,
                "name"	TEXT,
                "gebiet"	INTEGER,
                "level"	TEXT,
                "stil"	TEXT,
                "rating"	INTEGER,
                "kommentar"	TEXT,
                "sektor"	INTEGER
            )"""
        val routenKlettern = """CREATE TABLE "routen_klettern" (
                    `id`	INTEGER PRIMARY KEY AUTOINCREMENT,
                    `date`	DATE,
                    `name`	TEXT,
                    `gebiet`	INTEGER,
                    `level`	TEXT,
                    `stil`	TEXT,
                    `rating`	INTEGER,
                    `kommentar`	TEXT,
                    `sektor`	INTEGER
                )"""
        val sektorenBouldern = """CREATE TABLE "sektoren_bouldern" (
                    "id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    "name"	TEXT UNIQUE,
                    "lat"	REAL,
                    "gebiet"	INTEGER,
                    "lng"	REAL
                )"""
        val sektorenKlettern = """CREATE TABLE "sektoren_klettern" (
                    "id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    "name"	TEXT UNIQUE,
                    "lat"	REAL,
                    "gebiet"	INTEGER,
                    "lng"	REAL
                )"""
        val sequence = "CREATE TABLE sqlite_sequence(name,seq)"
        val metadata = "CREATE TABLE android_metadata (locale TEXT)"
        return arrayOf(
            gebieteBouldern,
            gebieteKlettern,
            projekteBouldern,
            projekteKlettern,
            routenBouldern,
            routenKlettern,
            sektorenBouldern,
            sektorenKlettern,
            sequence,
            metadata
        )
    }
}