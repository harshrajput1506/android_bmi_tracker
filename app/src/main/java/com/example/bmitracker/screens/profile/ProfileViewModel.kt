package com.example.bmitracker.screens.profile

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmitracker.data.DataSession
import com.example.bmitracker.data.SharedPreferencesManager
import com.example.bmitracker.models.User
import com.example.bmitracker.models.WeightRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStore : DataSession,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore
    val user = auth.currentUser

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state
    private var isRegister: Boolean = false;
    private var initialUserState = User()

    init {
        getUserData()
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private fun getUserData() {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid)
                .get().addOnSuccessListener { document ->
                    if(document.data.isNullOrEmpty()){
                        // Register time it is
                        isRegister = true
                        _state.value = _state.value.copy(isLoading = false)
                    } else {
                        // Get data
                        val user = document.toObject<User>()
                        user?.let {
                            initialUserState = it
                            dataInDataStore(it)
                            sharedPreferencesManager.saveIsLoggedIn(value = true)
                            _state.value = ProfileState(user = it, isLoading = false)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting document", exception)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserData() {
        user?.let { user ->
            var userData = _state.value.user
            userData = userData.copy(
                uid = user.uid,
                name = user.displayName?.lowercase() ?: "master",
                email = user.email ?: "",
                profile = userData.profile.copy(
                    bmi = bmi(),
                    age = calculateAge(userData.profile.dob).toString(),
                    weightRecord = weightRecords(),
                    lastUpdated = if(isBMIChanges()) getCurrentDate() else initialUserState.profile.lastUpdated
                )
            )
            firestore.collection("users").document(user.uid).set(userData).addOnSuccessListener {
                Log.d(TAG, "addUserData - User Data Successfully Added: ")
                dataInDataStore(userData)
                _state.value = _state.value.copy(isError = false, isLoading = false, isUpdated = true, user = userData)

            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error writing document", exception)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSave() {
        val user = _state.value.user
        if(user.profile.weight.isEmpty() || user.profile.height.isEmpty() || user.profile.dob.isNullOrEmpty()){
            _state.value = _state.value.copy(isError = true)
        } else {
            if(isInputChanges()) {
                _state.value = _state.value.copy(isLoading = true)
                saveUserData()
            } else {
                // Back to previous screen
                _state.value = _state.value.copy(isUpdated = true)
            }

        }



    }

    @SuppressLint("DefaultLocale")
    private fun bmi() : String {
        val weight = _state.value.user.profile.weight.toDouble()
        val height = _state.value.user.profile.height.toDouble() / 100
        val bmi = weight / (height * height)
        return String.format("%.2f", bmi)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateAge(dobString: String?): Int {
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy") // Customize format

        return try {
            val dob = LocalDate.parse(dobString, formatter)
            val today = LocalDate.now()
            Period.between(dob, today).years
        } catch (e: DateTimeParseException) {
            // Handle invalid date format (e.g., show error message)
            -1 // Return -1 or another appropriate value for invalid input
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy")
        return currentDate.format(formatter)
    }

    private fun isInputChanges() : Boolean {
        val currentUserState = _state.value.user
        return currentUserState.profile.dob != initialUserState.profile.dob
                || currentUserState.profile.gender != initialUserState.profile.gender
                || currentUserState.profile.weight != initialUserState.profile.weight
                || currentUserState.profile.height != initialUserState.profile.height
    }

    private fun isBMIChanges() : Boolean {
        return bmi() != initialUserState.profile.bmi
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun weightRecords() : List<WeightRecord> {
        if(initialUserState.profile.weight != _state.value.user.profile.weight){
            if(initialUserState.profile.weightRecord.isEmpty()) {
                return listOf(
                    WeightRecord(
                        date = getCurrentDate(),
                        weight = _state.value.user.profile.weight
                    )
                )
            } else {
                return initialUserState.profile.weightRecord.plus(
                    listOf(
                        WeightRecord(
                            date = getCurrentDate(),
                            weight = _state.value.user.profile.weight
                        )
                    )
                )
            }
        }
        return initialUserState.profile.weightRecord
    }

    private fun dataInDataStore(user: User) {
        viewModelScope.launch {
            dataStore.setAge(user.profile.age)
            dataStore.setHeight(user.profile.height)
            dataStore.setWeight(user.profile.weight)
            dataStore.setBMI(user.profile.bmi)
            dataStore.setLastUpdated(user.profile.lastUpdated!!)
            dataStore.setUserLogin(true)

        }
    }

    fun updateWeightInput(value : String) {
        val profile = _state.value.user.profile
        _state.value = _state.value.copy(user = _state.value.user.copy(profile = profile.copy(weight = value)))
    }

    fun updateHeightInput(value : String) {
        val profile = _state.value.user.profile
        _state.value = _state.value.copy(user = _state.value.user.copy(profile = profile.copy(height = value)))
    }

    fun updateGender(isMale : Boolean){
        val profile = _state.value.user.profile
        _state.value = _state.value.copy(user = _state.value.user.copy(profile = profile.copy(gender = if(isMale) "male" else "female")))
    }
    fun updateSelectedDate(date : String){
        val profile = _state.value.user.profile
        _state.value = _state.value.copy(user = _state.value.user.copy(profile = profile.copy(dob = date)))
    }

    fun updateError(b: Boolean) {
        _state.value = _state.value.copy(isError = b)
    }
    fun setIsChangedFalse() {
        _state.value = _state.value.copy(isUpdated = false)
    }


}