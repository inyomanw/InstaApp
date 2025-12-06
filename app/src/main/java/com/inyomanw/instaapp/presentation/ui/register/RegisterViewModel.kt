package com.inyomanw.instaapp.presentation.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import com.inyomanw.instaapp.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<UiState<UserDomain>>(UiState.Idle)
    val registerState: StateFlow<UiState<UserDomain>> = _registerState.asStateFlow()

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            registerUseCase(email, password, displayName).collect { result ->
                _registerState.value = result
            }
        }
    }

    fun resetState() {
        _registerState.value = UiState.Idle
    }
}
