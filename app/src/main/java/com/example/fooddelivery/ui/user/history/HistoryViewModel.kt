package com.example.fooddelivery.ui.user.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelivery.data.model.Order
import com.example.fooddelivery.data.repository.OrderRepository
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<ScreenState<List<Order>?>>(ScreenState.Loading())
    val orders = _orders


    init {
        getOrders()
    }

    private fun getOrders() = viewModelScope.launch {
        try {
            orderRepository.getCurrentUserOrders().let {
                if (it.isNotEmpty()) {
                    _orders.value = ScreenState.Success(it)
                } else _orders.value = ScreenState.Error("No orders yet!")
            }
        } catch (e: Exception) {
            _orders.value = ScreenState.Error(e.message.toString())
        }
    }

    fun refresh() {
        getOrders()
    }


}