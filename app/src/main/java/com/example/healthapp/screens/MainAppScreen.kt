package com.example.healthapp.screens
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.healthapp.AppData
import com.example.healthapp.Routes
import com.example.healthapp.firebaseAuth // Access global firebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf("Home", "Calendar", "Records", "Profile")
                val icons = listOf(Icons.Default.Home, Icons.Default.CalendarToday, Icons.Default.Description, Icons.Default.Person)

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = currentRoute == item.lowercase(),
                        onClick = {
                            bottomNavController.navigate(item.lowercase()) {
                                popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(rootNavController, bottomNavController) }
            composable("calendar") { CalendarScreen(rootNavController) }
            composable("records") { MedicalRecordsScreen(rootNavController) }
            composable("profile") { ProfileScreen(rootNavController) }
        }
    }

    // Logout Confirmation Dialog
    if (AppData.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { AppData.showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(onClick = {
                    AppData.isLoggedIn = false
                    AppData.showLogoutDialog = false
                    firebaseAuth.signOut()
                    rootNavController.navigate(Routes.AUTH) {
                        popUpTo(Routes.HOME_TABS) { inclusive = true }
                    }
                    Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { AppData.showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}
