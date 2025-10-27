package com.example.androidpractice.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidpractice.domain.model.Car
@Dao
interface CarDao {
    @Query("SELECT * FROM Car")
    suspend fun getAll(): List<Car>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car)
    @Delete
    suspend fun delete(car: Car)
}