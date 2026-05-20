package com.example.remedi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remedi.data.repository.MedicationRepository

class MedicationViewModelFactory(
    private val repository: MedicationRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}