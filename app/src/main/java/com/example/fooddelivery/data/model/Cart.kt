package com.example.fooddelivery.data.model

data class Cart(
    val id : String = "",
    val userId : String = "",
    val foodList : List<Food> = emptyList()
)