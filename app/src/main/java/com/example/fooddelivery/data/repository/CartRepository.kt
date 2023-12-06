package com.example.fooddelivery.data.repository

import android.util.Log
import com.example.fooddelivery.data.model.Cart
import com.example.fooddelivery.data.model.Food
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val cartCollection = firestore.collection("cart")
    private val userId = firebaseAuth.currentUser?.uid ?: ""

    suspend fun createUsersCart(uid: String) {
        try {
            val cartRef = cartCollection.document(uid)
            cartRef.set(Cart(uid, uid, emptyList())).await()
        } catch (e: FirebaseException) {
            Log.d("CartRepository", "Error creating cart: $e")
        }
    }

    suspend fun getUsersCart(): List<Food> {
        val userId = firebaseAuth.currentUser?.uid ?: ""

        return try {
            val cartRef = cartCollection.document(userId)
            val cartDocument = cartRef.get().await()

            if (cartDocument.exists()) {
                val cartData = cartDocument.toObject(Cart::class.java)
                cartData?.foodList ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.d("CartRepository", "Error getting food list from cart: $e")
            emptyList()
        }
    }

    suspend fun addFoodToCart(food: Food): String {
        return try {
            val cartRef = cartCollection.document(userId)

            // Get the current list of food
            val currentHomeworks =
                cartRef.get().await().toObject(Cart::class.java)?.foodList ?: emptyList()

            // Update the list of food
            val updatedFoodIds = currentHomeworks.toMutableList()
            updatedFoodIds.add(food)

            cartRef.update("foodList", updatedFoodIds).await()

            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }

    }

    suspend fun deleteFoodFromCart(foodId: String): String {
        val userId = firebaseAuth.currentUser?.uid ?: ""

        return try {
            val cartRef = cartCollection.document(userId)

            // Fetch the current list of food items
            val cartDocument = cartRef.get().await()
            val cartData = cartDocument.toObject(Cart::class.java)
            val currentFoodList = cartData?.foodList ?: emptyList()

            // Remove the specific food item by ID
            val updatedFoodList = currentFoodList.filter { it.id != foodId }

            // Update the cart document with the modified food list
            cartRef.update("foodList", updatedFoodList).await()

            "Food deleted from cart successfully"
        } catch (e: Exception) {
            Log.d("CartRepository", "Error deleting food from cart: $e")
            "Failed to delete food from cart"
        }
    }

    suspend fun clearCart(): String {
        val userId = firebaseAuth.currentUser?.uid ?: ""

        return try {
            val cartRef = cartCollection.document(userId)

            cartRef.update("foodList", FieldValue.delete()).await()

            "Done"
        } catch (e: Exception) {
            Log.d("CartRepository", "Error deleting all items from cart: $e")
            "Failed to delete all items from cart"
        }
    }


}