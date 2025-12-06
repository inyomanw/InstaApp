package com.inyomanw.instaapp.domain.usecase

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.PostDomain
import com.inyomanw.instaapp.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        userId: String,
        userName: String,
        userPhotoUrl: String,
        imageBytes: ByteArray,
        caption: String
    ): Flow<UiState<PostDomain>> {
        return repository.createPost(userId, userName, userPhotoUrl, imageBytes, caption)
    }
}