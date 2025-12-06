package com.inyomanw.instaapp.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import com.inyomanw.instaapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): Flow<UiState<UserDomain>> {
        return repository.signInWithGoogle(credential)
    }
}