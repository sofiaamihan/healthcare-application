package com.example.fullhealthcareapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Int = 0,
    val nric: String,
    val role: String,
    var age: Int,
    var gender: String, // Change to enum
    var weight: Double,
    var height: Double
)
