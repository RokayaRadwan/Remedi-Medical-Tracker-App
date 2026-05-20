package com.example.remedi.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.remedi.R
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.local.entity.UserEntity
import com.example.remedi.databinding.RegisterFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            registerUser()
        }

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun registerUser() {
        val fullName = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.nameEditText.error = "Full name is required"
            return
        }

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            return
        }

        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(requireContext())

            val existingUser = withContext(Dispatchers.IO) {
                database.userDao().getUserByEmail(email)
            }

            if (existingUser != null) {
                Toast.makeText(
                    requireContext(),
                    "Email already registered",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            val user = UserEntity(
                fullName = fullName,
                email = email,
                password = password
            )

            withContext(Dispatchers.IO) {
                database.userDao().registerUser(user)
            }

            Toast.makeText(
                requireContext(),
                "Registered successfully. Please login.",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}