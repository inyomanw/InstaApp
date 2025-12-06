package com.inyomanw.instaapp.data.source

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.inyomanw.instaapp.data.model.Post
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun getAllPosts(limit: Int, lastDocument: DocumentSnapshot?): Pair<List<Post>, DocumentSnapshot?> {
        var query = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())

        lastDocument?.let {
            query = query.startAfter(it)
        }

        val snapshot = query.get().await()
        val posts = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
        val lastVisible = snapshot.documents.lastOrNull()

        return Pair(posts, lastVisible)
    }

    suspend fun createPost(post: Post): String {
        val docRef = firestore.collection("posts").document()
        val newPost = post.copy(postId = docRef.id)
        docRef.set(newPost).await()
        return docRef.id
    }

    suspend fun uploadImage(userId: String, imageBytes: ByteArray): String {
        val imageRef = storage.reference
            .child("posts/$userId/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putBytes(imageBytes).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
}