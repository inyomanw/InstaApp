package com.inyomanw.instaapp.domain.usecase


import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val repository: LikeCommentRepository
) {
    suspend operator fun invoke(commentId: String, postId: String, userId: String): Flow<UiState<Boolean>> {
        return repository.deleteComment(commentId, postId, userId)
    }
}
