package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.Medication

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medication WHERE id = :userId")
    fun getAllMedications(userId: Int): List<Medication>

    @Upsert
    suspend fun addMedication(medication: Medication)

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteMedication(id: Int)

}