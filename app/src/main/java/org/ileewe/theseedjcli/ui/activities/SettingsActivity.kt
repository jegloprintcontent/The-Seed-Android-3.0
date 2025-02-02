package org.ileewe.theseedjcli.ui.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.messaging.MessagingUtils
import org.ileewe.theseedjcli.ui.activities.MainActivity.Companion.NOTIFICATION_ALREADY_SUBSCRIBED


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        private val TAG = SettingsActivity::class.java.simpleName
        private var alreadySubscribed: Boolean? = null
        private lateinit var mPreferences: SharedPreferences

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            //setPreferencesFromResource(R.xml.root_preferences, rootKey)
            addPreferencesFromResource(R.xml.root_preferences)

            //get the shared preference

            //Get the sharedPreferences
            mPreferences = preferenceScreen.sharedPreferences!!
            alreadySubscribed = mPreferences.getBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, false)

            Log.d(TAG, "Messaging: alreadySubscribed starting value: $alreadySubscribed")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)

            //summary preference
            val versionPreference: Preference? = findPreference(getString(R.string.key_pref_app_version))
            versionPreference?.summary = getVersion()

            //rating preference
            val ratePreference: Preference? = findPreference(getString(R.string.key_pref_rate_app))

            ratePreference?.let {
                rate ->
                rate.setOnPreferenceClickListener {
                    val uri: Uri = Uri.parse("market://details?id=${requireActivity().packageName}")
                    val gotoMarket = Intent(Intent.ACTION_VIEW, uri)

                    try {
                        startActivity(gotoMarket)
                    }catch (e: ActivityNotFoundException){
                        Toast.makeText(
                            activity,
                            getString(R.string.google_play_store_error),
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    true
                }
            }


            //policy preference
            val policyPreference: Preference? = findPreference(getString(R.string.key_pref_app_policy))
            policyPreference?.let {
                policy ->
                policy.setOnPreferenceClickListener {
                    val uri: Uri = Uri.parse(getString(R.string.privacy_policy_url))
                    val openPolicy = Intent(Intent.ACTION_VIEW, uri)

                    try {
                        startActivity(openPolicy)
                    }catch (e: ActivityNotFoundException) {

                        Toast.makeText(
                            activity,
                            getString(R.string.google_play_store_error),
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    true
                }
            }



        }

        override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            val preference: Preference? = p1?.let { findPreference(it) }
            preference?.let {
                if (it is SwitchPreference) {
                    if((preference as SwitchPreference).isChecked) {
                        Log.d(TAG, "Messaging: notification is turned ON")

                        Log.d(TAG, "Messaging: alreadySubscribed: $alreadySubscribed")

                        alreadySubscribed?.let {
                            if (!it) {
                                //subscribe the user to notification
                                MessagingUtils.subscribeToTopic(requireContext(), getString(R.string.messaging_topic))
                                mPreferences.edit().putBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, true).apply()

                                //Testing use case
                                alreadySubscribed = mPreferences.getBoolean(
                                    NOTIFICATION_ALREADY_SUBSCRIBED, false)

                                Log.d(TAG, "Messaging: user subscribed again, alreadySubscribed is now: $alreadySubscribed")

                            }
                        }
                    }else {
                        Log.d(TAG, "Messaging: notification is turned OFF")
                        //unsubscribe the user from notification
                        MessagingUtils.unSubscribeFromTopic(requireContext(), getString(R.string.messaging_topic))
                        mPreferences.edit().putBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, false).apply()

                        //Testing case
                        alreadySubscribed = mPreferences.getBoolean(
                            NOTIFICATION_ALREADY_SUBSCRIBED, false)

                        Log.d(TAG, "Messaging: user unsubscribed, alreadySubscribed is now: $alreadySubscribed")
                    }
                }
            }

        }


        override fun onDestroy() {
            super.onDestroy()
            preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        }


        private fun getVersion(): String {
            val pInfo: PackageInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            return pInfo.versionName
        }


    }
}