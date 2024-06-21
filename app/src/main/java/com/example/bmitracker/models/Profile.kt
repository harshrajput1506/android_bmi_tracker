package com.example.bmitracker.models

data class Profile(
    val bmi : String = "0",
    val height: String = "",
    val weight: String = "",
    val age: String = "",
    val gender: String = "male",
    val dob : String? = null,
    val weightRecord: List<WeightRecord> = emptyList(),
    val lastUpdated: String? = null
)
