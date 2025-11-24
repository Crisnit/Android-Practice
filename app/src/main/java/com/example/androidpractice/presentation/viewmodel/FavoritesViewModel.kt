package com.example.androidpractice.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.usecase.GetFavoritesUseCase
import com.example.androidpractice.presentation.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Car>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Car>>> = _uiState

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = getFavoritesUseCase()
                _uiState.value = UiState.Success(data = favorites)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(message = "Unknown error")
            }
        }
    }
}