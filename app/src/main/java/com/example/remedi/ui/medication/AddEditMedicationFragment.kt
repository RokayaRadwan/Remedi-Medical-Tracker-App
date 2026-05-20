package com.example.remedi.ui.medication

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.local.entity.MedicationEntity
import com.example.remedi.data.repository.MedicationRepository
import com.example.remedi.databinding.AddEditMedicationFragmentBinding
import com.example.remedi.viewmodel.MedicationViewModel
import com.example.remedi.viewmodel.MedicationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddEditMedicationFragment : Fragment() {

    private lateinit var medicationViewModel: MedicationViewModel
    private var _binding: AddEditMedicationFragmentBinding? = null
    private val binding get() = _binding!!

    private var currentUserId: Long = -1L
    private var medicationId: Long = -1L
    private var existingMedication: MedicationEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEditMedicationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserId = getCurrentUserId()
        medicationId = arguments?.getLong("medicationId", -1L) ?: -1L

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

        setupToolbar()
        setupTimePicker()
        setupSaveButton()

        if (medicationId != -1L) {
            loadMedicationForEdit()
        }
    }

    private fun getCurrentUserId(): Long {
        val sharedPref = requireContext().getSharedPreferences("user_session", 0)
        return sharedPref.getLong("userId", -1L)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val amPm = if (selectedHour < 12) "AM" else "PM"
                val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val timeString = String.format("%02d:%02d %s", hourIn12Format, selectedMinute, amPm)
                binding.etTime.setText(timeString)
            }, hour, minute, false).show()
        }
    }

    private fun loadMedicationForEdit() {
        lifecycleScope.launch {
            val medication = withContext(Dispatchers.IO) {
                medicationViewModel.getMedicationById(medicationId)
            }

            if (medication != null) {
                existingMedication = medication

                binding.etName.setText(medication.name)
                binding.etDosage.setText(medication.dosage)
                binding.etTime.setText(medication.time)
                binding.etInstruction.setText(medication.instruction)
                binding.etRefills.setText(medication.refillsLeft.toString())

                setSelectedDays(medication.daysOfWeek)

                binding.btnSave.text = "Update Medication"
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val dosage = binding.etDosage.text.toString().trim()
            val time = binding.etTime.text.toString().trim()
            val instruction = binding.etInstruction.text.toString().trim()
            val refillsText = binding.etRefills.text.toString().trim()
            val daysOfWeek = getSelectedDays()

            if (name.isBlank()) {
                binding.etName.error = "Name is required"
                return@setOnClickListener
            }

            if (dosage.isBlank()) {
                binding.etDosage.error = "Dosage is required"
                return@setOnClickListener
            }

            if (time.isBlank()) {
                binding.etTime.error = "Time is required"
                return@setOnClickListener
            }

            if (daysOfWeek.isBlank()) {
                Toast.makeText(requireContext(), "Select at least one day", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedColor = when (binding.rgColors.checkedRadioButtonId) {
                binding.rbOrange.id -> 0xFFFF7043.toInt()
                binding.rbDarkGreen.id -> 0xFF00695C.toInt()
                else -> 0xFF2DBE7F.toInt()
            }

            val medication = MedicationEntity(
                id = if (medicationId == -1L) 0 else medicationId,
                userId = currentUserId,
                name = name,
                dosage = dosage,
                time = time,
                instruction = instruction,
                refillsLeft = refillsText.toIntOrNull() ?: 0,
                color = selectedColor,
                daysOfWeek = daysOfWeek,
                createdAt = existingMedication?.createdAt ?: System.currentTimeMillis()
            )

            if (medicationId == -1L) {
                medicationViewModel.addMedication(medication)
                Toast.makeText(requireContext(), "Medication saved", Toast.LENGTH_SHORT).show()
            } else {
                medicationViewModel.updateMedication(medication)
                Toast.makeText(requireContext(), "Medication updated", Toast.LENGTH_SHORT).show()
            }

            findNavController().navigateUp()
        }
    }

    private fun getSelectedDays(): String {
        val selectedDays = mutableListOf<String>()

        if (binding.cbSat.isChecked) selectedDays.add("Sat")
        if (binding.cbSun.isChecked) selectedDays.add("Sun")
        if (binding.cbMon.isChecked) selectedDays.add("Mon")
        if (binding.cbTue.isChecked) selectedDays.add("Tue")
        if (binding.cbWed.isChecked) selectedDays.add("Wed")
        if (binding.cbThu.isChecked) selectedDays.add("Thu")
        if (binding.cbFri.isChecked) selectedDays.add("Fri")

        return selectedDays.joinToString(",")
    }

    private fun setSelectedDays(days: String) {
        val selectedDays = days.split(",")

        binding.cbSat.isChecked = "Sat" in selectedDays
        binding.cbSun.isChecked = "Sun" in selectedDays
        binding.cbMon.isChecked = "Mon" in selectedDays
        binding.cbTue.isChecked = "Tue" in selectedDays
        binding.cbWed.isChecked = "Wed" in selectedDays
        binding.cbThu.isChecked = "Thu" in selectedDays
        binding.cbFri.isChecked = "Fri" in selectedDays
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}