package com.example.fooddelivery.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.ActivityMainBinding
import com.example.fooddelivery.ui.auth.AuthActivity
import com.example.fooddelivery.util.AuthManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (firebaseAuth.currentUser?.uid == null) logout()

        if (!authManager.isLoggedIn()) {
            logout()
        }
        setBottomNavBar()
    }


    private fun setBottomNavBar() {
        val navController = findNavController(R.id.nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userHomeFragment,
                R.id.cartFragment,
                R.id.historyFragment,
                R.id.profileFragment
            )
        )

        val topLevelDestinations = setOf(
            R.id.userHomeFragment,
            R.id.cartFragment,
            R.id.historyFragment,
            R.id.profileFragment
        )
        // Show the bottom navigation view for top-level destinations only
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in topLevelDestinations) {
                binding.usersNavView.visibility = View.VISIBLE
            } else {
                binding.usersNavView.visibility = View.GONE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.usersNavView.setupWithNavController(navController)
    }

    private fun logout() {
        startActivity(Intent(this, AuthActivity::class.java))
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}