package com.example.bmitracker.screens.profile

import com.example.bmitracker.models.User

data class ProfileState(
    val user: User = User(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val isUpdated : Boolean = false,
    val isRegistered : Boolean = false

)