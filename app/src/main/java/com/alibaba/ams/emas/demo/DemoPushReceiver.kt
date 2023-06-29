package com.alibaba.ams.emas.demo

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage

/**
 * @author allen.wy
 * @date 2023/5/19
 */
class DemoPushReceiver: MessageReceiver() {
    override fun onNotificationOpened(context: Context?, title: String?, summary: String?, extraMap: String?) {
    }

    override fun onNotificationRemoved(context: Context?, messageId: String?) {
    }

    override fun onNotification(
        context: Context?,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?
    ) {
    }

    override fun onMessage(context: Context?, pushMessage: CPushMessage?) {
        Log.d(TAG, "receive Message: title:${pushMessage?.title}, content:${pushMessage?.content}, " +
                "messageId:${pushMessage?.messageId}, traceInfo:${pushMessage?.traceInfo}")
        val intent = Intent("${context?.packageName}.MESSAGE_ACTION")
        intent.putExtra("aliyun_message", pushMessage)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
    }

    override fun onNotificationClickedWithNoAction(
        context: Context?,
        title: String?,
        summary: String?,
        extraMap: String?
    ) {
    }

    override fun onNotificationReceivedInApp(
        context: Context?,
        title: String?,
        summary: String?,
        extraMap: MutableMap<String, String>?,
        openType: Int,
        openActivity: String?,
        openUrl: String?
    ) {
    }
}