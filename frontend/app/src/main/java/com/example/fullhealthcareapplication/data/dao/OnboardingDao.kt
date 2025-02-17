package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.Onboarding
import com.example.fullhealthcareapplication.data.entity.User

@Dao
interface OnboardingDao {

    @Query("SELECT onboarding FROM onboarding WHERE userId = :userId")
    suspend fun getOnboardingStatus(userId: Int): Boolean

    @Query("UPDATE onboarding SET onboarding = :onboarding WHERE userId = :userId")
    suspend fun updateOnboardingStatus(userId: Int, onboarding: Boolean)

}