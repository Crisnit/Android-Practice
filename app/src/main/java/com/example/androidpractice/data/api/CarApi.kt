package com.example.androidpractice.data.api

import com.example.androidpractice.domain.model.Car
import retrofit2.http.GET
import retrofit2.http.Path

interface CarApi {
    @GET("cars.json")
    suspend fun getCars(): Map<String, Car>

    @GET("cars/{model}.json")
    suspend fun getCar(@Path("model") model: String): Car
}