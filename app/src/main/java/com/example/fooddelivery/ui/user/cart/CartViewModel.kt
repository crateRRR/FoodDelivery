package com.example.fooddelivery.ui.user.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.repository.CartRepository
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<ScreenState<List<Food>?>>(ScreenState.Loading())
    val cartItems = _cartItems

    private val _deleteItem = MutableStateFlow("")
    val deleteItem = _deleteItem

    init {
        getCartItems()
    }

    fun refresh() {
        getCartItems()
    }

    private fun getCartItems() = viewModelScope.launch {
        try {
            cartRepository.getUsersCart().let { cartItems ->
                if (cartItems.isNotEmpty()) _cartItems.value = ScreenState.Success(cartItems)
                else _cartItems.value = ScreenState.Error("Cart is empty")
            }
        } catch (e: Exception) {
            _cartItems.value = ScreenState.Error(e.message.toString())
        }
    }


    fun deleteItem(foodId: String) = viewModelScope.launch {
        try {
            cartRepository.deleteFoodFromCart(foodId).let {
                if (it == "Done") getCartItems()
                else  _deleteItem.value = it
            }

        } catch (e: Exception) {
            _deleteItem.value = e.message.toString()
            Log.d("CartFragment", e.message.toString())
        }
    }

}