package com.example.androidpractice.content

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferencesKeys {
    val MANUFACTURER = stringPreferencesKey("manufacturer")
    val MIN_YEAR = intPreferencesKey("min_year")
    val CAR_CLASS = stringPreferencesKey("car_class")
    val PROFILE_NAME = stringPreferencesKey("profile_name")
    val PROFILE_AVATAR_URI = stringPreferencesKey("profile_avatar_uri")
    val PROFILE_RESUME_URL = stringPreferencesKey("profile_resume_url")
}