package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.Time

@Dao
interface TimeDao {

    @Query("SELECT * FROM time")
    fun getAllTimes(): List<Time>

    @Upsert
    suspend fun insertTime(time: Time)

}