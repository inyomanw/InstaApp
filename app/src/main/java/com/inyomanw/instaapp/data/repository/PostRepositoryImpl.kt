package com.inyomanw.instaapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.inyomanw.instaapp.data.model.Post
import com.inyomanw.instaapp.data.source.LikeCommentDataSource
import com.inyomanw.instaapp.data.source.PostDataSource
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.PostDomain
import com.inyomanw.instaapp.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDataSource: PostDataSource,
    private val likeCommentDataSource: LikeCommentDataSource,
    private val auth: FirebaseAuth
) : PostRepository {

    private var lastDocument: DocumentSnapshot? = null

    override suspend fun getAllPosts(limit: Int, lastPostId: String?): Flow<UiState<List<PostDomain>>> = flow {
        try {
            emit(UiState.Loading)

            val currentUserId = auth.currentUser?.uid ?: ""
            val (posts, lastDoc) = postDataSource.getAllPosts(limit, lastDocument)
            lastDocument = lastDoc

            val postsWithLikes = posts.map { post ->
                val isLiked = if (currentUserId.isNotEmpty()) {
                    likeCommentDataSource.isPostLikedByUser(post.postId, currentUserId)
                } else false

                post.toDomain(isLiked)
            }

            emit(UiState.Success(postsWithLikes))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to fetch posts: ${e.message}", e))
        }
    }

    override suspend fun createPost(
        userId: String,
        userName: String,
        userPhotoUrl: String,
        imageBytes: ByteArray,
        caption: String
    ): Flow<UiState<PostDomain>> = flow {
        try {
            emit(UiState.Loading)

            val imageUrl = postDataSource.uploadImage(userId, imageBytes)

            val post = Post(
                userId = userId,
                userName = userName,
                userPhotoUrl = userPhotoUrl,
                imageUrl = imageUrl,
                caption = caption,
                likesCount = 0,
                commentsCount = 0
            )

            val postId = postDataSource.createPost(post)
            val createdPost = post.copy(postId = postId)

            emit(UiState.Success(createdPost.toDomain(false)))
        } catch (e: Exception) {
            emit(UiState.Error("Failed to create post: ${e.message}", e))
        }
    }

    private fun Post.toDomain(isLiked: Boolean) = PostDomain(
        postId = postId,
        userId = userId,
        userName = userName,
        userPhotoUrl = userPhotoUrl,
        imageUrl = imageUrl,
        caption = caption,
        likesCount = likesCount,
        commentsCount = commentsCount,
        createdAt = createdAt?.time ?: 0L,
        isLikedByCurrentUser = isLiked
    )
}