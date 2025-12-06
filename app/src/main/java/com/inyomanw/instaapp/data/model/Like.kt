package com.inyomanw.instaapp.data.model

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Like(
    @PropertyName("likeId") val likeId: String = "",
    @PropertyName("postId") val postId: String = "",
    @PropertyName("userId") val userId: String = "",
    @ServerTimestamp @PropertyName("createdAt") val createdAt: Date? = null
)