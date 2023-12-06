package com.example.fooddelivery.data.repository

import com.example.fooddelivery.data.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FoodRepository @Inject constructor(
    firestore: FirebaseFirestore
) {

    private val foodCollection =
        firestore.collection("food")

    suspend fun getFoodList(category: String): List<Food>? {
        val foodList = mutableListOf<Food>()
        return try {
            val querySnapshot = if (category == "all") {
                foodCollection.get().await()
            } else {
                foodCollection.whereEqualTo("category", category).get().await()
            }

            for (document in querySnapshot.documents) {
                val food = document.toObject(Food::class.java)
                food?.let {
                    foodList.add(it)
                }
            }
            foodList
        } catch (e: Exception) {
            null
        }
    }


    suspend fun getFoodById(foodId: String): Food? {
        var food: Food? = null
        return try {
            val documentSnapshot = foodCollection.document(foodId).get().await()
            if (documentSnapshot.exists()) {
                food = documentSnapshot.toObject(Food::class.java)
            }
            food
        } catch (e: Exception) {
            null
        }
    }

}