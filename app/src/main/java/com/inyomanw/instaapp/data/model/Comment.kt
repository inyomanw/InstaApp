package com.inyomanw.instaapp.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Comment(
    @PropertyName("commentId") val commentId: String = "",
    @PropertyName("postId") val postId: String = "",
    @PropertyName("userId") val userId: String = "",
    @PropertyName("userName") val userName: String = "",
    @PropertyName("userPhotoUrl") val userPhotoUrl: String = "",
    @PropertyName("text") val text: String = "",
    @ServerTimestamp @PropertyName("createdAt") val createdAt: Date? = null
)
