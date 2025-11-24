package com.example.androidpractice.data.repository
import com.example.androidpractice.data.database.CarDao
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.repository.FavoritesRepository
class FavoritesRepositoryImpl(
    private val carDao: CarDao
) : FavoritesRepository {
    override suspend fun getFavorites(): List<Car> = carDao.getAll()
    override suspend fun addFavorite(car: Car) {
        carDao.insert(car)
    }
    override suspend fun removeFavorite(car: Car) {
        carDao.delete(car)
    }
}