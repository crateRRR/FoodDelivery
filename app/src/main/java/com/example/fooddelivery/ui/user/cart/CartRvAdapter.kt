package com.example.fooddelivery.ui.user.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.Food
import com.example.fooddelivery.databinding.CartItemBinding

class CartRvAdapter(
    private val cartList: List<Food>,
    private val removeListener: (food: Food) -> Unit
) : RecyclerView.Adapter<CartRvAdapter.CartItemViewHolder>() {

    class CartItemViewHolder(
        removeListener: (food: Food) -> Unit,
        private val binding: CartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var food: Food? = null
        fun bind(curFood: Food) {
            binding.textViewCartFoodName.text = curFood.name
            val priceString = String.format("%.2f", curFood.price)
            val priceTag = "$priceString$"
            binding.textViewCartFoodPrice.text = priceTag
            Glide.with(binding.root).load(curFood.image)
                .error(R.drawable.app_logo)
                .into(binding.imageViewCartFoodImg)
            food = curFood
        }


        init {
            binding.buttonDeleteFromCart.setOnClickListener { removeListener(food!!) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            removeListener,
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

}