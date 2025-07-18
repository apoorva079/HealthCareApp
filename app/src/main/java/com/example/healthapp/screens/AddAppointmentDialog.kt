package com.example.healthapp.screens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.healthapp.AppData
import com.example.healthapp.data.Appointment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentDialog(onDismiss: () -> Unit, onAddAppointment: (Appointment) -> Unit) {
    val context = LocalContext.current
    var doctorName by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) }
    var time by remember { mutableStateOf("10:00 AM") }
    var appointmentType by remember { mutableStateOf("Doctor Meet") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add New Appointment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = doctorName,
                    onValueChange = { doctorName = it },
                    label = { Text("Doctor/Test Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = specialty,
                    onValueChange = { specialty = it },
                    label = { Text("Specialty/Description") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g., 10:00 AM)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FilterChip(
                        selected = appointmentType == "Doctor Meet",
                        onClick = { appointmentType = "Doctor Meet" },
                        label = { Text("Doctor Meet") }
                    )
                    FilterChip(
                        selected = appointmentType == "Lab Test",
                        onClick = { appointmentType = "Lab Test" },
                        label = { Text("Lab Test") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        val parsedDate = try {
                            LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Invalid date format. Using current date.", Toast.LENGTH_SHORT).show()
                            LocalDate.now()
                        }
                        val newAppointment = Appointment(
                            doctorName = doctorName,
                            specialty = specialty,
                            date = parsedDate,
                            time = time,
                            status = "Pending",
                            type = appointmentType
                        )
                        onAddAppointment(newAppointment)
                    }) {
                        Text("Add Appointment")
                    }
                }
            }
        }
    }
}
