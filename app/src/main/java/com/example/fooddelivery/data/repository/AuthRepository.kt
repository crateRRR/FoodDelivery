package com.example.fooddelivery.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {


    suspend fun login(email: String, password: String): String {
        val deferred = CompletableDeferred<String>()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        deferred.complete("Done")
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        deferred.complete(errorMessage)
                    }
                }
        } catch (e: Exception) {
            deferred.complete(e.message ?: "Unknown error")
        }

        return deferred.await()
    }


    suspend fun signup(email: String, password: String): String {
        val deferred = CompletableDeferred<String>()

        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        deferred.complete(task.result.user?.uid ?: "")
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        deferred.complete(errorMessage)
                    }
                }
        } catch (e: Exception) {
            deferred.complete(e.message ?: "Unknown error")
        }

        return deferred.await()
    }

}