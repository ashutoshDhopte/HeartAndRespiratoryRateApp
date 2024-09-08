package com.example.diashieldcse535.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class MonitorDao {

    @Query("SELECT * FROM monitor order by monitorID desc limit 1")
    abstract fun findLast(): Monitor

    @Insert
    abstract fun insertMonitor(monitor: Monitor)

    fun insertMonitorWithTimestamp(monitor: Monitor){
        insertMonitor(monitor.apply {
            createdOn = System.currentTimeMillis()
        })
    }
}