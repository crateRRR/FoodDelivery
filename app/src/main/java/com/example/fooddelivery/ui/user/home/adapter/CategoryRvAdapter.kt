package com.example.fooddelivery.ui.user.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelivery.R
import com.example.fooddelivery.data.model.FoodCategory
import com.example.fooddelivery.databinding.CategoryItemBinding

class CategoryRvAdapter(
    private val categoryList: List<FoodCategory>,
    private val listener: (category: FoodCategory) -> Unit
) : RecyclerView.Adapter<CategoryRvAdapter.CategoryViewHolder>() {

    private var lastClickedIndex: Int = -1

    class CategoryViewHolder(
        private var adapter: CategoryRvAdapter,
        listener: (category: FoodCategory) -> Unit,
        private val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var category: FoodCategory? = null
        fun bind(curCategory: FoodCategory, position: Int) {
            binding.textViewFoodCategory.text = curCategory.strCategory
            Log.d("ImageUrl", curCategory.strCategoryThumb)
            Log.d("CategoryName", curCategory.strCategory)
            Glide.with(binding.root).load(curCategory.strCategoryThumb)
                .error(R.drawable.app_logo)
                .into(binding.imageFoodCategory)
            category = curCategory

            if (adapter.lastClickedIndex == position) {
                // Set background for the last clicked item
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.orange
                    )
                )
                binding.textViewFoodCategory.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                // Set the default background for other items
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.gray
                    )
                )
                binding.textViewFoodCategory.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
        }

        init {
            binding.root.setOnClickListener {
                listener(category!!)
                // Update the background for the last clicked item
                if (adapter.lastClickedIndex != -1) {
                    adapter.notifyItemChanged(adapter.lastClickedIndex)
                }

                // Update the background for the currently clicked item
                adapter.lastClickedIndex = adapterPosition
                adapter.notifyItemChanged(adapterPosition)

                // Notify the listener
                adapter.listener(category!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            this,
            listener,
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position], position)
    }

}