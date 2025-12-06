package com.inyomanw.instaapp.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import com.inyomanw.instaapp.domain.usecase.GoogleSignInUseCase
import com.inyomanw.instaapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<UserDomain>>(UiState.Idle)
    val loginState: StateFlow<UiState<UserDomain>> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).collect { result ->
                _loginState.value = result
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            googleSignInUseCase(credential).collect { result ->
                _loginState.value = result
            }
        }
    }

    fun resetState() {
        _loginState.value = UiState.Idle
    }
}
