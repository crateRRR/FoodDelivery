package com.example.fooddelivery.ui.user.food_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.repository.CartRepository
import com.example.fooddelivery.data.repository.FoodRepository
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _foodDetails = MutableStateFlow<ScreenState<Food?>>(ScreenState.Loading())
    val foodDetails = _foodDetails

    private val _addedToCart = MutableStateFlow("")
    val addedToCart = _addedToCart

    fun getFoodDetails(foodId: String) = viewModelScope.launch {

        try {
            foodRepository.getFoodById(foodId).let {
                if (it != null) _foodDetails.value = ScreenState.Success(it)
                else _foodDetails.value = ScreenState.Error("Food not found")
            }
        } catch (e: Exception) {
            _foodDetails.value = ScreenState.Error(e.message.toString())
        }

    }

    fun addToCart(food: Food) = viewModelScope.launch {
        try {
            cartRepository.addFoodToCart(food).let {
                _addedToCart.value = it
            }
        } catch (e: Exception) {
            _addedToCart.value = e.message.toString()
        }
    }

}