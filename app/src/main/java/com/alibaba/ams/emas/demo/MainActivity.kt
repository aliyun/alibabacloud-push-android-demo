package com.alibaba.ams.emas.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.ams.emas.demo.message.MessageShowActivity
import com.alibaba.sdk.android.push.IPushPermissionCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.aliyun.emas.pocdemo.R
import com.aliyun.emas.pocdemo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_basic, R.id.navigation_advance, R.id.navigation_info
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            PushServiceFactory.getCloudPushService()
                .requestNotificationPermission(this, 0x42, object : IPushPermissionCallback {
                    override fun onPushPermissionForbidden() {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.apply {
                            setTitle(getString(R.string.add_alias_tag))
                            setMessage(R.string.open_notification_permission)
                            setPositiveButton(R.string.ok) { dialog, _ ->
                                toOpenNotification()
                                dialog.dismiss()
                            }
                            setNegativeButton(R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }
                            show()
                        }
                    }

                    override fun onPushPermissionGranted() {
                    }

                })
        }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    var pushMessage: CPushMessage? = null
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                        pushMessage = intent.getParcelableExtra("aliyun_message", CPushMessage::class.java)
                    } else {
                        val extra: Parcelable? = intent.getParcelableExtra("aliyun_message")
                        extra?.let {
                            pushMessage = extra as CPushMessage
                        }
                    }

                    pushMessage?.let {
                        val showMessage = Intent(this@MainActivity, MessageShowActivity::class.java)
                        showMessage.putExtra("aliyun_message", pushMessage)
                        startActivity(showMessage)
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("$packageName.MESSAGE_ACTION"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    fun toOpenNotification() {
        try {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, applicationInfo.uid)
            }
            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", applicationInfo.uid)
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null);
            startActivity(intent);
        }
    }

}