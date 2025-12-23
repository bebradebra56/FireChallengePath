package com.firechalang.pathapp.iotrtg.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.firechalang.pathapp.FireChallengePathActivity
import com.firechalang.pathapp.R
import com.firechalang.pathapp.iotrtg.presentation.app.FireChallengePathApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val FIRE_CHALLENGE_PATH_CHANNEL_ID = "fire_challenge_path_notifications"
private const val FIRE_CHALLENGE_PATH_CHANNEL_NAME = "FireChallengePath Notifications"
private const val FIRE_CHALLENGE_PATH_NOT_TAG = "FireChallengePath"

class FireChallengePathPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                fireChallengePathShowNotification(it.title ?: FIRE_CHALLENGE_PATH_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                fireChallengePathShowNotification(it.title ?: FIRE_CHALLENGE_PATH_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            fireChallengePathHandleDataPayload(remoteMessage.data)
        }
    }

    private fun fireChallengePathShowNotification(title: String, message: String, data: String?) {
        val fireChallengePathNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FIRE_CHALLENGE_PATH_CHANNEL_ID,
                FIRE_CHALLENGE_PATH_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            fireChallengePathNotificationManager.createNotificationChannel(channel)
        }

        val fireChallengePathIntent = Intent(this, FireChallengePathActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val fireChallengePathPendingIntent = PendingIntent.getActivity(
            this,
            0,
            fireChallengePathIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val fireChallengePathNotification = NotificationCompat.Builder(this, FIRE_CHALLENGE_PATH_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.fire_challenge_path_noti_ic)
            .setAutoCancel(true)
            .setContentIntent(fireChallengePathPendingIntent)
            .build()

        fireChallengePathNotificationManager.notify(System.currentTimeMillis().toInt(), fireChallengePathNotification)
    }

    private fun fireChallengePathHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(FireChallengePathApplication.FIRE_CHALLENGE_PATH_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}