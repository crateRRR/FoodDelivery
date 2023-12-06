package com.example.fooddelivery.util

import android.content.Context

class AuthManager(private val context: Context) {

    fun isLoggedIn(): Boolean {
        val authToken = getUser()
        return authToken.isNotEmpty()
    }

    private fun getUser(): String {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.USER_KEY, "") ?: ""
    }

    fun saveUer(email: String) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Constants.USER_KEY, email).apply()
    }

    fun removeUser() {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(Constants.USER_KEY).apply()
    }
}
