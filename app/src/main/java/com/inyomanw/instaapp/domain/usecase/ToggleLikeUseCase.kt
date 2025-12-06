package com.inyomanw.instaapp.domain.usecase

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repository: LikeCommentRepository
) {
    suspend operator fun invoke(postId: String, userId: String, isCurrentlyLiked: Boolean): Flow<UiState<Boolean>> {
        return if (isCurrentlyLiked) {
            repository.unlikePost(postId, userId)
        } else {
            repository.likePost(postId, userId)
        }
    }
}
