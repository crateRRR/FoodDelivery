package com.example.fooddelivery.ui.user.food_details

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.data.model.ingredientsToString
import com.example.fooddelivery.databinding.FragmentFoodDetailsBinding
import com.example.fooddelivery.util.Constants
import com.ivkorshak.el_diaries.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodDetailsFragment : DialogFragment() {
    private var _binding: FragmentFoodDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodDetailsViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentFoodDetailsBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this.activity)
        builder.run { setView(binding.root) }
        val foodId = arguments?.getString(Constants.FOOD_ID) ?: ""
        Log.d("foodId", foodId)
        getDetails(foodId)
        return builder.create()
    }

    private fun addToCart(food: Food) {
        lifecycleScope.launch {
            viewModel.addToCart(food)
            viewModel.addedToCart.collect {
                if (it == "Done") Toast.makeText(
                    requireContext(),
                    "Added to cart",
                    Toast.LENGTH_SHORT
                ).show()
                else Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDetails(foodId: String) {
        lifecycleScope.launch {
            viewModel.getFoodDetails(foodId)
            viewModel.foodDetails.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> Toast.makeText(
                        context,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ScreenState.Success -> {
                        if (state.data != null) {
                            displayDetails(state.data)
                            binding.buttonAddToCart.setOnClickListener { addToCart(state.data) }
                        }
                    }

                }
            }
        }
    }

    private fun displayDetails(food: Food) = with(binding) {
        Glide.with(root).load(food.image)
            .error(R.drawable.app_logo)
            .into(imageviewDetailsImage)
        textViewDetailsName.text = food.name
        textViewDetailsIngredients.text = food.ingredients.ingredientsToString()
        val priceString = String.format("%.2f", food.price)
        val priceTag = "$$priceString"
        textViewDetailsPrice.text = priceTag
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}