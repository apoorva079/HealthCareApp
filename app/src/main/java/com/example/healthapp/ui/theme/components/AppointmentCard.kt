package com.example.healthapp.ui.theme.components


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthapp.Routes
import com.example.healthapp.data.Appointment
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: Appointment,
    navController: NavController,
    onConfirm: (String) -> Unit,
    onReschedule: (String) -> Unit,
    onCancel: (String) -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "Doctor", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(appointment.specialty, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                AssistChip(
                    onClick = { /* No action on click */ },
                    label = { Text(appointment.status) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (appointment.status) {
                            "Confirmed" -> Color(0xFFE8F5E9)
                            "Pending" -> Color(0xFFFFF3E0)
                            "Cancelled" -> Color(0xFFFBE9E7)
                            "Rescheduled" -> Color(0xFFE3F2FD) // Light blue for rescheduled
                            else -> Color.LightGray
                        },
                        labelColor = when (appointment.status) {
                            "Confirmed" -> Color(0xFF2E7D32)
                            "Pending" -> Color(0xFFF57F17)
                            "Cancelled" -> Color(0xFFD32F2F)
                            "Rescheduled" -> Color(0xFF1976D2) // Dark blue for rescheduled
                            else -> Color.DarkGray
                        }
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Date", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${appointment.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}, ${appointment.time}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (appointment.status == "Confirmed") {
                    Button(
                        onClick = { navController.navigate(Routes.MOCK_VIDEO_CALL) },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Join Video Call")
                    }
                    OutlinedButton(
                        onClick = { onReschedule(appointment.id) },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reschedule")
                    }
                } else if (appointment.status == "Pending") {
                    Button(
                        onClick = { onConfirm(appointment.id) },
                        modifier = Modifier.weight(1f).padding(end = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Confirm")
                    }
                    OutlinedButton(
                        onClick = { onCancel(appointment.id) },
                        modifier = Modifier.weight(1f).padding(start = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }
                } else if (appointment.status == "Rescheduled") {
                    TextButton(onClick = { /* View new details */ }) {
                        Text("View New Details")
                    }
                } else if (appointment.status == "Cancelled") {
                    Text("Cancelled", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
