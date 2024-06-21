package com.example.bmitracker.models


data class User(
    val uid: String = "",
    val name: String = "",
    val email: String ="",
    val profile: Profile = Profile()
)