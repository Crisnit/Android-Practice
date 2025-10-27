package com.example.androidpractice.domain.usecase
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.FavoritesRepository
class GetFavoritesUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(): List<Car> = repository.getFavorites()
}