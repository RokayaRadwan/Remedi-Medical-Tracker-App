package com.example.remedi.ui.medication

data class Medication(
    val name: String,
    val dose: String,
    val time: String,
    val refills: String,
    val instruction: String,
    val color: Int = 0xFF2DBE7F.toInt()
)

data class Vitamin(
    val name: String,
    val dosage: String,
    val icon: Int
)