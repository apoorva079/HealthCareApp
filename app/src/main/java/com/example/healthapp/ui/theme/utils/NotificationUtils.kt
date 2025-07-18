package com.example.healthapp.ui.theme.utils


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.healthapp.CHANNEL_ID
import com.example.healthapp.MainActivity
import com.example.healthapp.NOTIFICATION_ID
import com.example.healthapp.R // Import R for notification icon

fun sendLocalNotification(context: Context, title: String, content: String, ticker: String) {
    // Check if POST_NOTIFICATIONS permission is granted before sending notification
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, cannot send notification.
            // In a real app, you might want to show a dialog explaining why permission is needed.
            Toast.makeText(context, "Notification permission not granted. Cannot send notification.", Toast.LENGTH_LONG).show()
            return
        }
    }

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_icon) // Ensure you have a small notification icon (e.g., a simple white circle)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setTicker(ticker) // Text that scrolls across the status bar

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(NOTIFICATION_ID, builder.build())
    }
}
