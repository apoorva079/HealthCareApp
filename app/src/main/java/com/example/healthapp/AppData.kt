// AppData.kt
package com.example.healthapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Science
import com.example.healthapp.data.Appointment // Import Appointment data class
import com.example.healthapp.data.Medication // Import Medication data class
import com.example.healthapp.data.MedicalRecord // Import MedicalRecord data class
import com.example.healthapp.data.UserProfile // Import UserProfile data class
import com.example.healthapp.data.HealthSummaryData // Import HealthSummaryData data class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

object AppData {
    // Firebase instances, now managed within AppData object
    // इन्हें अब MainActivity से initFirebase फंक्शन के माध्यम से पास किया जाएगा
    private lateinit var _firebaseAuth: FirebaseAuth
    private lateinit var _firebaseFirestore: FirebaseFirestore

    // Firebase इंस्टेंस के लिए पब्लिक गेटर्स
    val firebaseAuth: FirebaseAuth
        get() = _firebaseAuth
    val firebaseFirestore: FirebaseFirestore
        get() = _firebaseFirestore

    // AppData में Firebase इंस्टेंस को इनिशियलाइज़ करने का फ़ंक्शन
    fun initFirebase(auth: FirebaseAuth, firestore: FirebaseFirestore) {
        _firebaseAuth = auth
        _firebaseFirestore = firestore
    }

    val healthSummary = HealthSummaryData(activity = 75, sleep = 60, nutrition = 80)

    val upcomingAppointments = mutableStateListOf(
        Appointment("app1", "Dr. Sarah Johnson", "Cardiologist", LocalDate.now(), "2:00 PM", "Confirmed", "Doctor Meet"),
        Appointment("app2", "Dr. Michael Chen", "General Practitioner", LocalDate.now().plusDays(1), "10:30 AM", "Pending", "Doctor Meet"),
        Appointment("app3", "Lab Test", "Blood Work", LocalDate.now().plusDays(2), "9:00 AM", "Confirmed", "Lab Test")
    )

    val medicationReminders = mutableStateListOf(
        Medication("med1", "Lisinopril", "10mg, 1 tablet", "8:00 AM"),
        Medication("med2", "Metformin", "500mg, 1 tablet", "1:00 PM"),
        Medication("med3", "Atorvastatin", "20mg, 1 tablet", "8:00 PM")
    )

    val allMedicalRecords = mutableStateListOf(
        MedicalRecord("rec1", "Blood Test Results", "Lab Results", LocalDate.of(2023, 6, 5), "Complete Blood Count (CBC)", Icons.Default.Science, null),
        MedicalRecord("rec2", "Prescription", "Prescriptions", LocalDate.of(2023, 5, 28), "Lisinopril, Metformin, Atorvastatin", Icons.Default.Description, null),
        MedicalRecord("rec3", "Chest X-Ray", "Imaging Results", LocalDate.of(2023, 5, 15), "Imaging Results", Icons.Default.Image, null),
        MedicalRecord("rec4", "Visit Summary", "Visit Summary", LocalDate.of(2023, 5, 10), "Dr. Michael Chen", Icons.Default.MedicalServices, null)
    )

    var userProfile by mutableStateOf(UserProfile())
    var isLoggedIn by mutableStateOf(false)
    var showLogoutDialog by mutableStateOf(false)
    var showAddAppointmentDialog by mutableStateOf(false)
    var showVideoCallScreen by mutableStateOf(false)

    // Function to fetch user profile from Firestore
    fun fetchUserProfile(userId: String) {
        val firestoreAppId = "healthapp-default"
        if (::_firebaseFirestore.isInitialized) { // Check if initialized
            _firebaseFirestore.collection("artifacts")
                .document(firestoreAppId)
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userProfile = document.toObject(UserProfile::class.java) ?: UserProfile()
                        println("User profile fetched: ${userProfile.name}")
                    } else {
                        // If no profile exists, create a default one with the current userId and basic info
                        // Ensure _firebaseAuth is initialized before accessing currentUser
                        val currentUserEmail = if (::_firebaseAuth.isInitialized) _firebaseAuth.currentUser?.email else "newuser@example.com"
                        saveUserProfile(UserProfile(userId = userId, name = "New User", email = currentUserEmail))
                        println("No profile found, creating default for user: $userId")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error fetching user profile: ${e.message}")
                    userProfile = UserProfile(userId = userId)
                }
        } else {
            println("Firebase Firestore not initialized in AppData. Cannot fetch profile.")
        }
    }

    // Function to save user profile to Firestore
    fun saveUserProfile(profile: UserProfile) {
        val userId = if (::_firebaseAuth.isInitialized) _firebaseAuth.currentUser?.uid else null
        if (userId == null) {
            println("Firebase Auth not initialized or user not logged in in AppData. Cannot save profile.")
            return
        }

        val firestoreAppId = "healthapp-default"
        if (::_firebaseFirestore.isInitialized) { // Check if initialized
            _firebaseFirestore.collection("artifacts")
                .document(firestoreAppId)
                .collection("users")
                .document(userId)
                .set(profile)
                .addOnSuccessListener {
                    userProfile = profile
                    println("User profile saved successfully!")
                }
                .addOnFailureListener { e ->
                    println("Error saving user profile: ${e.message}")
                }
        } else {
            println("Firebase Firestore not initialized in AppData. Cannot save profile.")
        }
    }
}
