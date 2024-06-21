package com.example.bmitracker.navigation

import androidx.lifecycle.ViewModel
import com.example.bmitracker.data.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {
    val isLoggedIn
        get() = sharedPreferencesManager.isLoggedIn()
}