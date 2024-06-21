package com.example.bmitracker.screens.onboard

data class AuthState(
    var isLoading : Boolean = false,
    var isLoggedIn : Boolean = false,
    var isError: Boolean = false
)