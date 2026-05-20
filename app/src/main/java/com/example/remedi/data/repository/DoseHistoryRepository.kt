package com.example.remedi.data.repository

import androidx.lifecycle.LiveData
import com.example.remedi.data.local.dao.DoseHistoryDao
import com.example.remedi.data.local.entity.DoseHistoryEntity

class DoseHistoryRepository(
    private val doseHistoryDao: DoseHistoryDao
) {

    val allDoseHistory: LiveData<List<DoseHistoryEntity>> =
        doseHistoryDao.getAllDoseHistory()

    val doseHistoryCount: LiveData<Int> =
        doseHistoryDao.getDoseHistoryCount()

    suspend fun insertDoseHistory(history: DoseHistoryEntity): Long {
        return doseHistoryDao.insertDoseHistory(history)
    }

    suspend fun deleteDoseHistory(history: DoseHistoryEntity) {
        doseHistoryDao.deleteDoseHistory(history)
    }

    suspend fun clearDoseHistory() {
        doseHistoryDao.clearDoseHistory()
    }
}