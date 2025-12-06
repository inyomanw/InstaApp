package com.inyomanw.instaapp.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    @PropertyName("postId") val postId: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("userName") val userName: String = "",
    @PropertyName("userPhotoUrl") val userPhotoUrl: String = "",
    @PropertyName("imageUrl") val imageUrl: String = "",
    @PropertyName("caption") val caption: String = "",
    @PropertyName("likesCount") val likesCount: Int = 0,
    @PropertyName("commentsCount") val commentsCount: Int = 0,
    @ServerTimestamp @PropertyName("createdAt") val createdAt: Date? = null
)