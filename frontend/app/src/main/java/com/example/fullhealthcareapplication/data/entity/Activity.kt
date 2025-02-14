package com.example.fullhealthcareapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "time_taken")
    val timeTaken: String, // Supposed to be DateTime
    @ColumnInfo(name = "calories_burnt")
    val caloriesBurnt: Double,
    @ColumnInfo(name = "step_count")
    val stepCount: Double,
    @ColumnInfo(name = "distance")
    val distance: Double,
    @ColumnInfo(name = "walking_speed")
    val walkingSpeed: Double,
    @ColumnInfo(name = "walking_steadiness")
    val walkingSteadiness: Double,
)
