package com.example.bmitracker.screens.onboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    val signInClient: GoogleSignInClient
) : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _state = mutableStateOf(AuthState())
    val state : State<AuthState> = _state

    companion object {
        private const val TAG = "OnBoardViewModel"
    }

    fun signInWithGoogle(idToken : String) {
        _state.value = AuthState(isLoading = true)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success " + auth.currentUser?.email)
                    val user = auth.currentUser
                    _state.value = AuthState(
                        isLoading = false,
                        isLoggedIn = true,
                    )
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    _state.value = AuthState(
                        isError = true
                    )
                }
            }
    }

    fun updateIsLoggedIn(isLoggedIn : Boolean) {
        _state.value = AuthState(
            isLoggedIn = isLoggedIn
        )
    }



}