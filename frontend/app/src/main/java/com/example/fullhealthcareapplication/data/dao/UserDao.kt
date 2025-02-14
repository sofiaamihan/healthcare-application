package com.example.fullhealthcareapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fullhealthcareapplication.data.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE  id = :id")
    fun getUser(id: Int): User?

    @Upsert
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUser(id: Int)
}