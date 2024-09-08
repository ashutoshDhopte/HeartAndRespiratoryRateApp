package com.example.diashieldcse535.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Monitor::class], version = 1)
abstract class Databases: RoomDatabase() {

    abstract fun monitorDao(): MonitorDao
}