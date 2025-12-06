package com.inyomanw.instaapp.data.repository

import com.google.firebase.auth.AuthCredential
import com.inyomanw.instaapp.data.model.User
import com.inyomanw.instaapp.data.source.FirebaseAuthDataSource
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.UserDomain
import com.inyomanw.instaapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Flow<UiState<UserDomain>> = flow {
        try {
            emit(UiState.Loading)
            val user = dataSource.registerWithEmail(email, password, displayName)
            dataSource.saveUserToFirestore(user)
            emit(UiState.Success(user.toDomain()))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Registration failed"))
        }
    }

    override suspend fun login(email: String, password: String): Flow<UiState<UserDomain>> = flow {
        try {
            emit(UiState.Loading)
            val user = dataSource.loginWithEmail(email, password)
            emit(UiState.Success(user.toDomain()))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Login failed"))
        }
    }

    override suspend fun signInWithGoogle(credential: AuthCredential): Flow<UiState<UserDomain>> = flow {
        try {
            emit(UiState.Loading)
            val user = dataSource.signInWithGoogleCredential(credential)
            dataSource.saveUserToFirestore(user)
            emit(UiState.Success(user.toDomain()))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Google sign-in failed"))
        }
    }

    override suspend fun saveUserToFirestore(user: UserDomain): Flow<UiState<Boolean>> = flow {
        try {
            emit(UiState.Loading)
            val dataUser = User(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl
            )
            val result = dataSource.saveUserToFirestore(dataUser)
            emit(UiState.Success(result))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Failed to save user"))
        }
    }

    override fun getCurrentUser(): UserDomain? {
        return dataSource.getCurrentUser()?.toDomain()
    }

    override suspend fun logout() {
        dataSource.logout()
    }

    private fun User.toDomain() = UserDomain(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl
    )
}