package com.example.remedi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.data.repository.MedicationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationViewModel(
    private val repository: MedicationRepository
) : ViewModel() {

    val medications: LiveData<List<MedicationEntity>> = repository.allMedications
    val medicationCount: LiveData<Int> = repository.medicationCount

    fun addMedication(medication: MedicationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMedication(medication)
        }
    }

    fun updateMedication(medication: MedicationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedication(medication)
        }
    }

    suspend fun getMedicationById(id: Long): MedicationEntity? {
        return repository.getMedicationById(id)
    }

    suspend fun getMedicationIdByName(medicineName: String): Long? {
        return repository.getMedicationIdByName(medicineName)
    }

    fun deleteMedication(medication: MedicationEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedication(medication)
        }
    }
}