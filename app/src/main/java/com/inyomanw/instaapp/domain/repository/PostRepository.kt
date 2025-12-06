package com.inyomanw.instaapp.domain.repository

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.PostDomain
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getAllPosts(limit: Int, lastPostId: String?): Flow<UiState<List<PostDomain>>>
    suspend fun createPost(
        userId: String,
        userName: String,
        userPhotoUrl: String,
        imageBytes: ByteArray,
        caption: String
    ): Flow<UiState<PostDomain>>
}