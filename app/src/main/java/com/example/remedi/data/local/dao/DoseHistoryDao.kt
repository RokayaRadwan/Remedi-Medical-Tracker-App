package com.example.remedi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.remedi.data.local.entity.DoseHistoryEntity

@Dao
interface DoseHistoryDao {

    @Query("SELECT * FROM dose_history ORDER BY id DESC")
    fun getAllDoseHistory(): LiveData<List<DoseHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoseHistory(history: DoseHistoryEntity): Long

    @Delete
    suspend fun deleteDoseHistory(history: DoseHistoryEntity)

    @Query("DELETE FROM dose_history")
    suspend fun clearDoseHistory()

    @Query("SELECT COUNT(*) FROM dose_history")
    fun getDoseHistoryCount(): LiveData<Int>
}


