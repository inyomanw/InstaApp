package com.inyomanw.instaapp.data.repository

import com.inyomanw.instaapp.data.model.Comment
import com.inyomanw.instaapp.data.source.LikeCommentDataSource
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.CommentDomain
import com.inyomanw.instaapp.domain.repository.LikeCommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LikeCommentRepositoryImpl @Inject constructor(
    private val dataSource: LikeCommentDataSource
) : LikeCommentRepository {

    override suspend fun likePost(postId: String, userId: String): Flow<UiState<Boolean>> = flow {
        try {
            emit(UiState.Loading)
            dataSource.likePost(postId, userId)
            emit(UiState.Success(true))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to like post: ${e.message}", e))
        }
    }

    override suspend fun unlikePost(postId: String, userId: String): Flow<UiState<Boolean>> = flow {
        try {
            emit(UiState.Loading)
            dataSource.unlikePost(postId, userId)
            emit(UiState.Success(true))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to unlike post: ${e.message}", e))
        }
    }

    override suspend fun getComments(postId: String): Flow<UiState<List<CommentDomain>>> = flow {
        try {
            emit(UiState.Loading)
            val comments = dataSource.getComments(postId)
            emit(UiState.Success(comments.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to fetch comments: ${e.message}", e))
        }
    }

    override suspend fun addComment(
        postId: String,
        userId: String,
        userName: String,
        userPhotoUrl: String,
        text: String
    ): Flow<UiState<CommentDomain>> = flow {
        try {
            emit(UiState.Loading)

            val comment = Comment(
                postId = postId,
                userId = userId,
                userName = userName,
                userPhotoUrl = userPhotoUrl,
                text = text
            )

            val commentId = dataSource.addComment(comment)
            val createdComment = comment.copy(commentId = commentId)

            emit(UiState.Success(createdComment.toDomain()))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to add comment: ${e.message}", e))
        }
    }

    override suspend fun deleteComment(
        commentId: String,
        postId: String,
        userId: String
    ): Flow<UiState<Boolean>> = flow {
        try {
            emit(UiState.Loading)
            dataSource.deleteComment(commentId, postId)
            emit(UiState.Success(true))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to delete comment: ${e.message}", e))
        }
    }

    private fun Comment.toDomain() = CommentDomain(
        commentId = commentId,
        postId = postId,
        userId = userId,
        userName = userName,
        userPhotoUrl = userPhotoUrl,
        text = text,
        createdAt = createdAt?.time ?: 0L
    )
}
