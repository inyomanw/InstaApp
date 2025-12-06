package com.inyomanw.instaapp.domain.usecase

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.CommentDomain
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: LikeCommentRepository
) {
    suspend operator fun invoke(
        postId: String,
        userId: String,
        userName: String,
        userPhotoUrl: String,
        text: String
    ): Flow<UiState<CommentDomain>> {
        return repository.addComment(postId, userId, userName, userPhotoUrl, text)
    }
}
