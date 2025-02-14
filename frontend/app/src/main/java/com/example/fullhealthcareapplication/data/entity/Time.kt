package com.example.fullhealthcareapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time")
data class Time(
    @PrimaryKey val id: Int,
    val time: String, // Supposed to be DateTime
)
