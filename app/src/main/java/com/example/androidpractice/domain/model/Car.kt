package com.example.androidpractice.domain.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Car")
data class Car (
    @PrimaryKey val model: String,
    val manufacturer: String,
    val productionYears: String,
    val carClass: String,
    val powertrainLayout: String,
    val description: String,
    val imagePath: String
)