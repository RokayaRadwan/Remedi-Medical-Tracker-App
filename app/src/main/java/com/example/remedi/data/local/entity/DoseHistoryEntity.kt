package com.example.remedi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dose_history",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId")]
)
data class DoseHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val medicationId: Long,

    val medicineName: String,
    val doseDetails: String,
    val dateTime: String,
    val status: String
)