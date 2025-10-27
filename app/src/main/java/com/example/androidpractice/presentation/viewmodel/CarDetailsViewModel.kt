package com.example.androidpractice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.usecase.GetCarUseCase
import com.example.androidpractice.presentation.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarDetailsViewModel(
    private val getCarUseCase: GetCarUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Car>>(UiState.Loading)
    val uiState: StateFlow<UiState<Car>> = _uiState.asStateFlow();

    fun loadCar(model: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val car = getCarUseCase(model)
                _uiState.value = UiState.Success(car)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}