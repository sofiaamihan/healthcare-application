package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.Activity

@Dao
interface ActivityDao {
    @Query ("SELECT * FROM activity WHERE user_id= :userId AND DATE(time_taken)= :date")
    fun getAllActivities(userId: Int, date: String): List<Activity>

    @Upsert
    suspend fun addActivity(activity: Activity)

    @Query("DELETE FROM activity WHERE id = :id")
    suspend fun deleteActivity(id: Int)
}