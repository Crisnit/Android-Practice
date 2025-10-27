package com.example.androidpractice.presentation
class FilterCache {
    var hasFiltersApplied: Boolean = false
        private set
    fun setFiltersApplied(applied: Boolean) {
        hasFiltersApplied = applied
    }
}