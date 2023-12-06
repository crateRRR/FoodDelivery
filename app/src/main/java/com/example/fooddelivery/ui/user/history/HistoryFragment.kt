package com.example.fooddelivery.ui.user.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.data.model.Order
import com.example.fooddelivery.databinding.FragmentHistoryBinding
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()
    private var rvOrderAdapter: OrdersRvAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getOrdersList()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getOrdersList() {
        lifecycleScope.launch {
            viewModel.orders.collect { state ->
                Log.d("HistoryFragment", state.message.toString())
                Log.d("HistoryFragment", state.data.toString())
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        handleError(state.message!!)
                    }

                    is ScreenState.Success -> {
                        binding.textViewError.visibility = View.GONE
                        binding.rvOrders.visibility = View.VISIBLE
                        if (state.data.isNullOrEmpty()) {
                            handleError("No orders yet")
                        } else displayHistoryList(state.data)

                    }
                }
            }
        }
    }

    private fun displayHistoryList(orders: List<Order>) {
        Log.d("HistoryFragment", orders.toString())
        rvOrderAdapter = OrdersRvAdapter(orders)
        binding.rvOrders.setHasFixedSize(true)
        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.adapter = rvOrderAdapter
    }

    private fun handleError(message: String) {
        binding.rvOrders.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        rvOrderAdapter = null
    }

}
