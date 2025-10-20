package com.example.androidpractice.data.repository

import com.example.androidpractice.data.api.CarApi
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.CarRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CarRepositoryImpl(
    private val carApi: CarApi,
    private val ioDispatcher: CoroutineDispatcher
) : CarRepository {

    override suspend fun getCars(): List<Car> = withContext(ioDispatcher) {
        carApi.getCars().values.toList()
    }

    override suspend fun getCar(model: String): Car = withContext(ioDispatcher) {
        carApi.getCar(model)
    }
}