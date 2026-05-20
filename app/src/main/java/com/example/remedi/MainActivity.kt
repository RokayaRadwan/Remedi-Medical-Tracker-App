package com.example.remedi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.remedi.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    private val screensWithBottomBar = setOf(
        R.id.dashboardFragment,
        R.id.medicationListFragment,
        R.id.reminderScheduleFragment,
        R.id.profileSettingsFragment,
        R.id.todayMedicationsFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        setupBottomNavigation()
        setupFab()
        controlBottomBarVisibility()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            navigateTo(item.itemId)
            true
        }
    }

    private fun setupFab() {
        binding.fabAddMedication.setOnClickListener {
            navigateTo(R.id.addEditMedicationFragment)
        }
    }

    private fun controlBottomBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomBar = destination.id in screensWithBottomBar

            binding.bottomBarContainer.isVisible = showBottomBar
            binding.fabAddMedication.isVisible = showBottomBar

            val menuItem = binding.bottomNavigation.menu.findItem(destination.id)
            menuItem?.isChecked = true
        }
    }

    private fun navigateTo(destinationId: Int) {
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }
}