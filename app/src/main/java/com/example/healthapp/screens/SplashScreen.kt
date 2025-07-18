// screens/SplashScreen.kt
package com.example.healthapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthapp.R // Make sure to import your R file for drawables

@Composable
fun SplashScreen(navController: NavController) { // Added navController as parameter

    val startColor = Color(0xFFE8DDCB) // Light brownish-beige from top-left
    val endColor = Color(0xFFDCC8B3)   // Slightly darker brownish-beige from bottom-right

    Box( // Use Box to layer the circles on top of the column
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = listOf(startColor, endColor))) // Apply linear gradient
    ) {
        // Circles (Approximated positions, you might need to fine-tune with specific modifiers)
        // Top-left large circle
        Box(
            modifier = Modifier
                .align(Alignment.TopStart) // Align to top-start
                .offset(x = (-80).dp, y = (-80).dp) // Adjust position to move it into view
                .size(200.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)) // Semi-transparent white for effect
        )
        // Bottom-left smaller circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart) // Align to bottom-start
                .offset(x = (-40).dp, y = (40).dp) // Adjust position
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)) // Semi-transparent white for effect
        )
        // Top-right large circle
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd) // Align to top-end
                .offset(x = (80).dp, y = (-120).dp) // Adjust position
                .size(150.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)) // Semi-transparent white for effect
        )
        // Bottom-right smaller circle (added to match the image better)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to bottom-end
                .offset(x = (40).dp, y = (80).dp) // Adjust position
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF)) // Semi-transparent white for effect
        )


        // Main content of the splash screen (centered)
        Column(
            modifier = Modifier.fillMaxSize(), // Fill the entire Box
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main heart icon and text
            Image(
                painter = painterResource(id = R.drawable.ic_heart_brown), // Assuming a brown heart icon for contrast
                contentDescription = "HealthCare Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "HealthCare",
                color = Color(0xFF4A4A4A), // Dark grey text for contrast
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your health, our priority", // Updated tagline as per image
                color = Color(0xFF6A6A6A), // Slightly lighter grey for subtitle
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(40.dp))
            // Linear progress indicator and loading text
            LinearProgressIndicator(
                color = Color(0xFF7B5A40), // Brownish color for progress bar
                trackColor = Color(0xFFB3A596), // Lighter track color
                modifier = Modifier.width(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Loading your personal health dashboard...",
                color = Color(0xFF6A6A6A),
                fontSize = 14.sp
            )
        }
    }
}
