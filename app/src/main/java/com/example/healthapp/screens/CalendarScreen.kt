package com.example.healthapp.screens
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.healthapp.AppData
import com.example.healthapp.Routes
import com.example.healthapp.data.Appointment
import com.example.healthapp.ui.theme.components.AppointmentCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val context = LocalContext.current
    val currentMonth = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    val appointmentsForSelectedDate = AppData.upcomingAppointments.filter { it.date == selectedDate.value }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { AppData.showAddAppointmentDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Appointment")
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
                .padding(16.dp)
        ) {
            // Month Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth.value = currentMonth.value.minusMonths(1) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
                }
                Text(
                    text = currentMonth.value.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { currentMonth.value = currentMonth.value.plusMonths(1) }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Day Headers
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(day, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val firstDayOfMonth = currentMonth.value.withDayOfMonth(1)
            val daysInMonth = currentMonth.value.lengthOfMonth()
            val firstDayOfWeekValue = firstDayOfMonth.dayOfWeek.value // MONDAY = 1, SUNDAY = 7
            val daysOffset = if (firstDayOfWeekValue == 7) 0 else firstDayOfWeekValue // If Sunday (7), offset is 0, else its value

            val calendarDays = remember(currentMonth.value) { // Use remember to avoid recalculation
                mutableListOf<LocalDate?>().apply {
                    for (i in 0 until daysOffset) {
                        add(null) // Placeholder for days before the 1st of the month
                    }
                    for (i in 1..daysInMonth) {
                        add(currentMonth.value.withDayOfMonth(i))
                    }
                }
            }

            // Display days in a grid
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(calendarDays.chunked(7)) { week ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        week.forEach { date ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (date == selectedDate.value) MaterialTheme.colorScheme.primary else Color.Transparent
                                    )
                                    .clickable { if (date != null) selectedDate.value = date },
                                contentAlignment = Alignment.Center
                            ) {
                                if (date != null) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = if (date == selectedDate.value) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Appointments for selected date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate.value.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (appointmentsForSelectedDate.isNotEmpty()) {
                    Text("${appointmentsForSelectedDate.size} Appointments", color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (appointmentsForSelectedDate.isEmpty()) {
                Text("No appointments for this date.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appointmentsForSelectedDate) { appointment ->
                        AppointmentCard(
                            appointment = appointment,
                            navController = navController,
                            onConfirm = { id ->
                                val index = AppData.upcomingAppointments.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    val updatedAppointment = AppData.upcomingAppointments[index].copy(status = "Confirmed")
                                    AppData.upcomingAppointments[index] = updatedAppointment
                                }
                                Toast.makeText(context, "Appointment Confirmed!", Toast.LENGTH_SHORT).show()
                            },
                            onReschedule = { id ->
                                AppData.upcomingAppointments.find { it.id == id }?.let { appt ->
                                    val index = AppData.upcomingAppointments.indexOfFirst { it.id == id }
                                    if (index != -1) {
                                        val updatedAppointment = AppData.upcomingAppointments[index].copy(status = "Rescheduled")
                                        AppData.upcomingAppointments[index] = updatedAppointment
                                    }
                                    AppData.showAddAppointmentDialog = true
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
        }
    }

    if (AppData.showAddAppointmentDialog) {
        AddAppointmentDialog(
            onDismiss = { AppData.showAddAppointmentDialog = false },
            onAddAppointment = { newAppointment ->
                AppData.upcomingAppointments.add(newAppointment)
                Toast.makeText(context, "Appointment Added!", Toast.LENGTH_SHORT).show()
                AppData.showAddAppointmentDialog = false
            }
        )
    }
}

