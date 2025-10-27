package com.example.androidpractice.presentation.viewmodel
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.domain.model.Car
import com.example.androidpractice.domain.usecase.GetCarsUseCase
import com.example.androidpractice.presentation.ui.UiState
import com.example.androidpractice.content.PreferencesKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
        loadCarsWithFilters(null)
    }
    fun loadCarsWithFilters(dataStore: DataStore<Preferences>?) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                var cars = getCarsUseCase()
                if (dataStore != null) {
                    val prefs = dataStore.data.first()
                    val manufacturer = prefs[PreferencesKeys.MANUFACTURER]
                    val minYear = prefs[PreferencesKeys.MIN_YEAR]
                    val carClass = prefs[PreferencesKeys.CAR_CLASS]
                    if (!manufacturer.isNullOrBlank()) {
                        cars = cars.filter { it.manufacturer.contains(manufacturer, ignoreCase = true) }
                    }
                    if (minYear != null) {
                        cars = cars.filter { parseMinYear(it.productionYears) >= minYear }
                    }
                    if (!carClass.isNullOrBlank()) {
                        cars = cars.filter { it.carClass.contains(carClass, ignoreCase = true) }
                    }
                }
                _uiState.value = UiState.Success(cars)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
    private fun parseMinYear(years: String): Int {
        val firstYear = years.split("-").firstOrNull()?.trim()?.toIntOrNull()
        return firstYear ?: 0
    }
}