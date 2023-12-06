package com.example.fooddelivery.data.repository

import com.example.fooddelivery.data.model.FoodCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FoodCategoryRepository @Inject constructor(
    firestore: FirebaseFirestore
) {

    private val categoryCollection =
        firestore.collection("categories")

    suspend fun getCategoryList(): List<FoodCategory>? {
        val categoryList = mutableListOf<FoodCategory>()
        return try {
            val querySnapshot = categoryCollection.get().await()
            for (document in querySnapshot.documents) {
                val category = document.toObject(FoodCategory::class.java)
                category?.let {
                    categoryList.add(it)
                }
            }
            categoryList
        } catch (e: Exception) {
            null
        }
    }

}