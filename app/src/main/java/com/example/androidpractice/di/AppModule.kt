package com.example.androidpractice.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.androidpractice.data.api.CarApi
import com.example.androidpractice.data.database.AppDatabase
import com.example.androidpractice.data.repository.CarRepositoryImpl
import com.example.androidpractice.data.repository.FavoritesRepositoryImpl
import com.example.androidpractice.domain.repository.CarRepository
import com.example.androidpractice.domain.repository.FavoritesRepository
import com.example.androidpractice.domain.usecase.AddFavoriteUseCase
import com.example.androidpractice.domain.usecase.GetCarUseCase
import com.example.androidpractice.domain.usecase.GetCarsUseCase
import com.example.androidpractice.domain.usecase.GetFavoritesUseCase
import com.example.androidpractice.domain.usecase.RemoveFavoriteUseCase
import com.example.androidpractice.presentation.FilterCache
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filters")

    private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_preferences")
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://cars-api-a7e69-default-rtdb.europe-west1.firebasedatabase.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val carApi: CarApi = retrofit.create(CarApi::class.java)

    fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore

    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .build()

    fun provideFavoritesRepository(db: AppDatabase): FavoritesRepository =
        FavoritesRepositoryImpl(db.carDao())

    val carRepository: CarRepository = CarRepositoryImpl(
        carApi = carApi,
        ioDispatcher = Dispatchers.IO
    )

    val getCarsUseCase: GetCarsUseCase = GetCarsUseCase(carRepository)
    val getCarUseCase: GetCarUseCase = GetCarUseCase(carRepository)

    fun provideAddFavoriteUseCase(repo: FavoritesRepository): AddFavoriteUseCase = AddFavoriteUseCase(repo)
    fun provideRemoveFavoriteUseCase(repo: FavoritesRepository): RemoveFavoriteUseCase = RemoveFavoriteUseCase(repo)
    fun provideGetFavoritesUseCase(repo: FavoritesRepository): GetFavoritesUseCase = GetFavoritesUseCase(repo)

    fun provideProfileDataStore(context: Context): DataStore<Preferences> = context.profileDataStore

    val filterCache: FilterCache = FilterCache()
}