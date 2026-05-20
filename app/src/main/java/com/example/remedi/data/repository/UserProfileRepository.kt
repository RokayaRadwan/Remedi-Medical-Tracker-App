package com.example.remedi.data.repository

import androidx.lifecycle.LiveData
import com.example.remedi.data.local.dao.UserProfileDao
import com.example.remedi.data.local.entity.UserProfileEntity

class UserProfileRepository(
    private val userProfileDao: UserProfileDao
) {

    val userProfile: LiveData<UserProfileEntity?> =
        userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun clearProfile() {
        userProfileDao.clearProfile()
    }
}