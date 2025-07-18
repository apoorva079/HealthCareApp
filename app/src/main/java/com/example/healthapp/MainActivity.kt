// MainActivity.kt
package com.example.healthapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthapp.screens.* // screens पैकेज से सभी स्क्रीन इंपोर्ट करें
import com.example.healthapp.ui.theme.HealthAppTheme // थीम इंपोर्ट करें
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import androidx.core.content.ContextCompat // परमिशन चेक के लिए

// ग्लोबल Firebase वेरिएबल्स (MainActivity में इनिशियलाइज़ होंगे)
// अब AppData ऑब्जेक्ट में Firebase इंस्टेंस को सीधे एक्सेस नहीं करेंगे, बल्कि AppData.initFirebase के माध्यम से पास करेंगे।
lateinit var firebaseAuth: FirebaseAuth // अब भी यहाँ lateinit रहेगा
lateinit var firebaseFirestore: FirebaseFirestore // अब भी यहाँ lateinit रहेगा

// नोटिफिकेशन चैनल ID
const val CHANNEL_ID = "health_app_channel"
const val NOTIFICATION_ID = 101

// --- Main Activity ---
class MainActivity : ComponentActivity() {

    // Launcher for requesting POST_NOTIFICATIONS permission
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // परमिशन मिली, अब आप नोटिफिकेशन्स पोस्ट कर सकते हैं
            Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            // परमिशन नहीं मिली, तदनुसार हैंडल करें (जैसे, एक मैसेज दिखाएं)
            Toast.makeText(this, "Notification permission denied. Some features may be limited.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // नोटिफिकेशन चैनल बनाएं (Android 8.0+ के लिए आवश्यक)
        createNotificationChannel()

        // Android 13+ को टारगेट करते समय POST_NOTIFICATIONS परमिशन का अनुरोध करें
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        try {
            // Firebase ऐप इनिशियलाइज़ करें - यह एक बार होना चाहिए
            FirebaseApp.initializeApp(this)
        } catch (e: IllegalStateException) {
            // FirebaseApp पहले से ही इनिशियलाइज़्ड है, अनदेखा करें
        }

        // Initialize global Firebase instances
        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

        // Initialize AppData with Firebase instances
        AppData.initFirebase(firebaseAuth, firebaseFirestore)


        setContent {
            HealthAppTheme { // HealthAppTheme समग्र थीम को नियंत्रित करेगा जिसमें डार्क मोड भी शामिल है
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var showSplash by remember { mutableStateOf(true) }
                    // वास्तविक स्टार्ट रूट को स्प्लैश के बाद होल्ड करने के लिए एक म्यूटेबलस्टेट का उपयोग करें
                    var startRoute by remember { mutableStateOf(Routes.SPLASH) }

                    LaunchedEffect(Unit) {
                        delay(2000) // स्प्लैश 2 सेकंड के लिए दिखाएं
                        showSplash = false // स्प्लैश स्क्रीन UI को फेड आउट करें

                        val auth = FirebaseAuth.getInstance()
                        // Determine initial destination based on login status
                        val initialDestination = if (auth.currentUser != null && AppData.isLoggedIn) {
                            Routes.HOME_TABS
                        } else {
                            Routes.AUTH
                        }
                        startRoute = initialDestination // स्टार्टरूट स्टेट को अपडेट करें

                        // Navigate explicitly after splash delay
                        navController.navigate(initialDestination) {
                            popUpTo(Routes.SPLASH) { inclusive = true } // बैक स्टैक से स्प्लैश हटाएं
                        }

                        // Handle initial anonymous sign-in or check for existing user after navigation decision
                        try {
                            if (auth.currentUser == null) {
                                auth.signInAnonymously().await()
                                println("Signed in anonymously for demonstration.")
                            } else {
                                println("User already signed in: ${auth.currentUser?.uid}")
                            }
                        } catch (e: Exception) {
                            println("Firebase Auth Error during initial sign-in: ${e.message}")
                        }
                    }

                    // Navigation Host
                    NavHost(
                        navController = navController,
                        startDestination = startRoute // स्टार्टडेस्टिनेशन के लिए म्यूटेबल स्टेट का उपयोग करें
                    ) {
                        composable(Routes.SPLASH) {
                            AnimatedVisibility(
                                visible = showSplash, // यह स्प्लैश स्क्रीन की दृश्यता को नियंत्रित करता है
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                SplashScreen(navController = navController) // SplashScreen को navController पास करें
                            }
                        }
                        composable(Routes.AUTH) {
                            AuthScreen(navController = navController)
                        }
                        composable(Routes.HOME_TABS) {
                            MainAppScreen(rootNavController = navController)
                        }
                        composable(Routes.EDIT_PROFILE) {
                            EditProfileScreen(navController = navController)
                        }
                        composable(Routes.MOCK_VIDEO_CALL) {
                            MockVideoCallScreen(navController = navController)
                        }
                        composable(Routes.ALL_MEDICATIONS) {
                            AllMedicationsScreen(navController = navController)
                        }
                        composable(Routes.MESSAGES) {
                            MessagesScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }

    // नोटिफिकेशन चैनल बनाने का फ़ंक्शन (Android 8.0+ के लिए आवश्यक)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Health App Reminders"
            val descriptionText = "Notifications for appointments and medication reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
