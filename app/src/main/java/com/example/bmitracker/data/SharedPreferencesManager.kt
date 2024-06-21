package com.example.bmitracker.data

import android.content.Context
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(
    private val context: Context
) {

    companion object{
        private const val PREFERENCES_NAME = "my_app_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }


    fun saveIsLoggedIn(value: Boolean) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, value)
            apply()
        }
    }

    fun isLoggedIn(defaultValue: Boolean = false): Boolean {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, defaultValue)
    }

}
