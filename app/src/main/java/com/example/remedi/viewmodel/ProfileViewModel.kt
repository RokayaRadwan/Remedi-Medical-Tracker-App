package com.example.remedi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedi.data.local.entity.UserProfileEntity
import com.example.remedi.data.repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserProfileRepository
) : ViewModel() {

    val userProfile: LiveData<UserProfileEntity?> = repository.userProfile

    fun saveProfile(
        fullName: String,
        email: String,
        phoneNumber: String,
        age: String,
        gender: String,
        emergencyContact: String,
        notificationsEnabled: Boolean,
        darkModeEnabled: Boolean
    ) {
        val profile = UserProfileEntity(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            age = age,
            gender = gender,
            emergencyContact = emergencyContact,
            notificationsEnabled = notificationsEnabled,
            darkModeEnabled = darkModeEnabled
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserProfile(profile)
        }
    }

    fun clearProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProfile()
        }
    }
}