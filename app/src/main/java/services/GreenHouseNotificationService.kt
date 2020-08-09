package services

import activities.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.iotgreenhouse.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GreenHouseNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Notification", "$message")

        message.data.isNotEmpty().let {
            val home = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, home, PendingIntent.FLAG_ONE_SHOT)
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val customNotification = NotificationCompat.Builder(applicationContext, "NEWS_NOTIFICATIONS")
                .setSmallIcon(R.drawable.ic_hardware)
                .setContentTitle(message.notification?.title)
                .setSound(uri)
                .setContentIntent(pendingIntent)

            val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("NEWS_NOTIFICATIONS", "NEWS STORY NOTIFICATION CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManger.createNotificationChannel(channel)
            }
         notificationManger.notify(0, customNotification.build())
        }
    }
}