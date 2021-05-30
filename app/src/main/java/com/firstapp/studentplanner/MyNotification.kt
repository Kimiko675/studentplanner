package com.firstapp.studentplanner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService

class MyNotification : BroadcastReceiver() {

    lateinit var notificationChannel: NotificationChannel

    public var CHANNEL_ID = "channel-id"
    public var NOTIFICATION_ID = "notification-id"
    public var NOTIFICATION = "notification"
    var description = "Zadanie"



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification? = intent.getParcelableExtra(NOTIFICATION)

        CHANNEL_ID = intent.getStringExtra("channel-name").toString()
        description = intent.getStringExtra("description").toString()

        notificationChannel = NotificationChannel(CHANNEL_ID,description,NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )

            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, notification)
    }
}