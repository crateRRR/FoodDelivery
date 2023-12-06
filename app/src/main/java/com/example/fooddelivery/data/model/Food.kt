package com.example.fooddelivery.data.model

data class Food(
    val id : String = "",
    val category : String = "",
    val ingredients : List<String> = listOf(),
    val name : String = "",
    val price : Double = 0.0,
    val image : String = ""
)

fun List<String>.ingredientsToString() : String {
    var ingredients = ""
    forEach {
        if (it != null) ingredients += "$it, "
    }
    return ingredients.dropLast(2)
}