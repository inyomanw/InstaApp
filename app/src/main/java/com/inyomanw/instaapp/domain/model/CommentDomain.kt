package com.inyomanw.instaapp.domain.model

data class CommentDomain(
    val commentId: String,
    val postId: String,
    val userId: String,
    val userName: String,
    val userPhotoUrl: String,
    val text: String,
    val createdAt: Long
)
