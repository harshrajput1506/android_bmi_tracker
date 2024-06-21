package com.example.bmitracker.navigation

import com.example.bmitracker.models.User
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object OnBoard

    @Serializable
    object BasicProfile

    @Serializable
    object Home
}