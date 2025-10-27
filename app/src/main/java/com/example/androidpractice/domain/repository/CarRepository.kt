package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Car

interface CarRepository {
    suspend fun getCars(): List<Car>
    suspend fun getCar(model: String): Car
}