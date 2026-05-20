package com.example.remedi.data.repository

import androidx.lifecycle.LiveData
import com.example.remedi.data.local.dao.MedicationDao
import com.example.remedi.data.local.entity.MedicationEntity

class MedicationRepository(
    private val medicationDao: MedicationDao,
    private val userId: Long
) {

    val allMedications: LiveData<List<MedicationEntity>> =
        medicationDao.getMedicationsForUser(userId)

    val medicationCount: LiveData<Int> =
        medicationDao.getMedicationCountForUser(userId)

    suspend fun getMedicationById(id: Long): MedicationEntity? {
        return medicationDao.getMedicationById(id)
    }

    suspend fun insertMedication(medication: MedicationEntity): Long {
        return medicationDao.insertMedication(medication)
    }

    suspend fun updateMedication(medication: MedicationEntity) {
        medicationDao.updateMedication(medication)
    }

    suspend fun getMedicationIdByName(medicineName: String): Long? {
        return medicationDao.getMedicationIdByName(medicineName, userId)
    }

    suspend fun deleteMedication(medication: MedicationEntity) {
        medicationDao.deleteMedication(medication)
    }
}