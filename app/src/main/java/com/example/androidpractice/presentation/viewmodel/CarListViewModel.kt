package com.example.androidpractice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.usecase.GetCarsUseCase
import com.example.androidpractice.presentation.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListViewModel(
    private val getCarsUseCase: GetCarsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Car>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Car>>> = _uiState

    init {
        loadCars()
    }

    fun loadCars() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val cars = getCarsUseCase()
                _uiState.value = UiState.Success(cars)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}