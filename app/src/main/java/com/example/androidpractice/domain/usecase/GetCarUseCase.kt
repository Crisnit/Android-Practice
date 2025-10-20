package com.example.androidpractice.domain.usecase

import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.CarRepository

class GetCarUseCase(private val carRepository: CarRepository) {
    suspend operator fun invoke(model: String): Car {
        return carRepository.getCar(model)
    }
}