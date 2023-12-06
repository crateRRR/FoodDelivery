package com.example.fooddelivery.ui.user.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.databinding.FragmentCartBinding
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private var rvCartRvAdapter: CartRvAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getCartItems()
        binding.buttonPay.setOnClickListener {
            findNavController().navigate(R.id.nav_cart_to_order)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun getCartItems() {
        lifecycleScope.launch {
            viewModel.cartItems.collect { state ->
                Log.d("CartFragment", state.data.toString())
                Log.d("CartFragment", state.message.toString())
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        handleError(state.message!!)
                    }

                    is ScreenState.Success -> {
                        binding.textViewError.visibility = View.GONE
                        binding.rvCartFood.visibility = View.VISIBLE
                        if (state.data.isNullOrEmpty()) {
                            handleError("No items in cart")
                        } else displayCartItems(state.data)

                    }
                }
            }
        }
    }

    private fun removeItemFromCart(foodId: String) {
        lifecycleScope.launch {
            viewModel.deleteItem(foodId)
            viewModel.deleteItem.collect {
                if (it != "Done") Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayCartItems(foodList: List<Food>) {
        Log.d("CartFragment", foodList.toString())
        binding.textViewError.visibility = View.GONE
        binding.buttonPay.visibility = View.VISIBLE
        rvCartRvAdapter = CartRvAdapter(foodList) { removeItemFromCart(it.id) }
        binding.rvCartFood.layoutManager = LinearLayoutManager(context)
        binding.rvCartFood.setHasFixedSize(true)
        binding.rvCartFood.adapter = rvCartRvAdapter
    }

    private fun handleError(message: String) {
        binding.rvCartFood.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        rvCartRvAdapter = null
    }

}