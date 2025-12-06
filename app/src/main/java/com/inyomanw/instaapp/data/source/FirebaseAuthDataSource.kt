package com.inyomanw.instaapp.data.source

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.inyomanw.instaapp.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerWithEmail(email: String, password: String, displayName: String): User {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("User creation failed")

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        firebaseUser.updateProfile(profileUpdates).await()

        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = displayName,
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }

    suspend fun loginWithEmail(email: String, password: String): User {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Login failed")

        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }

    suspend fun signInWithGoogleCredential(credential: AuthCredential): User {
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: throw Exception("Google sign-in failed")

        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }

    suspend fun saveUserToFirestore(user: User): Boolean {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return User(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            displayName = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }

    fun logout() {
        auth.signOut()
    }
}