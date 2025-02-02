package org.ileewe.theseedjcli.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.ui.activities.MainActivity
import java.util.*

class NotificationUtils {

    companion object {

        val CHANNEL_ID = "com.audionowdigital.player.channels24.notifications"
        val CHANNEL_NAME = "News"
        val CHANNEL_DESCRIPTION = "Channels 24 - delivers breaking news alert around the world"
        val FOREGROUND_NOTIFICATION_PENDING_INTENT_ID = 202
        private var manager: NotificationManager? = null

        //Display notifications
        fun displayNotification(context: Context, title: String?, body: String?) {
            val NOTIFICATION_ID = Random(20).nextInt()


            //Define the notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = CHANNEL_DESCRIPTION
                channel.enableVibration(true)
                channel.enableLights(true)
                channel.canShowBadge()
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                channel.lightColor = context.resources.getColor(R.color.primaryColor, null)
                manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager!!.createNotificationChannel(channel)
            }
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            //Set the notification using notificationBuilder class
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.primaryColor))
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_sermons)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(foregroundIntent(context))
                .setAutoCancel(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O
            ) {
                builder.priority = Notification.PRIORITY_HIGH
            }
            val mNotificationMgr = NotificationManagerCompat.from(context)
            mNotificationMgr.notify(NOTIFICATION_ID, builder.build())
        }


        //Add contentIntent
        private fun foregroundIntent(context: Context): PendingIntent? {
            val startActivityIntent = Intent(context, MainActivity::class.java)
            //startActivityIntent.putExtra("DATA_ID", id);
            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return PendingIntent.getActivity(
                context,
                FOREGROUND_NOTIFICATION_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }


}