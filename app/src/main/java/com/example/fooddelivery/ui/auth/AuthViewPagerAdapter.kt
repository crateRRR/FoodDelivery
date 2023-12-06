package com.example.fooddelivery.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fooddelivery.ui.auth.login.LoginFragment
import com.example.fooddelivery.ui.auth.signup.SignupFragment

class AuthViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> SignupFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


}