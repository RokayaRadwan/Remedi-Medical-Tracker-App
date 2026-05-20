package com.example.remedi.ui.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remedi.R
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.databinding.MedicationListFragmentBinding
import com.example.remedi.viewmodel.MedicationViewModel
import com.example.remedi.viewmodel.MedicationViewModelFactory
import androidx.core.os.bundleOf


class MedicationListFragment : Fragment() {

    private lateinit var medicationViewModel: MedicationViewModel
    private lateinit var medicationAdapter: AllMedicationAdapter

    private var _binding: MedicationListFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentUserId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MedicationListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = getCurrentUserId()

        if (currentUserId == -1L) {
            Toast.makeText(requireContext(), "Please login again", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        val database = AppDatabase.getDatabase(requireContext())
        val repository = MedicationRepository(database.medicationDao(), currentUserId)
        val factory = MedicationViewModelFactory(repository)

        medicationViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[MedicationViewModel::class.java]

        setupRecyclerView()
        observeMedications()
        setupListeners()
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        return sharedPref.getLong("userId", -1L)
    }

    private fun setupRecyclerView() {
        medicationAdapter = AllMedicationAdapter(emptyList()) { medication ->
            findNavController().navigate(
                R.id.action_medicationListFragment_to_addEditMedicationFragment,
                bundleOf("medicationId" to medication.id)
            )
        }

        binding.rvMedications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = medicationAdapter
        }
    }

    private fun observeMedications() {
        medicationViewModel.medications.observe(viewLifecycleOwner) { medications ->
            medicationAdapter.updateList(medications)
        }
    }

    private fun setupListeners() {
        binding.fabAddMedication.setOnClickListener {
            findNavController().navigate(
                R.id.action_medicationListFragment_to_addEditMedicationFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}