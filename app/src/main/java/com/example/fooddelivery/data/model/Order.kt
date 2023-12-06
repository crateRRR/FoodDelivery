package com.example.fooddelivery.data.model

data class Order (
    val id : String = "",
    val userId : String = "",
    val foodList : List<String> = emptyList(),
    val totalPrice : Double = 0.0,
    val date : String = "",
    val paymentMethod : String = "",
    val address : String = "",
    val phoneNum : String = "",
    val comments : String = ""
)