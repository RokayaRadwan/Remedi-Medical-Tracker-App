package com.example.remedi.ui.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.databinding.TodayMedicationsFragmentBinding
import com.example.remedi.viewmodel.MedicationViewModel
import com.example.remedi.viewmodel.MedicationViewModelFactory
import java.util.Calendar

class TodayMedicationsFragment : Fragment() {

    private lateinit var medicationViewModel: MedicationViewModel
    private lateinit var medicationAdapter: MedicationAdapter

    private var _binding: TodayMedicationsFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentUserId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodayMedicationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = getCurrentUserId()

        if (currentUserId == -1L) {
            Toast.makeText(requireContext(), "Please login again", Toast.LENGTH_SHORT).show()
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
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        return sharedPref.getLong("userId", -1L)
    }

    private fun getTodayName(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY -> "Sat"
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            else -> ""
        }
    }

    private fun setupRecyclerView() {
        medicationAdapter = MedicationAdapter(emptyList())

        binding.rvTodayMedications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = medicationAdapter
        }
    }

    private fun observeMedications() {
        val today = getTodayName()

        // DEBUG: shows which day we're filtering by
        Toast.makeText(requireContext(), "Showing meds for: $today", Toast.LENGTH_SHORT).show()

        medicationViewModel.medications.observe(viewLifecycleOwner) { medications ->
            val todayMedications = medications.filter { medication ->
                medication.daysOfWeek.split(",").contains(today)
            }

            // DEBUG: shows total vs filtered count
            binding.tvEmptyTodayMedications.text =
                if (todayMedications.isEmpty())
                    "No medications for $today\n(Total in DB: ${medications.size})"
                else
                    "" // clear it when there are results

            medicationAdapter.updateList(todayMedications)

            binding.tvEmptyTodayMedications.visibility =
                if (todayMedications.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}