package com.alibaba.ams.emas.demo

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.push.AliyunMessageIntentService
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage

/**
 * @author allen.wy
 * @date 2023/5/16
 */
class DemoPushIntentService: AliyunMessageIntentService() {

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
        Log.d(
            MessageReceiver.TAG, "receive Message: title:${pushMessage?.title}, content:${pushMessage?.content}, " +
                "messageId:${pushMessage?.messageId}, traceInfo:${pushMessage?.traceInfo}")
        //消息需要展示
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
        sumamry: String?,
        extraMap: MutableMap<String, String>?,
        openType: Int,
        openActivity: String?,
        openUrl: String?
    ) {
    }
}