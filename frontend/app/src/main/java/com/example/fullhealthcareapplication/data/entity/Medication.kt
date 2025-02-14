package com.example.fullhealthcareapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class Medication(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "time_id")
    val timeId: Int,
    val name: String,
    val type: String, // Supposed to be an Enum
    @ColumnInfo(name = "measure_amount")
    val measureAmount: Double,
    @ColumnInfo(name = "measure_unit")
    val measureUnit: String, // Supposed to be an Enum
    val frequency: String, // Supposed to be an Enum
)
