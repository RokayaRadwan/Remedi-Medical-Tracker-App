package com.example.remedi.ui.reminderschedule

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.data.local.entity.ReminderEntity
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.data.repository.ReminderRepository
import com.example.remedi.databinding.ReminderScheduleFragmentBinding
import com.example.remedi.viewmodel.MedicationViewModel
import com.example.remedi.viewmodel.MedicationViewModelFactory
import com.example.remedi.viewmodel.ReminderViewModel
import com.example.remedi.viewmodel.ReminderViewModelFactory
import java.util.Calendar
import java.util.Locale

class ReminderScheduleFragment : Fragment() {

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var medicationViewModel: MedicationViewModel

    private var _binding: ReminderScheduleFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var reminderAdapter: ReminderScheduleAdapter

    private var selectedTime: String = ""
    private var availableMedications: List<MedicationEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReminderScheduleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModels()
        setupRepeatSpinner()
        setupMedicationSpinner(emptyList())
        setupRecyclerView()
        setupClickListeners()
        observeData()
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        return sharedPref.getLong("userId", -1L)
    }

    private fun setupViewModels() {
        val database = AppDatabase.getDatabase(requireContext())

        val reminderRepository = ReminderRepository(database.reminderDao())
        val doseHistoryRepository = DoseHistoryRepository(database.doseHistoryDao())

        val reminderFactory = ReminderViewModelFactory(
            reminderRepository,
            doseHistoryRepository
        )

        reminderViewModel = ViewModelProvider(
            requireActivity(),
            reminderFactory
        )[ReminderViewModel::class.java]

        val currentUserId = getCurrentUserId()

        if (currentUserId == -1L) {
            showToast("Please login again")
            return
        }

        val medicationRepository = MedicationRepository(database.medicationDao(), currentUserId)
        val medicationFactory = MedicationViewModelFactory(medicationRepository)

        medicationViewModel = ViewModelProvider(
            requireActivity(),
            medicationFactory
        )[MedicationViewModel::class.java]
    }

    private fun observeData() {
        reminderViewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.updateList(reminders)
            updateUi(reminders)
        }

        medicationViewModel.medications.observe(viewLifecycleOwner) { medications ->
            availableMedications = medications
            setupMedicationSpinner(medications)
        }
    }

    private fun setupMedicationSpinner(medications: List<MedicationEntity>) {
        val medicationNames = if (medications.isEmpty()) {
            listOf("No medications available")
        } else {
            medications.map { medication ->
                "${medication.name} - ${medication.dosage}"
            }
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            medicationNames
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMedication.adapter = adapter
    }

    private fun setupRepeatSpinner() {
        val repeatOptions = listOf("Once", "Daily", "Twice a day", "Weekly")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            repeatOptions
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRepeat.adapter = adapter
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderScheduleAdapter(emptyList())

        binding.rvReminderSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reminderAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnPickTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSaveReminder.setOnClickListener {
            saveReminder()
        }

        binding.btnClearReminders.setOnClickListener {
            reminderViewModel.clearAllReminders()
            showToast("All reminders cleared")
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    selectedHour,
                    selectedMinute
                )

                binding.btnPickTime.text = "Selected Time: $selectedTime"
            },
            hour,
            minute,
            true
        ).show()
    }

    private fun saveReminder() {
        val doseAmount = binding.etDoseAmount.text.toString().trim()
        val repeat = binding.spinnerRepeat.selectedItem.toString()
        val isEnabled = binding.switchReminderEnabled.isChecked

        if (availableMedications.isEmpty()) {
            showToast("Please add a medication first before creating a reminder")
            return
        }

        if (doseAmount.isEmpty()) {
            binding.etDoseAmount.error = "Enter dose amount"
            return
        }

        if (selectedTime.isEmpty()) {
            showToast("Please pick reminder time")
            return
        }

        val selectedMedicationIndex = binding.spinnerMedication.selectedItemPosition

        if (selectedMedicationIndex < 0 || selectedMedicationIndex >= availableMedications.size) {
            showToast("Please select a valid medication")
            return
        }

        val selectedMedication = availableMedications[selectedMedicationIndex]

        val reminder = ReminderEntity(
            medicationId = selectedMedication.id,
            medicineName = selectedMedication.name,
            doseAmount = doseAmount,
            time = selectedTime,
            repeatType = repeat,
            isEnabled = isEnabled,
            status = if (isEnabled) "Scheduled" else "Disabled"
        )

        reminderViewModel.addReminder(reminder)

        clearInputFields()
        showToast("Reminder saved")
    }

    private fun clearInputFields() {
        binding.etDoseAmount.text.clear()
        binding.spinnerRepeat.setSelection(0)
        binding.switchReminderEnabled.isChecked = true
        binding.btnPickTime.text = "Pick Reminder Time"
        selectedTime = ""

        if (availableMedications.isNotEmpty()) {
            binding.spinnerMedication.setSelection(0)
        }
    }

    private fun updateUi(reminders: List<ReminderEntity>) {
        if (reminders.isEmpty()) {
            binding.rvReminderSchedule.visibility = View.GONE
            binding.tvEmptyReminders.visibility = View.VISIBLE
            binding.btnClearReminders.isEnabled = false
            binding.btnClearReminders.alpha = 0.5f
        } else {
            binding.rvReminderSchedule.visibility = View.VISIBLE
            binding.tvEmptyReminders.visibility = View.GONE
            binding.btnClearReminders.isEnabled = true
            binding.btnClearReminders.alpha = 1f
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}