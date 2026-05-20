package com.example.remedi.ui.dashboard

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
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.data.repository.ReminderRepository
import com.example.remedi.databinding.DashboardFragmentBinding
import com.example.remedi.ui.medication.MedicationAdapter
import com.example.remedi.ui.medication.Vitamin
import com.example.remedi.ui.medication.VitaminAdapter
import com.example.remedi.viewmodel.DashboardViewModel
import com.example.remedi.viewmodel.DashboardViewModelFactory
import com.example.remedi.viewmodel.MedicationViewModel
import com.example.remedi.viewmodel.MedicationViewModelFactory

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var medicationViewModel: MedicationViewModel
    private lateinit var todayMedicationAdapter: MedicationAdapter

    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentUserId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
        ,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getTodayName(): String {
        val calendar = java.util.Calendar.getInstance()

        return when (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            java.util.Calendar.SATURDAY -> "Sat"
            java.util.Calendar.SUNDAY -> "Sun"
            java.util.Calendar.MONDAY -> "Mon"
            java.util.Calendar.TUESDAY -> "Tue"
            java.util.Calendar.WEDNESDAY -> "Wed"
            java.util.Calendar.THURSDAY -> "Thu"
            java.util.Calendar.FRIDAY -> "Fri"
            else -> ""
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = getCurrentUserId()

        if (currentUserId == -1L) {
            Toast.makeText(requireContext(), "Please login again", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        setupViewModel()
        setupUI()
        setupVitaminsRecyclerView()
        setupMedicationsRecyclerView()
        observeDashboard()
        observeTodayMedications()
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        return sharedPref.getLong("userId", -1L)
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())

        val medicationRepository = MedicationRepository(database.medicationDao(), currentUserId)
        val reminderRepository = ReminderRepository(database.reminderDao())
        val doseHistoryRepository = DoseHistoryRepository(database.doseHistoryDao())

        val dashboardFactory = DashboardViewModelFactory(
            medicationRepository,
            reminderRepository,
            doseHistoryRepository
        )

        dashboardViewModel = ViewModelProvider(
            requireActivity(),
            dashboardFactory
        )[DashboardViewModel::class.java]

        val medicationFactory = MedicationViewModelFactory(medicationRepository)

        medicationViewModel = ViewModelProvider(
            requireActivity(),
            medicationFactory
        )[MedicationViewModel::class.java]
    }

    private fun setupUI() {
        binding.tvGreeting.text = "Ya alf welcome,"

        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        val fullName = sharedPref.getString("fullName", "User")
        binding.tvUserName.text = fullName

        binding.btnNotification.setOnClickListener {
            // Handle notification click later
        }

        binding.btnViewAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboardFragment_to_todayMedicationsFragment
            )
        }
    }

    private fun observeDashboard() {
        dashboardViewModel.dashboardSummary.observe(viewLifecycleOwner) { summary ->
            // Dashboard summary is connected to Room.
        }
    }

    private fun setupVitaminsRecyclerView() {
        val vitamins = listOf(
            Vitamin("Vitamin C", "1000mg", android.R.drawable.ic_menu_gallery),
            Vitamin("Vitamin D", "2000IU", android.R.drawable.ic_menu_day),
            Vitamin("Omega 3", "500mg", android.R.drawable.ic_menu_compass),
            Vitamin("Zinc", "50mg", android.R.drawable.ic_menu_agenda)
        )

        binding.rvVitamins.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = VitaminAdapter(vitamins)
        }
    }

    private fun setupMedicationsRecyclerView() {
        todayMedicationAdapter = MedicationAdapter(emptyList())

        binding.rvTodayMedications.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMedicationAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeTodayMedications() {
        medicationViewModel.medications.observe(viewLifecycleOwner) { medications ->
            val today = getTodayName()

            val todayMedications = medications.filter { medication ->
                medication.daysOfWeek.split(",").contains(today)
            }

            todayMedicationAdapter.updateList(todayMedications)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}