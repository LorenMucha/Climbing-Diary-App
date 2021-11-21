package com.main.climbingdiary.helper

import com.main.climbingdiary.database.TaskRepository

object TestSqliteHelper {
    fun cleanAllTables():Boolean {
        val tasks = arrayOf(
            "DELETE FROM routen_bouldern",
            "DELETE FROM routen_klettern",
            "DELETE FROM projekte_bouldern",
            "DELETE FROM projekte_klettern",
            "DELETE FROM gebiete_klettern",
            "DELETE FROM gebiete_bouldern",
            "DELETE FROM sektoren_bouldern",
            "DELETE FROM sektoren_klettern"
        )
        return TaskRepository.executeSqlTasks(tasks)
    }
}