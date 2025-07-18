package com.example.healthapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.healthapp.firebaseAuth // Access global firebaseAuth
import com.example.healthapp.ui.theme.components.ProfileInfoRow
import com.example.healthapp.ui.theme.components.ProfileInfoTags
import com.example.healthapp.ui.theme.components.ProfileSection
import com.example.healthapp.ui.theme.components.SettingToggle
import androidx.compose.foundation.shape.RoundedCornerShape // Import for RoundedCornerShape
import com.example.healthapp.ui.theme.components.ProfileInfoRow
import com.example.healthapp.ui.theme.components.ProfileInfoTags
import com.example.healthapp.ui.theme.components.ProfileSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val user = AppData.userProfile

    // State for dark mode toggle (will be saved in real app)
    var isDarkModeEnabled by remember { mutableStateOf(false) } // This state needs to be persisted

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F2F5))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Profile Header
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(96.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        user.name ?: "Not provided",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        user.email ?: "Not provided",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Routes.EDIT_PROFILE) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Edit Profile")
                    }
                }
            }

            item {
                // Personal Information
                ProfileSection(title = "Personal Information") {
                    ProfileInfoRow(label = "Date of Birth", value = user.dob ?: "Not provided")
                    ProfileInfoRow(label = "Gender", value = user.gender ?: "Not provided")
                    ProfileInfoRow(label = "Blood Type", value = user.bloodType ?: "Not provided")
                    ProfileInfoRow(label = "Height", value = user.height ?: "Not provided")
                    ProfileInfoRow(label = "Weight", value = user.weight ?: "Not provided")
                    ProfileInfoRow(label = "Phone", value = user.phone ?: "Not provided")
                }
            }

            item {
                // Medical Information
                ProfileSection(title = "Medical Information") {
                    ProfileInfoTags(label = "Allergies", tags = user.allergies ?: emptyList())
                    ProfileInfoTags(label = "Conditions", tags = user.conditions ?: emptyList())
                    ProfileInfoRow(
                        label = "Primary Care Physician",
                        value = user.primaryCarePhysician ?: "Not provided"
                    )
                    ProfileInfoRow(
                        label = "Physician Phone",
                        value = user.physicianPhone ?: "Not provided"
                    )
                }
            }

            item {
                // InsuranceInformation
                ProfileSection(title = "Insurance Information") {
                    ProfileInfoRow(
                        label = "Provider",
                        value = user.insuranceProvider ?: "Not provided"
                    )
                    ProfileInfoRow(label = "Member ID", value = user.memberId ?: "Not provided")
                    ProfileInfoRow(
                        label = "Group Number",
                        value = user.groupNumber ?: "Not provided"
                    )
                }
            }

            item {
                // Settings
                ProfileSection(title = "Settings") {
                    SettingToggle(label = "Notifications") { isChecked ->
                        Toast.makeText(context, "Notifications: $isChecked", Toast.LENGTH_SHORT)
                            .show()
                    }
                    SettingToggle(label = "Appointment Reminders") { isChecked ->
                        Toast.makeText(
                            context,
                            "Appointment Reminders: $isChecked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    SettingToggle(label = "Medication Alerts") { isChecked ->
                        Toast.makeText(context, "Medication Alerts: $isChecked", Toast.LENGTH_SHORT)
                            .show()
                    }
                    SettingToggle(label = "Health Tips") { isChecked ->
                        Toast.makeText(context, "Health Tips: $isChecked", Toast.LENGTH_SHORT)
                            .show()
                    }
                    SettingToggle(label = "Dark Mode") { isChecked ->
                        isDarkModeEnabled = isChecked
                        Toast.makeText(context, "Dark Mode: $isChecked", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            item {
                // Logout Button
                Button(
                    onClick = { AppData.showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Log Out", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp)) //Bottom Padding
            }
        }
    }
}
