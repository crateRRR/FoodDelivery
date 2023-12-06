package com.example.fooddelivery.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fooddelivery.ui.user.MainActivity
import com.example.fooddelivery.databinding.FragmentLoginBinding
import com.example.fooddelivery.util.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    @Inject lateinit var authManager: AuthManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonLogin.setOnClickListener {
            if (allFieldsAreFilled()) login()
            else Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun login() {
        binding.buttonLogin.isClickable = false
        lifecycleScope.launch {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.login(email, password)
            viewModel.loggedIn.collect { result ->
                if (result == "Done") {
                    authManager.saveUer(email)
                    navToMainActivity()
                }
                else {
                    Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
                    binding.buttonLogin.isClickable = true
                }
            }
        }
    }

    private fun navToMainActivity() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun allFieldsAreFilled(): Boolean {
        return !(binding.editTextEmail.text.toString()
            .isEmpty() || binding.editTextPassword.text.toString().isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}