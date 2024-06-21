package com.example.bmitracker.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmitracker.data.DataSession
import com.example.bmitracker.models.Profile
import com.example.bmitracker.models.User
import com.example.bmitracker.ui.theme.cyan
import com.example.bmitracker.ui.theme.green
import com.example.bmitracker.ui.theme.orange
import com.example.bmitracker.ui.theme.red
import com.example.bmitracker.ui.theme.yellow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataSession: DataSession
) : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    private val _state = mutableStateOf(HomeState())
    val state = _state

    companion object{
        private const val TAG = "HomeViewModel"
    }

    init {
        getDataFromDataStore()
        getUserData()
    }



    fun categorizeBMI(bmi: Double): String {
        return when (bmi) {
            in 0.0..18.4 -> "underweight"
            in 18.5..24.9 -> "normal"
            in 25.0..29.9 -> "overweight"
            in 30.0..34.9 -> "obese class I"
            in 35.0..100.0 -> "obese class II"
            else -> if (bmi >= 0) "obese" else "Invalid BMI" // Added a check for invalid BMI
        }
    }

    private fun getUserData() {
        currentUser?.uid?.let { uid ->
            firestore.collection("users").document(uid)
                .get().addOnSuccessListener { document ->
                    if(document.data.isNullOrEmpty()){
                        // Register time it is
                        //_state.value = _state.value.copy(isLoading = false)
                    } else {
                        // Get data
                        val user = document.toObject<User>()
                        user?.let {
                            dataInDataStore(it.profile)
                            _state.value = HomeState(profile = it.profile)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting document", exception)
                }
        }
    }

    private fun getDataFromDataStore(){
        viewModelScope.launch {
            dataSession.getBMI().collect { bmi ->
                _state.value = state.value.copy(profile = _state.value.profile.copy(bmi = bmi))
            }
        }
        viewModelScope.launch {
            dataSession.getWeight().collect { weight ->
                _state.value = state.value.copy(profile = _state.value.profile.copy(weight = weight))
            }
        }
        viewModelScope.launch {
            dataSession.getHeight().collect { height ->
                _state.value = state.value.copy(profile = _state.value.profile.copy(height = height))
            }
        }
        viewModelScope.launch {
            dataSession.getAge().collect { age ->
                _state.value = state.value.copy(profile = _state.value.profile.copy(age = age))
            }
        }
        viewModelScope.launch {
            dataSession.getLastUpdated().collect { lastUpdated ->
                _state.value = state.value.copy(profile = _state.value.profile.copy(lastUpdated = lastUpdated))
            }
        }
    }
    private fun dataInDataStore(profile: Profile) {
        viewModelScope.launch {
            dataSession.setAge(profile.age)
            dataSession.setHeight(profile.height)
            dataSession.setWeight(profile.weight)
            dataSession.setBMI(profile.bmi)
            dataSession.setLastUpdated(profile.lastUpdated!!)
            dataSession.setUserLogin(true)

        }
    }

    fun bmiColor(category: String): Color {
        return when (category) {
            "underweight" -> cyan
            "normal" -> green
            "overweight" -> yellow
            "obese class I" -> orange
            "obese class II" -> red
            else -> Color.White // Default for invalid categories
        }
    }


}