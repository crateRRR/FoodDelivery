package com.example.fooddelivery.ui.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.model.FoodCategory
import com.example.fooddelivery.data.repository.CartRepository
import com.example.fooddelivery.data.repository.FoodCategoryRepository
import com.example.fooddelivery.data.repository.FoodRepository
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val categoryRepository: FoodCategoryRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _foodList = MutableStateFlow<ScreenState<List<Food>?>>(ScreenState.Loading())
    val foodList = _foodList

    private val _categoryList =
        MutableStateFlow<ScreenState<List<FoodCategory>?>>(ScreenState.Loading())
    val categoryList = _categoryList

    private val _addedToCart = MutableStateFlow("")
    val addedToCart = _addedToCart

    init {
        getCategoriesList()
    }

    fun refreshFoodList(category: String) {
        getFoodList(category)
    }

    fun filterFoodList(query: String): List<Food> {
        return when (val currentState = foodList.value) {
            is ScreenState.Success -> {
                currentState.data?.filter { food ->
                    food.name.contains(query, ignoreCase = true)
                } ?: emptyList()
            }

            else -> emptyList()
        }
    }


    private fun getCategoriesList() = viewModelScope.launch {
        try {
            categoryRepository.getCategoryList().let {
                if (it.isNullOrEmpty())
                    _categoryList.value = ScreenState.Error("No Category Found")
                else
                    _categoryList.value = ScreenState.Success(it)
            }
        } catch (e: Exception) {
            _categoryList.value = ScreenState.Error(e.message.toString())
        }
    }

    fun getFoodList(category: String) = viewModelScope.launch {
        try {
            foodRepository.getFoodList(category).let {
                if (it.isNullOrEmpty())
                    _foodList.value = ScreenState.Error("No Food Found")
                else
                    _foodList.value = ScreenState.Success(it)
            }
        } catch (e: Exception) {
            _foodList.value = ScreenState.Error(e.message.toString())
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