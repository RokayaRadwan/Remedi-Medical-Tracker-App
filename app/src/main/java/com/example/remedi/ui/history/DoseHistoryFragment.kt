package com.example.remedi.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProvider
import com.example.remedi.data.local.entity.DoseHistoryEntity
import com.example.remedi.databinding.DoseHistoryFragmentBinding
import com.example.remedi.data.local.AppDatabase
import com.example.remedi.data.repository.DoseHistoryRepository
import com.example.remedi.data.repository.ReminderRepository
import com.example.remedi.viewmodel.ReminderViewModel
import com.example.remedi.viewmodel.ReminderViewModelFactory

class DoseHistoryFragment : Fragment() {
    private lateinit var reminderViewModel: ReminderViewModel
    private var _binding: DoseHistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var doseHistoryAdapter: DoseHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DoseHistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        val reminderRepo = ReminderRepository(database.reminderDao())
        val historyRepo = DoseHistoryRepository(database.doseHistoryDao())
        val factory = ReminderViewModelFactory(reminderRepo, historyRepo)

        reminderViewModel = ViewModelProvider(requireActivity(), factory)[ReminderViewModel::class.java]

        setupRecyclerView()
        setupClickListeners()

        reminderViewModel.doseHistory.observe(viewLifecycleOwner) { historyList ->
            doseHistoryAdapter.updateList(historyList)
            updateUi(historyList)
        }
    }

    private fun setupRecyclerView() {
        doseHistoryAdapter = DoseHistoryAdapter(emptyList())

        binding.rvDoseHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = doseHistoryAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnClearHistory.setOnClickListener {
            reminderViewModel.clearDoseHistory()
            Toast.makeText(
                requireContext(),
                "Dose history cleared",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUi(historyList: List<DoseHistoryEntity>) {
        binding.tvTotalDoses.text = historyList.size.toString()

        if (historyList.isEmpty()) {
            binding.rvDoseHistory.visibility = View.GONE
            binding.tvEmptyDoseHistory.visibility = View.VISIBLE
            binding.btnClearHistory.isEnabled = false
            binding.btnClearHistory.alpha = 0.5f
        } else {
            binding.rvDoseHistory.visibility = View.VISIBLE
            binding.tvEmptyDoseHistory.visibility = View.GONE
            binding.btnClearHistory.isEnabled = true
            binding.btnClearHistory.alpha = 1f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}