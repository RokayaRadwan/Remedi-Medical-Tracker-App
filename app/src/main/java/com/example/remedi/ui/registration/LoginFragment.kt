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
import com.example.remedi.databinding.LoginFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.registerText.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

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

            val user = withContext(Dispatchers.IO) {
                database.userDao().loginUser(email, password)
            }

            if (user != null) {
                val sharedPref = requireContext().getSharedPreferences(
                    "user_session",
                    0
                )

                sharedPref.edit()
                    .putLong("userId", user.id)
                    .putString("fullName", user.fullName)
                    .putString("email", user.email)
                    .apply()

                Toast.makeText(
                    requireContext(),
                    "Login Successful",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    R.id.action_loginFragment_to_dashboardFragment
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Invalid Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}