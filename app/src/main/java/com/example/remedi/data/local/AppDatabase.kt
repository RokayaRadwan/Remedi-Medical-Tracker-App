package com.example.remedi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.remedi.data.local.dao.DoseHistoryDao
import com.example.remedi.data.local.dao.MedicationDao
import com.example.remedi.data.local.dao.ReminderDao
import com.example.remedi.data.local.dao.UserDao
import com.example.remedi.data.local.dao.UserProfileDao
import com.example.remedi.data.local.entity.DoseHistoryEntity
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.data.local.entity.ReminderEntity
import com.example.remedi.data.local.entity.UserEntity
import com.example.remedi.data.local.entity.UserProfileEntity

@Database(
    entities = [
        MedicationEntity::class,
        ReminderEntity::class,
        DoseHistoryEntity::class,
        UserProfileEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao
    abstract fun reminderDao(): ReminderDao
    abstract fun doseHistoryDao(): DoseHistoryDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "remedi_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}