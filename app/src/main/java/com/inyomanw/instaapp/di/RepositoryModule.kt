package com.inyomanw.instaapp.di

import com.google.firebase.auth.FirebaseAuth
import com.inyomanw.instaapp.data.repository.AuthRepositoryImpl
import com.inyomanw.instaapp.data.repository.LikeCommentRepositoryImpl
import com.inyomanw.instaapp.data.repository.PostRepositoryImpl
import com.inyomanw.instaapp.data.source.FirebaseAuthDataSource
import com.inyomanw.instaapp.data.source.LikeCommentDataSource
import com.inyomanw.instaapp.data.source.PostDataSource
import com.inyomanw.instaapp.domain.repository.AuthRepository
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import com.inyomanw.instaapp.domain.repository.PostRepository
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

    @Provides
    @Singleton
    fun providesPostRepository(
        postDataSource: PostDataSource,
        likeCommentDataSource: LikeCommentDataSource,
        auth: FirebaseAuth
    ): PostRepository = PostRepositoryImpl(postDataSource, likeCommentDataSource, auth)

    @Provides
    @Singleton
    fun providesLikeCommentRepository(
        likeCommentDataSource: LikeCommentDataSource,
    ): LikeCommentRepository = LikeCommentRepositoryImpl(likeCommentDataSource)

}