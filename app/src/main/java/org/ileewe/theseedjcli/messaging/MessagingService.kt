package org.ileewe.theseedjcli.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {

    private val TAG = "MessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val messageBody = remoteMessage.notification!!.body
            if (remoteMessage.data.size > 0) {
                val id = remoteMessage.data["id"]
                Log.d(TAG, "Messaging: FCM onMessageReceived is called with id $id")
            }
            NotificationUtils.displayNotification(applicationContext, title, messageBody)
            Log.d(TAG, "Messaging: FCM onMessageReceived is called...")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Messaging: FCM token Refreshed: $token")
        //Send registration to remote server
        //sendRegistrationToServer(token);
    }

}