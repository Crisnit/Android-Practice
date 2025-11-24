package com.example.androidpractice.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidpractice.domain.model.Car

@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}