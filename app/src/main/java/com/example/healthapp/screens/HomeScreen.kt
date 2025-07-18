// screens/HomeScreen.kt
package com.example.healthapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthapp.AppData
import com.example.healthapp.Routes
import com.example.healthapp.ui.theme.components.HealthMetricCircle // HealthMetricCircle
import com.example.healthapp.ui.theme.components.QuickActionButton // QuickActionButton
import com.example.healthapp.ui.theme.components.AppointmentCard // AppointmentCard
import com.example.healthapp.ui.theme.components.MedicationReminderCard // MedicationReminderCard
import com.example.healthapp.ui.theme.components.MedicationReminderCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.healthapp.ui.theme.utils.sendLocalNotification
import com.example.healthapp.ui.theme.components.AppointmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(rootNavController: NavController, bottomNavController: NavController) {
    val context = LocalContext.current // Get context for toasts/notifications

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hello, ${AppData.userProfile.name ?: "User"}", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text("How are you feeling today?", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Navigate to profile tab via rootNavController
                        // This navigates within the main NavHost, switching the bottom tab
                        bottomNavController.navigate("profile") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F2F5))
                .verticalScroll(rememberScrollState()) // Enable vertical scrolling for the whole column
        ) {
            // Health Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Health Summary",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { /* TODO: Navigate to Health Details */ }) {
                        Text("View Details", color = Color.White.copy(alpha = 0.8f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        HealthMetricCircle("Activity", AppData.healthSummary.activity)
                        HealthMetricCircle("Sleep", AppData.healthSummary.sleep)
                        HealthMetricCircle("Nutrition", AppData.healthSummary.nutrition)
                    }
                }
            }

            // Quick Actions
            Text(
                text = "Quick Actions",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    QuickActionButton(
                        icon = Icons.Default.CalendarMonth, // Changed icon to CalendarMonth
                        label = "Appointments", // Changed label
                        color = Color(0xFFE0F7FA)
                    ) {
                        bottomNavController.navigate("calendar") // Navigate to Calendar tab
                        Toast.makeText(context, "Navigating to Appointments", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.Folder, // Changed icon to Folder
                        label = "Records",
                        color = Color(0xFFE8F5E9)
                    ) {
                        bottomNavController.navigate("records") // Navigate to Records tab
                        Toast.makeText(context, "Navigating to Records", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.Description, // Changed icon to Description
                        label = "Prescriptions",
                        color = Color(0xFFF3E5F5) // Light purple
                    ) {
                        bottomNavController.navigate("records") // Navigate to Records tab (or specific prescriptions section)
                        Toast.makeText(context, "Navigating to Prescriptions", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.Message, // Changed icon to Message
                        label = "Messages",
                        color = Color(0xFFFFF3E0) // Light orange
                    ) {
                        rootNavController.navigate(Routes.MESSAGES) // Navigate to Messages screen
                        Toast.makeText(context, "Navigating to Messages", Toast.LENGTH_SHORT).show()
                    }
                }
                item {
                    QuickActionButton(
                        icon = Icons.Default.LocalHospital, // Hospital icon
                        label = "Nearby\nHospitals",
                        color = Color(0xFFFFEBEE) // Light red
                    ) {
                        Toast.makeText(context, "Finding nearby hospitals...", Toast.LENGTH_SHORT).show()
                        // TODO: Implement actual location services and map integration here
                    }
                }
            }

            // Upcoming Appointments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upcoming Appointments",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                TextButton(onClick = { bottomNavController.navigate("calendar") }) {
                    Text("See All") // Changed from "View All" to "See All"
                }
            }

            val nextTwoAppointments = AppData.upcomingAppointments
                .filter { it.date >= LocalDate.now() } // Filter out past appointments
                .sortedBy { it.date } // Sort by date
                .take(2)
            if (nextTwoAppointments.isEmpty()) {
                Text(
                    text = "No upcoming appointments.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    nextTwoAppointments.forEach { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            navController = rootNavController, // Use rootNavController for video call
                            onConfirm = { id ->
                                val index = AppData.upcomingAppointments.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    val updatedAppointment = AppData.upcomingAppointments[index].copy(status = "Confirmed")
                                    AppData.upcomingAppointments[index] = updatedAppointment // Replace to trigger recomposition
                                }
                                Toast.makeText(context, "Appointment Confirmed!", Toast.LENGTH_SHORT).show()
                            },
                            onReschedule = { id ->
                                AppData.upcomingAppointments.find { it.id == id }?.let { appt ->
                                    val index = AppData.upcomingAppointments.indexOfFirst { it.id == id }
                                    if (index != -1) {
                                        val updatedAppointment = AppData.upcomingAppointments[index].copy(status = "Rescheduled")
                                        AppData.upcomingAppointments[index] = updatedAppointment // Replace to trigger recomposition
                                    }
                                    AppData.showAddAppointmentDialog = true // Reuse add dialog for reschedule
                                    Toast.makeText(context, "Reschedule dialog for ${appt.doctorName}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onCancel = { id ->
                                AppData.upcomingAppointments.removeIf { it.id == id }
                                Toast.makeText(context, "Appointment Cancelled!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            // Medication Reminders
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Medication Reminders",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                TextButton(onClick = { rootNavController.navigate(Routes.ALL_MEDICATIONS) }) {
                    Text("See All") // Changed from "View All" to "See All"
                }
            }

            val nextTwoMedications = AppData.medicationReminders.take(2)
            if (nextTwoMedications.isEmpty()) {
                Text(
                    text = "No medication reminders.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    nextTwoMedications.forEach { medication ->
                        MedicationReminderCard(medication = medication, onTakeMedication = {
                            sendLocalNotification(
                                context,
                                "Medication Reminder",
                                "It's time to take ${medication.name} (${medication.dosage})!",
                                "Medication taken: ${medication.name}"
                            )
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
