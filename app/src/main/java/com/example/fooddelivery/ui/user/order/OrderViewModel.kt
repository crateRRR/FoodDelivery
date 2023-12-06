package com.example.fooddelivery.ui.user.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.model.Order
import com.example.fooddelivery.data.repository.CartRepository
import com.example.fooddelivery.data.repository.OrderRepository
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<ScreenState<List<Food>?>>(ScreenState.Loading())
    val cartItems = _cartItems

    private val _order = MutableStateFlow<ScreenState<String>?>(null)
    val order = _order

    init {
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

    fun order(order: Order) = viewModelScope.launch {
        try {
            orderRepository.addOrder(order).let {
                if (it == "Done") {
                    Log.d("OrderViewModel", it)
                    _order.value = ScreenState.Success("Order placed successfully")
                    clearCart()
                } else _order.value = ScreenState.Error("Order failed")
            }
        } catch (e: Exception) {
            Log.d("OrderViewModel", e.message.toString())
            _order.value = ScreenState.Error(e.message.toString())
        }
    }

    private fun clearCart() = viewModelScope.launch {
        try {
            cartRepository.clearCart()
        } catch (e: Exception) {
            _order.value = ScreenState.Error(e.message.toString())
        }
    }

}