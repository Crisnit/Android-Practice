package com.example.androidpractice.di

import com.example.androidpractice.data.api.CarApi
import com.example.androidpractice.data.repository.CarRepositoryImpl
import com.example.androidpractice.domain.repository.CarRepository
import com.example.androidpractice.domain.usecase.GetCarUseCase
import com.example.androidpractice.domain.usecase.GetCarsUseCase
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://cars-api-a7e69-default-rtdb.europe-west1.firebasedatabase.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val carApi: CarApi = retrofit.create(CarApi::class.java)

    private val carRepository: CarRepository = CarRepositoryImpl(
        carApi = carApi,
        ioDispatcher = Dispatchers.IO
    )

    val getCarsUseCase: GetCarsUseCase = GetCarsUseCase(carRepository)

    val getCarUseCase: GetCarUseCase = GetCarUseCase(carRepository)
}