package com.example.remedi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.ReminderRepository

class ReminderViewModelFactory(
    private val reminderRepository: ReminderRepository,
    private val doseHistoryRepository: DoseHistoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReminderViewModel(
                reminderRepository,
                doseHistoryRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}