package com.example.androidpractice.domain.usecase
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.FavoritesRepository
class RemoveFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(car: Car) = repository.removeFavorite(car)
}