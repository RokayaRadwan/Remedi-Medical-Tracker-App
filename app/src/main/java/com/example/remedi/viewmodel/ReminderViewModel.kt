package com.example.remedi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedi.data.local.entity.DoseHistoryEntity
import com.example.remedi.data.local.entity.ReminderEntity
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val reminderRepository: ReminderRepository,
    private val doseHistoryRepository: DoseHistoryRepository
) : ViewModel() {

    val reminders: LiveData<List<ReminderEntity>> = reminderRepository.allReminders

    val doseHistory: LiveData<List<DoseHistoryEntity>> =
        doseHistoryRepository.allDoseHistory

    fun addReminder(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.insertReminder(reminder)
        }
    }

    fun updateReminder(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.deleteReminder(reminder)
        }
    }

    fun clearAllReminders() {
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepository.clearAllReminders()
        }
    }

    fun markDoseAsTaken(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val historyItem = DoseHistoryEntity(
                medicationId = reminder.medicationId,
                medicineName = reminder.medicineName,
                doseDetails = reminder.doseAmount,
                dateTime = reminder.time,
                status = "Taken"
            )

            doseHistoryRepository.insertDoseHistory(historyItem)
            reminderRepository.deleteReminder(reminder)
        }
    }

    fun markDoseAsMissed(reminder: ReminderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val historyItem = DoseHistoryEntity(
                medicationId = reminder.medicationId,
                medicineName = reminder.medicineName,
                doseDetails = reminder.doseAmount,
                dateTime = reminder.time,
                status = "Missed"
            )

            doseHistoryRepository.insertDoseHistory(historyItem)
            reminderRepository.deleteReminder(reminder)
        }
    }

    fun clearDoseHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            doseHistoryRepository.clearDoseHistory()
        }
    }
}