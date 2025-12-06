package com.inyomanw.instaapp.domain.repository

import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.CommentDomain
import kotlinx.coroutines.flow.Flow

interface LikeCommentRepository {
    suspend fun likePost(postId: String, userId: String): Flow<UiState<Boolean>>
    suspend fun unlikePost(postId: String, userId: String): Flow<UiState<Boolean>>
    suspend fun getComments(postId: String): Flow<UiState<List<CommentDomain>>>
    suspend fun addComment(
        postId: String,
        userId: String,
        userName: String,
        userPhotoUrl: String,
        text: String
    ): Flow<UiState<CommentDomain>>
    suspend fun deleteComment(commentId: String, postId: String, userId: String): Flow<UiState<Boolean>>
}
