package com.example.androidpractice.domain.usecase

import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.CarRepository

class GetCarsUseCase(private val carRepository: CarRepository) {
    suspend operator fun invoke(): List<Car> {
        return carRepository.getCars()
    }
}