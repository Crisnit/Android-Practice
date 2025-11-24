package com.example.androidpractice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class FavoritePairAlarmReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "favorite_pair_channel"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_USER_NAME = "user_name"
    }
    override fun onReceive(context: Context, intent: Intent?) {
        val userName = intent?.getStringExtra(EXTRA_USER_NAME) ?: "Друг"
        createNotificationChannel(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Пора на пару, $userName!")
            .setContentText("Твоя любимая пара начинается прямо сейчас")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Уведомления о любимой паре",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Напоминание о начале любимой пары"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}