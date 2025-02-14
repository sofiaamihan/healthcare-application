package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAllCategories(): List<Category>

    @Upsert
    suspend fun insertCategory(category: Category)

}