package com.example.remedi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medications",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: Long,

    val name: String,
    val dosage: String,
    val time: String,
    val instruction: String,
    val refillsLeft: Int,
    val color: Int,
    val daysOfWeek: String,

    val createdAt: Long = System.currentTimeMillis()
)