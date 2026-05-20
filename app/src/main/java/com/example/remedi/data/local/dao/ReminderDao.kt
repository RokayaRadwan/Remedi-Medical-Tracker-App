package com.example.remedi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.remedi.data.local.entity.ReminderEntity

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY time ASC")
    fun getAllReminders(): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE medicationId = :medicationId ORDER BY time ASC")
    fun getRemindersForMedication(medicationId: Long): LiveData<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders")
    suspend fun clearAllReminders()

    @Query("SELECT COUNT(*) FROM reminders")
    fun getReminderCount(): LiveData<Int>
}