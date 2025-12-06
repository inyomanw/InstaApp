package com.inyomanw.instaapp.domain.usecase

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import com.inyomanw.instaapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): Flow<UiState<UserDomain>> {
        return repository.register(email, password, displayName)
    }
}