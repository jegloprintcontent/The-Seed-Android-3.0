package org.ileewe.theseedjcli.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.databinding.ActivityMainBinding
import org.ileewe.theseedjcli.messaging.MessagingUtils
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils.Companion.KEY_ACTION_LINK_TEXT
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils.Companion.KEY_ACTION_LINK_URL
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils.Companion.KEY_DISPLAY_VIEW
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils.Companion.configSettings
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils.Companion.getRemoteConfig


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var isSubscriptionEnabled: Boolean? = null
    private var alreadySubscribed: Boolean? = null
    private lateinit var sharedPreference: SharedPreferences

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        val NOTIFICATION_ALREADY_SUBSCRIBED = "already_subscribed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_the_seed, R.id.navigation_sermons, R.id.navigation_ministries, R.id.navigation_more
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Get the shared preferences
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        //Check whether subscription is enabled from preferences
        isSubscriptionEnabled = sharedPreference.getBoolean(getString(R.string.key_pref_push_notification), true)
        alreadySubscribed = sharedPreference.getBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, false)


        Log.d(TAG, "Messaging: starting values - isSubscriptionEnabled:  $isSubscriptionEnabled, alreadySubscribed: $alreadySubscribed")

        //Only subscribe user to notification if
        // isSubscriptionEnabled is TRUE & alreadySubscribed is FALSE
        //Otherwise DO NOTHING
        if (isSubscriptionEnabled as Boolean) {

            //Check that device is not already subscribed
            if (!alreadySubscribed!!) {

                //Subscribe the device
                MessagingUtils.subscribeToTopic(this, getString(R.string.messaging_topic))
                sharedPreference.edit().putBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, true).apply()
                Log.d(TAG, "Messaging: Device has been subscribed to messaging service successfully")

                //Testing case
                alreadySubscribed = sharedPreference.getBoolean(NOTIFICATION_ALREADY_SUBSCRIBED, false)
                Log.d(TAG, "Messaging:  alreadySubscribed is now: $alreadySubscribed")


            }else {
                Log.d(TAG, "Messaging: Device already subscribed to messaging service")
            }

        }else {
            Log.d(TAG, "Messaging: User has turned off messaging feature")
        }

    }


    override fun onResume() {
        super.onResume()
        //Analytics
        Analytics.recordScreenView(this@MainActivity, "Home", "Application - Home")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_setting -> {
               startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun hideBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
    }

    fun  showBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0F).duration = 300
    }



}