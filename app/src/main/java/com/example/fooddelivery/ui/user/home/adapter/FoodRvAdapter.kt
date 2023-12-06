package com.example.fooddelivery.ui.user.home.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.databinding.FoodItemBinding

class FoodRvAdapter(
    private var foodList: List<Food>,
    private val cartListener: (food: Food) -> Unit,
    private val itemListener: (food: Food) -> Unit
) : RecyclerView.Adapter<FoodRvAdapter.FoodViewHolder>() {

    class FoodViewHolder(
        cartListener: (food: Food) -> Unit,
        itemListener: (food: Food) -> Unit,
        private val binding: FoodItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var food: Food? = null
        fun bind(curFood: Food) {
            Log.d("FoodItem", curFood.toString())
            binding.textViewOrderFoodName.text = curFood.name
            val priceString = String.format("%.2f", curFood.price)
            val priceTag =  "$priceString$"
            binding.textViewOrderFoodPrice.text = priceTag
            Glide.with(binding.root).load(curFood.image)
                .error(R.drawable.app_logo)
                .into(binding.imageViewOrderFood)
            food = curFood
        }

        init {
            binding.buttonAddToCart.setOnClickListener { cartListener(food!!) }
            binding.root.setOnClickListener { itemListener(food!!) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            cartListener,
            itemListener,
            FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFoodList(newFoodList: List<Food>) {
        foodList = newFoodList.toMutableList()
        notifyDataSetChanged()
    }


}