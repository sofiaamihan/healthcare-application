package com.example.fullhealthcareapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fullhealthcareapplication.data.dao.ActivityDao
import com.example.fullhealthcareapplication.data.dao.CategoryDao
import com.example.fullhealthcareapplication.data.dao.MedicationDao
import com.example.fullhealthcareapplication.data.dao.OnboardingDao
import com.example.fullhealthcareapplication.data.dao.TimeDao
import com.example.fullhealthcareapplication.data.dao.UserDao
import com.example.fullhealthcareapplication.data.entity.Category
import com.example.fullhealthcareapplication.data.entity.Medication
import com.example.fullhealthcareapplication.data.entity.Activity
import com.example.fullhealthcareapplication.data.entity.Onboarding
import com.example.fullhealthcareapplication.data.entity.User
import com.example.fullhealthcareapplication.data.entity.Time


@Database(
    entities = [User::class, Activity::class, Category::class, Medication::class, Time::class, Onboarding::class],
    version = 3,
    exportSchema = false
)
abstract class HealthServiceDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val activityDao: ActivityDao
    abstract val medicationDao: MedicationDao
    abstract val categoryDao: CategoryDao
    abstract val timeDao: TimeDao
    abstract val onboardingDao: OnboardingDao


    companion object {
        @Volatile
        private var INSTANCE: HealthServiceDatabase? = null

        fun getDatabase(context: Context): HealthServiceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthServiceDatabase::class.java,
                    "health_service_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}