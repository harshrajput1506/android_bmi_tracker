package com.example.bmitracker.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object OnBoard

    @Serializable
    object BasicProfile

    @Serializable
    object Home
}