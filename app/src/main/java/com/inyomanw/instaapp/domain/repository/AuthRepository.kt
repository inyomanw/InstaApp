package com.inyomanw.instaapp.domain.repository

import com.google.firebase.auth.AuthCredential
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, password: String, displayName: String): Flow<UiState<UserDomain>>
    suspend fun login(email: String, password: String): Flow<UiState<UserDomain>>
    suspend fun signInWithGoogle(credential: AuthCredential): Flow<UiState<UserDomain>>
    suspend fun saveUserToFirestore(user: UserDomain): Flow<UiState<Boolean>>
    fun getCurrentUser(): UserDomain?
    suspend fun logout()
}