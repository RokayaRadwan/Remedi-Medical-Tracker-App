package com.example.remedi.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.remedi.R
import com.example.remedi.databinding.WelcomeFragmentBinding

class WelcomeFragment : Fragment() {

    private var _binding: WelcomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = WelcomeFragmentBinding.inflate(inflater, container, false)

        binding.continueButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_welcomeFragment_to_loginFragment
            )

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}