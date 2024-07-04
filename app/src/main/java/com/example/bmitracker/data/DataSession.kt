package com.example.bmitracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataSession @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object{
        const val DATA = "DATA"
        private const val BMI = "bmi"
        private const val IS_LOGIN = "is_login"
        private const val HEIGHT = "height"
        private const val WEIGHT = "weight"
        private const val AGE = "age"
        private const val LAST_UPDATED = "last_updated"
        val bmiKey = stringPreferencesKey(BMI)
        val isLoginKey = booleanPreferencesKey(IS_LOGIN)
        val heightKey = stringPreferencesKey(HEIGHT)
        val weightKey = stringPreferencesKey(WEIGHT)
        val ageKey = stringPreferencesKey(AGE)
        val lastUpdatedKey = stringPreferencesKey(LAST_UPDATED)
    }

    fun isUserLogin() : Flow<Boolean> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[isLoginKey] ?: false
        }
    }

    suspend fun setUserLogin(isLogin: Boolean){
        dataStore.edit { preference ->
            preference[isLoginKey] = isLogin
        }
    }

    fun getBMI() : Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[bmiKey] ?: "0"
        }
    }

    suspend fun setBMI(bmi: String){
        dataStore.edit { preference ->
            preference[bmiKey] = bmi
        }
    }

    fun getHeight() : Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[heightKey] ?: ""
        }
    }

    suspend fun setHeight(height: String){
        dataStore.edit { preference ->
            preference[heightKey] = height
        }
    }

    fun getWeight() : Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[weightKey] ?: ""
        }
    }

    suspend fun setWeight(weight: String) {
        dataStore.edit { preference ->
            preference[weightKey] = weight
        }
    }

    fun getAge() : Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[ageKey] ?: ""
        }
    }

    suspend fun setAge(age: String) {
        dataStore.edit { preference ->
            preference[ageKey] = age
        }
    }

    fun getLastUpdated() : Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[lastUpdatedKey] ?: ""
        }
    }

    suspend fun setLastUpdated(lastUpdated: String) {
        dataStore.edit { preference ->
            preference[lastUpdatedKey] = lastUpdated
        }
    }
}