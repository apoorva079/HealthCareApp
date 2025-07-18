package com.example.healthapp.data
import java.time.LocalDate
import java.util.UUID

data class Appointment(
    val id: String = UUID.randomUUID().toString(),
    val doctorName: String,
    val specialty: String,
    val date: LocalDate,
    val time: String,
    var status: String, // e.g., "Confirmed", "Pending", "Cancelled", "Rescheduled"
    val type: String // e.g., "Doctor Meet", "Lab Test"
)

