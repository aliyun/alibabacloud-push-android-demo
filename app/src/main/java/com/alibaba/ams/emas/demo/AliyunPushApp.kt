package com.alibaba.ams.emas.demo

import android.app.*
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import com.alibaba.sdk.android.push.HonorRegister
import com.alibaba.sdk.android.push.PushControlService
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.register.*
import com.aliyun.emas.pocdemo.BuildConfig
import com.aliyun.emas.pocdemo.R

/**
 * @author wangyun
 * @date 2023/5/12
 */
class AliyunPushApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PushServiceFactory.init(this)

        //创建渠道
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val group = NotificationChannelGroup("aliyunGroup", "aliyunChannelGroup")
            notificationManager.createNotificationChannelGroup(group)

            val channel = NotificationChannel(
                "aliyun_push_demo",
                "aliyunChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Aliyun Notification Description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(
                Uri.parse("android.resource://" + packageName + "/" + R.raw.alicloud_notification_sound),
                Notification.AUDIO_ATTRIBUTES_DEFAULT
            )
            channel.group = "aliyunGroup"
            notificationManager.createNotificationChannel(channel)
        }

        PushServiceFactory.getPushControlService().setConnectionChangeListener(object:
            PushControlService.ConnectionChangeListener {
            override fun onConnect() {
                Log.d("AliyunPush", "Aliyun Push connected")
            }

            override fun onDisconnect(code: String?, msg: String?) {
                Log.d("AliyunPush", "Aliyun Push disconnect, errorCode: $code, msg: $msg")
            }

        })

        initThirdPush()
    }

    private fun initThirdPush() {
        HuaWeiRegister.register(this)
        HonorRegister.register(this)
        OppoRegister.registerAsync(this, BuildConfig.OPPO_APP_KEY, BuildConfig.OPPO_APP_SECRET)
        VivoRegister.registerAsync(this)
        MeizuRegister.registerAsync(this, BuildConfig.MEIZU_APP_ID, BuildConfig.MEIZU_APP_KEY)
        MiPushRegister.register(this, BuildConfig.MIPUSH_ID, BuildConfig.MIPUSH_KEY)
        GcmRegister.register(
            this,
            BuildConfig.GCM_SEND_ID,
            BuildConfig.GCM_APPLICATION_ID,
            BuildConfig.GCM_PROJECT_ID,
            BuildConfig.GCM_API_KEY
        )
    }
}