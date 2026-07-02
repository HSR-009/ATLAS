package com.atlas.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EventEntity::class,
        DepartmentEntity::class,
        VolunteerEntity::class,
        TaskEntity::class,
        LogEntryEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AtlasDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun taskDao(): TaskDao
    abstract fun logEntryDao(): LogEntryDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AtlasDatabase? = null

        fun getInstance(context: Context): AtlasDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AtlasDatabase::class.java,
                    "atlas_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
