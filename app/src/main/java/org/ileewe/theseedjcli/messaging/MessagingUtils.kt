package org.ileewe.theseedjcli.messaging

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import org.ileewe.theseedjcli.R

class MessagingUtils {

    companion object {

        private val TAG = "MessagingUtils"

        fun subscribeToTopic(context: Context, topic: String?) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic!!).addOnSuccessListener {
                val successMessage = context.resources.getString(
                    R.string.messaging_subscription_success,
                    topic
                )
                //Toast.makeText(context.getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
            }
        }


        fun unSubscribeFromTopic(context: Context, topic: String?) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic!!).addOnSuccessListener {
                val unSubscribeMessage = context.resources.getString(
                    R.string.messaging_subscription_removed,
                    topic
                )
                Toast.makeText(context.applicationContext, unSubscribeMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }


        private fun getDeviceToken(context: Context) {

            //Get registration token
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                // Get FCM registration token
                val token = task.result
                val msg =
                    context.resources.getString(R.string.messaging_subscription_token, token)
                Toast.makeText(context.applicationContext, msg, Toast.LENGTH_LONG).show()
            }
        }

    }


}