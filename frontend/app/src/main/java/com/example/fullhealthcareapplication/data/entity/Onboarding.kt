package com.example.fullhealthcareapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onboarding")
data class Onboarding(
    @PrimaryKey val id:Int,
    val userId: Int,
    val onboarding: Boolean = false
)

