package com.example.remedi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.remedi.data.local.entity.MedicationEntity

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY id DESC")
    fun getMedicationsForUser(userId: Long): LiveData<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): MedicationEntity?

    @Query("SELECT id FROM medications WHERE name = :medicineName AND userId = :userId LIMIT 1")
    suspend fun getMedicationIdByName(medicineName: String, userId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    @Query("SELECT COUNT(*) FROM medications WHERE userId = :userId")
    fun getMedicationCountForUser(userId: Long): LiveData<Int>
}