package org.ileewe.theseedjcli.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics {

    companion object {

        private val TAG = "Analytics"
        private var mFirebaseAnalytics: FirebaseAnalytics? = null


        private fun getAnalytics(context: Context?): FirebaseAnalytics {
            if (mFirebaseAnalytics == null) {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
            }
            return mFirebaseAnalytics!!
        }


        fun recordScreenView(context: Context?, screenName: String?, TAG: String?) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, TAG)
            getAnalytics(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
            Log.d(TAG, "Logging screen view")
        }

        fun recordSectionVisit(context: Context?, section: String) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, section)
            getAnalytics(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
            Log.d(TAG, "Logging screen view - Section being queried  $section")
        }

        fun recordShareEvent(context: Context?, title: String?, url: String?) {
            //Log Share Event
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title)
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, url)
            getAnalytics(context).logEvent(FirebaseAnalytics.Event.SHARE, bundle)
        }
    }

}