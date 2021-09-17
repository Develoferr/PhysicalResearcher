package com.tutorial.earthquakemonitor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tutorial.earthquakemonitor.Earthquake
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Earthquake::class], version = 1)
abstract class EqDatabase: RoomDatabase() {
    abstract val eqDao: EqDao
}

private lateinit var INSTANCE: EqDatabase

fun getDatabase(context: Context): EqDatabase {
    kotlin.synchronized(EqDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                EqDatabase::class.java,
                "earthquake_db"
            ).build()
        }
        return INSTANCE
    }
}