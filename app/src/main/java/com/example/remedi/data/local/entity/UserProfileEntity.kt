package com.example.remedi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,

    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val age: String = "",
    val gender: String = "",
    val emergencyContact: String = "",

    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)

