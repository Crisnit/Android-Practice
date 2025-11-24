package com.example.androidpractice.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.presentation.FilterCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object PreferencesKeys {
    val MANUFACTURER = stringPreferencesKey("manufacturer")
    val MIN_YEAR = intPreferencesKey("min_year")
    val CAR_CLASS = stringPreferencesKey("car_class")
}

data class FilterUiState(
    val manufacturer: String = "",
    val minYear: String = "",
    val carClass: String = ""
)

class FilterViewModel(
    private val dataStore: DataStore<Preferences>,
    private val filterCache: FilterCache
) : ViewModel() {
    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState

    init {
        loadFilters()
    }

    private fun loadFilters() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            _uiState.value = FilterUiState(
                manufacturer = prefs[PreferencesKeys.MANUFACTURER] ?: "",
                minYear = (prefs[PreferencesKeys.MIN_YEAR]?.toString() ?: ""),
                carClass = prefs[PreferencesKeys.CAR_CLASS] ?: ""
            )
        }
    }

    fun updateManufacturer(manufacturer: String) {
        _uiState.value = _uiState.value.copy(manufacturer = manufacturer)
    }

    fun updateMinYear(minYear: String) {
        _uiState.value = _uiState.value.copy(minYear = minYear)
    }

    fun updateCarClass(carClass: String) {
        _uiState.value = _uiState.value.copy(carClass = carClass)
    }

    fun applyFilters() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                val manufacturer = _uiState.value.manufacturer
                if (manufacturer.isNotBlank()) {
                    preferences[PreferencesKeys.MANUFACTURER] = manufacturer
                } else {
                    preferences.remove(PreferencesKeys.MANUFACTURER)
                }
                val minYearInt = _uiState.value.minYear.toIntOrNull() ?: 0
                preferences[PreferencesKeys.MIN_YEAR] = minYearInt
                val carClass = _uiState.value.carClass
                if (carClass.isNotBlank()) {
                    preferences[PreferencesKeys.CAR_CLASS] = carClass
                } else {
                    preferences.remove(PreferencesKeys.CAR_CLASS)
                }
            }
            val prefs = dataStore.data.first()
            filterCache.setFiltersApplied(
                !prefs[PreferencesKeys.MANUFACTURER].isNullOrBlank() ||
                        prefs[PreferencesKeys.MIN_YEAR] != 0 ||
                        !prefs[PreferencesKeys.CAR_CLASS].isNullOrBlank()
            )
        }
    }
}