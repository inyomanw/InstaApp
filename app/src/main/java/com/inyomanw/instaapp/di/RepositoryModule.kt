package com.inyomanw.instaapp.di

import com.inyomanw.instaapp.data.repository.AuthRepositoryImpl
import com.inyomanw.instaapp.data.source.FirebaseAuthDataSource
import com.inyomanw.instaapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesAuthRepository(firebaseAuthDataSource: FirebaseAuthDataSource): AuthRepository {
        return AuthRepositoryImpl(firebaseAuthDataSource)
    }
}