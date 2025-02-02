package org.ileewe.theseedjcli.remoteconfig

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.*
import kotlin.collections.HashMap

class RemoteConfigUtils {

    /*val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }
    remoteConfig.setConfigSettingsAsync(configSettings)*/

    companion object {

        private val TAG: String = RemoteConfigUtils::class.java.simpleName
        const val KEY_DISPLAY_VIEW = "display_view"
        const val KEY_ACTION_LINK_URL = "action_link_url"
        const val KEY_ACTION_LINK_TEXT = "action_link_text"


        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 900 // 900- 15 mins,3600 1 hr
        }

        fun getRemoteConfig(context: FirebaseApp): FirebaseRemoteConfig {

            val remoteConfig: FirebaseRemoteConfig? = null
            if(remoteConfig == null) {
                return FirebaseRemoteConfig.getInstance(context)
            }
            return remoteConfig
        }

        /*fun myRemoteConfig(): FirebaseRemoteConfig? {
            return remoteConfig
        }*/




    }


}