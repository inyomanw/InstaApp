package com.inyomanw.instaapp.domain.usecase

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.CommentDomain
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: LikeCommentRepository
) {
    suspend operator fun invoke(postId: String): Flow<UiState<List<CommentDomain>>> {
        return repository.getComments(postId)
    }
}
