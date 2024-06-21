package com.example.bmitracker.utils

import com.example.bmitracker.models.WeightRecord

object Constants {
    const val wave_emoji = "👋";
    const val CLIENT_ID = "253532087938-5fsusidqqcuemht9qucodn3748gfkdcs.apps.googleusercontent.com"
    val sampleData = listOf(
        WeightRecord("Fri, 21 Jun 2024", "70.0"), // Your original entry
        WeightRecord("Sun, 23 Jun 2024", "70.5"), // Slight increase
        WeightRecord("Tue, 25 Jun 2024", "70.3"), // Minor fluctuation
        WeightRecord("Thu, 27 Jun 2024", "71.0"), // Noticeable increase
        WeightRecord("Sat, 29 Jun 2024", "71.2"), // Further increase
        WeightRecord("Mon, 01 Jul 2024", "70.9"), // Slight decrease
        WeightRecord("Wed, 03 Jul 2024", "70.7"), // Another minor decrease
        WeightRecord("Fri, 05 Jul 2024", "70.4"), // Continued slight decrease
        WeightRecord("Sun, 07 Jul 2024", "70.1"),
        WeightRecord("Tue, 09 Jul 2024", "69.8"),
        WeightRecord("Thu, 11 Jul 2024", "69.6"),
        WeightRecord("Sat, 13 Jul 2024", "69.4"),
        WeightRecord("Mon, 15 Jul 2024", "69.2"),
        WeightRecord("Wed, 17 Jul 2024", "69.0"),
        WeightRecord("Fri, 19 Jul 2024", "68.8"),
        WeightRecord("Sun, 21 Jul 2024", "69.1"),
        WeightRecord("Tue, 23 Jul 2024", "69.4"),
        WeightRecord("Thu, 25 Jul 2024", "69.7"),
        WeightRecord("Sat, 27 Jul 2024", "70.0"),
        WeightRecord("Mon, 29 Jul 2024", "70.3"),
        WeightRecord("Wed, 31 Jul 2024", "70.6")

    )
    val sampleData1 = listOf(
        WeightRecord("Fri, 21 Jun 2024", "70.0"),
        WeightRecord("Fri, 21 Jun 2024", "70.2"), // Duplicate date
        WeightRecord("Sun, 23 Jun 2024", "70.5"),
        WeightRecord("Tue, 25 Jun 2024", "70.3"),
        WeightRecord("Tue, 25 Jun 2024", "70.1"), // Duplicate date
        // ... rest of your data (add more duplicates as needed)
    )
}