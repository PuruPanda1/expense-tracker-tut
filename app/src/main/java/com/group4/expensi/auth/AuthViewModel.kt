package com.group4.expensi.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var cnfPassword by mutableStateOf("")
        private set

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun onCnfPasswordChange(value: String) {
        cnfPassword = value
    }

    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.UnAuthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password can not be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password can not be empty")
            return
        }

        if(!password.equals(cnfPassword)){
            _authState.value = AuthState.Error("Password does not match")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

}

sealed class AuthState{
    object Authenticated: AuthState()
    object UnAuthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}