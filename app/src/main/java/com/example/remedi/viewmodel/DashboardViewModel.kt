package com.example.remedi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.remedi.data.local.entity.ReminderEntity
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.data.repository.ReminderRepository

data class DashboardSummary(
    val medicationCount: Int = 0,
    val reminderCount: Int = 0,
    val doseHistoryCount: Int = 0,
    val nextReminderText: String = "No upcoming reminders"
)

class DashboardViewModel(
    medicationRepository: MedicationRepository,
    reminderRepository: ReminderRepository,
    doseHistoryRepository: DoseHistoryRepository
) : ViewModel() {

    private val _dashboardSummary = MediatorLiveData<DashboardSummary>()
    val dashboardSummary: LiveData<DashboardSummary> = _dashboardSummary

    private var medicationCount = 0
    private var reminderCount = 0
    private var doseHistoryCount = 0
    private var reminders: List<ReminderEntity> = emptyList()

    init {
        _dashboardSummary.value = DashboardSummary()

        _dashboardSummary.addSource(medicationRepository.medicationCount) { count ->
            medicationCount = count
            updateSummary()
        }

        _dashboardSummary.addSource(reminderRepository.reminderCount) { count ->
            reminderCount = count
            updateSummary()
        }

        _dashboardSummary.addSource(doseHistoryRepository.doseHistoryCount) { count ->
            doseHistoryCount = count
            updateSummary()
        }

        _dashboardSummary.addSource(reminderRepository.allReminders) { reminderList ->
            reminders = reminderList
            updateSummary()
        }
    }

    private fun updateSummary() {
        val nextReminder = reminders.firstOrNull()

        _dashboardSummary.value = DashboardSummary(
            medicationCount = medicationCount,
            reminderCount = reminderCount,
            doseHistoryCount = doseHistoryCount,
            nextReminderText = if (nextReminder != null) {
                "${nextReminder.medicineName} at ${nextReminder.time}"
            } else {
                "No upcoming reminders"
            }
        )
    }
}