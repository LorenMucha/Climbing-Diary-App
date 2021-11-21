package com.main.climbingdiary.helper

import com.main.climbingdiary.database.TaskRepository

object TestSqliteHelper {
    fun cleanAllTables() {
        val tasks = arrayOf(
            "DELETE FROM routen_bouldern",
            "DELETE FROM routen_klettern",
            "DELETE FROM projekte_bouldern",
            "DELETE FROM projekte_klettern"
        )
        TaskRepository.executeSqlTasks(tasks)
    }
}