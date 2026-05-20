package com.example.remedi.data.repository

import androidx.lifecycle.LiveData
import com.example.remedi.data.local.dao.ReminderDao
import com.example.remedi.data.local.entity.ReminderEntity

class ReminderRepository(
    private val reminderDao: ReminderDao
) {

    val allReminders: LiveData<List<ReminderEntity>> =
        reminderDao.getAllReminders()

    val reminderCount: LiveData<Int> =
        reminderDao.getReminderCount()

    fun getRemindersForMedication(medicationId: Long): LiveData<List<ReminderEntity>> {
        return reminderDao.getRemindersForMedication(medicationId)
    }

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun clearAllReminders() {
        reminderDao.clearAllReminders()
    }
}