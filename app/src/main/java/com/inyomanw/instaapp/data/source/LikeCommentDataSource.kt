package com.inyomanw.instaapp.data.source

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.inyomanw.instaapp.data.model.Comment
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LikeCommentDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun likePost(postId: String, userId: String) {
        val likeId = "${postId}_${userId}"

        firestore.runTransaction { transaction ->
            val postRef = firestore.collection("posts").document(postId)
            val postSnapshot = transaction.get(postRef)

            val currentLikes = postSnapshot.getLong("likesCount") ?: 0

            val likeData = hashMapOf(
                "likeId" to likeId,
                "postId" to postId,
                "userId" to userId,
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            transaction.set(firestore.collection("likes").document(likeId), likeData)
            transaction.update(postRef, "likesCount", currentLikes + 1)
        }.await()
    }

    suspend fun unlikePost(postId: String, userId: String) {
        val likeId = "${postId}_${userId}"

        firestore.runTransaction { transaction ->
            val postRef = firestore.collection("posts").document(postId)
            val postSnapshot = transaction.get(postRef)

            val currentLikes = postSnapshot.getLong("likesCount") ?: 0
            transaction.delete(firestore.collection("likes").document(likeId))
            transaction.update(postRef, "likesCount", maxOf(0, currentLikes - 1))
        }.await()
    }

    suspend fun isPostLikedByUser(postId: String, userId: String): Boolean {
        val likeId = "${postId}_${userId}"
        val doc = firestore.collection("likes").document(likeId).get().await()
        return doc.exists()
    }

    suspend fun getComments(postId: String): List<Comment> {
        val snapshot = firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
    }

    suspend fun addComment(comment: Comment): String {
        val docRef = firestore.collection("comments").document()
        val newComment = comment.copy(commentId = docRef.id)

        firestore.runTransaction { transaction ->
            val postRef = firestore.collection("posts").document(comment.postId)
            val postSnapshot = transaction.get(postRef)

            val currentComments = postSnapshot.getLong("commentsCount") ?: 0
            transaction.set(docRef, newComment)
            transaction.update(postRef, "commentsCount", currentComments + 1)
        }.await()

        return docRef.id
    }

    suspend fun deleteComment(commentId: String, postId: String) {
        firestore.runTransaction { transaction ->
            val postRef = firestore.collection("posts").document(postId)
            val postSnapshot = transaction.get(postRef)

            val currentComments = postSnapshot.getLong("commentsCount") ?: 0
            transaction.delete(firestore.collection("comments").document(commentId))
            transaction.update(postRef, "commentsCount", maxOf(0, currentComments - 1))
        }.await()
    }
}
