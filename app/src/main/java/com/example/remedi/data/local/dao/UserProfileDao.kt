package com.example.remedi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.remedi.data.local.entity.UserProfileEntity

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): LiveData<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()
}
