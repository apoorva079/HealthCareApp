package com.example.healthapp.data
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate
import java.util.UUID

data class MedicalRecord(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: String, // e.g., "Blood Test Results", "Prescription", "Chest X-Ray", "Visit Summary"
    val date: LocalDate,
    val description: String,
    val icon: ImageVector, // For UI representation
    var imageUrl: String? = null // For image upload demo
)

