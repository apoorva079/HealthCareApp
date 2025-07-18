package com.example.healthapp.data

data class UserProfile(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val dob: String? = null,
    val gender: String? = null,
    val bloodType: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val phone: String? = null,
    val allergies: List<String>? = null,
    val conditions: List<String>? = null,
    val primaryCarePhysician: String? = null,
    val physicianPhone: String? = null,
    val insuranceProvider: String? = null,
    val memberId: String? = null,
    val groupNumber: String? = null
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
}