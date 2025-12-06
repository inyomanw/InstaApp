package com.inyomanw.instaapp.domain.model

data class PostDomain(
    val postId: String,
    val userId: String,
    val userName: String,
    val userPhotoUrl: String,
    val imageUrl: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val createdAt: Long
)