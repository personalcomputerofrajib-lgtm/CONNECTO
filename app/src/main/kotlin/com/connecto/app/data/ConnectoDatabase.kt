package com.connecto.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SelectionEntity::class, ReportEntity::class], version = 1, exportSchema = false)
abstract class ConnectoDatabase : RoomDatabase() {

    abstract fun selectionDao(): SelectionDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile private var INSTANCE: ConnectoDatabase? = null

        fun getInstance(context: Context): ConnectoDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ConnectoDatabase::class.java,
                    "connecto_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
