package com.example.androidpractice.domain.repository
import com.example.androidpractice.domain.model.Car
interface FavoritesRepository {
    suspend fun getFavorites(): List<Car>
    suspend fun addFavorite(car: Car)
    suspend fun removeFavorite(car: Car)
}