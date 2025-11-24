package com.example.androidpractice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.usecase.AddFavoriteUseCase
import com.example.androidpractice.domain.usecase.GetCarUseCase
import com.example.androidpractice.domain.usecase.GetFavoritesUseCase
import com.example.androidpractice.domain.usecase.RemoveFavoriteUseCase
import com.example.androidpractice.presentation.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarDetailsViewModel(
    private val getCarUseCase: GetCarUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Car>>(UiState.Loading)
    val uiState: StateFlow<UiState<Car>> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadCar(model: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val car = getCarUseCase(model)
                _uiState.value = UiState.Success(car)
                val favorites = getFavoritesUseCase()
                _isFavorite.value = favorites.any { it.model == model }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun toggleFavorite(car: Car) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                removeFavoriteUseCase(car)
                _isFavorite.value = false
            } else {
                addFavoriteUseCase(car)
                _isFavorite.value = true
            }
        }
    }
}