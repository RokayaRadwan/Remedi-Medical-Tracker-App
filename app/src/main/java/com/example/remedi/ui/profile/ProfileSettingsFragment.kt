package com.example.remedi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.remedi.R
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.repository.UserProfileRepository
import com.example.remedi.databinding.ProfileSettingsFragmentBinding
import com.example.remedi.viewmodel.ProfileViewModel
import com.example.remedi.viewmodel.ProfileViewModelFactory

class ProfileSettingsFragment : Fragment() {

    private var _binding: ProfileSettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    private var isLoadingProfile = false

    private var sessionFullName: String = "User"
    private var sessionEmail: String = "No email saved"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserSession()
        setupViewModel()
        observeProfile()
        setupSwitchListeners()
        setupClickListeners()
    }

    private fun loadUserSession() {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)

        sessionFullName = sharedPref.getString("fullName", "User") ?: "User"
        sessionEmail = sharedPref.getString("email", "No email saved") ?: "No email saved"

        binding.txtUserName.text = sessionFullName
        binding.txtEmail.text = sessionEmail
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = UserProfileRepository(database.userProfileDao())
        val factory = ProfileViewModelFactory(repository)

        profileViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[ProfileViewModel::class.java]
    }

    private fun observeProfile() {
        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            isLoadingProfile = true

            if (profile != null) {
                binding.txtUserName.text = sessionFullName
                binding.txtEmail.text = sessionEmail

                binding.txtEmergencyContact.text =
                    profile.emergencyContact.ifBlank { "No emergency contact saved" }

                binding.switchNotifications.isChecked = profile.notificationsEnabled
                binding.switchDarkMode.isChecked = profile.darkModeEnabled
            } else {
                binding.txtUserName.text = sessionFullName
                binding.txtEmail.text = sessionEmail
                binding.txtEmergencyContact.text = "No emergency contact saved"

                profileViewModel.saveProfile(
                    fullName = sessionFullName,
                    email = sessionEmail,
                    phoneNumber = "",
                    age = "",
                    gender = "",
                    emergencyContact = "No emergency contact saved",
                    notificationsEnabled = true,
                    darkModeEnabled = false
                )
            }

            isLoadingProfile = false
        }
    }

    private fun setupSwitchListeners() {
        binding.switchNotifications.setOnCheckedChangeListener { _, _ ->
            if (!isLoadingProfile) {
                saveCurrentProfile()
            }
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, _ ->
            if (!isLoadingProfile) {
                saveCurrentProfile()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("user_session", 0)
            sharedPref.edit().clear().apply()

            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

            findNavController().navigate(
                R.id.action_profileSettingsFragment_to_loginFragment
            )
        }
    }

    private fun saveCurrentProfile() {
        profileViewModel.saveProfile(
            fullName = sessionFullName,
            email = sessionEmail,
            phoneNumber = "",
            age = "",
            gender = "",
            emergencyContact = binding.txtEmergencyContact.text.toString(),
            notificationsEnabled = binding.switchNotifications.isChecked,
            darkModeEnabled = binding.switchDarkMode.isChecked
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}