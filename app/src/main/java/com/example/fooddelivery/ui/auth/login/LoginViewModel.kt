package com.example.fooddelivery.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

    private val _loggedIn = MutableStateFlow("")
    val loggedIn = _loggedIn

    fun login(email : String, password : String) = viewModelScope.launch {
        try {
            authRepository.login(email, password).let {
                _loggedIn.value = it
                Log.d("ResultExcVm", it)
            }
        } catch (e : Exception){
            _loggedIn.value = e.message.toString()
            Log.d("ResultExcVM", e.message.toString())
        }
    }

}