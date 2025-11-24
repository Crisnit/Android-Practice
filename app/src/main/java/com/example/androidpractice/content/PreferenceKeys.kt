package com.example.androidpractice.content

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferencesKeys {
    val MANUFACTURER = stringPreferencesKey("manufacturer")
    val MIN_YEAR = intPreferencesKey("min_year")
    val CAR_CLASS = stringPreferencesKey("car_class")
}