package com.example.fooddelivery.ui.user.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.model.Order
import com.example.fooddelivery.databinding.FragmentOrderBinding
import com.example.fooddelivery.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun allFieldsAreFilled(): Boolean {
        return !(binding.radioButtonCreditCard.isChecked &&
                (binding.editTextCardNumber.text.isNotEmpty() &&
                        binding.editTextExpiryDate.text.isNotEmpty() &&
                        binding.editTextCVV.text.isNotEmpty()) || binding.editTextOrderAddress.text.toString()
            .isEmpty() ||
                binding.editTextOrderPhoneNum.text.toString().isEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getOrderDetails()
        binding.buttonFinish.setOnClickListener {
            if (allFieldsAreFilled()) submitOrder()
            else Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getOrderDetails() {
        lifecycleScope.launch {
            viewModel.cartItems.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    is ScreenState.Success -> {

                        if (state.data.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                        } else displayOrderDetails(state.data)

                    }
                }
            }
        }
    }

    private fun displayOrderDetails(foodList: List<Food>) {
        val foodNames = foodList.joinToString { it.name }
        val totalPrice = foodList.sumOf { it.price }
        val priceString = String.format("%.2f", totalPrice)
        val priceTag = "$priceString$"
        binding.textViewOrderDetails.text = foodNames
        binding.textViewOrderTotalPrice.text = priceTag
        binding.textViewOrderDate.text = Constants.getCurrentDate()
    }

    private fun submitOrder() {
        val foodList = binding.textViewOrderDetails.text.toString().split(",")
        val totalPrice = binding.textViewOrderTotalPrice.text.toString().replace("$", "").toDouble()
        val orderDate = binding.textViewOrderDate.text.toString()
        val address = binding.editTextOrderAddress.text.toString()
        val paymentMethod = if (binding.radioButtonCreditCard.isChecked) "Credit Card" else "Cash"
        val phoneNum = binding.editTextOrderPhoneNum.text.toString()
        val userId = firebaseAuth.currentUser?.uid ?: ""
        var comment = binding.editTextOrderComment.text.toString()
        if (comment.isEmpty()) comment = ""
        val order = Order(
            id = Constants.generateRandomId(),
            userId = userId,
            foodList = foodList,
            totalPrice = totalPrice,
            date = orderDate,
            address = address,
            paymentMethod = paymentMethod,
            phoneNum = phoneNum,
            comments = comment
        )

        lifecycleScope.launch {
            viewModel.order(order)
            viewModel.order.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    is ScreenState.Success -> {
                        Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                    else -> {}
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}