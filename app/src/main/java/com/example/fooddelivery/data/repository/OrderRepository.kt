package com.example.fooddelivery.data.repository

import android.util.Log
import com.example.fooddelivery.data.model.Order
import com.example.fooddelivery.util.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val ordersCollection = firestore.collection("orders")

    suspend fun addOrder(order: Order): String {
        Log.d("OrderViewModel", order.toString())
        return try {
            val id = Constants.generateRandomId()
            val orderRef = ordersCollection.document(id)
            orderRef.set(order).await()
            "Done"
        } catch (e: FirebaseException) {
            Log.d("OrderViewModel", e.message.toString())
            e.message.toString()
        }
    }

    suspend fun getCurrentUserOrders(): List<Order> {
        val userId = firebaseAuth.currentUser?.uid ?: ""

        return try {
            val querySnapshot = ordersCollection.whereEqualTo("userId", userId).get().await()
            val orders = mutableListOf<Order>()

            for (document in querySnapshot.documents) {
                Log.d("OrderViewModel", document.toObject(Order::class.java).toString())
                val order = document.toObject(Order::class.java)
                order?.let { orders.add(it) }
            }
            Log.d("OrderViewModel", orders.toString())
            orders
        } catch (e: Exception) {
            Log.d("OrderViewModel", e.message.toString())
            emptyList()
        }
    }

}