package com.alibaba.ams.emas.demo.ui.info

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.alibaba.ams.emas.demo.SingleLiveData
import com.alibaba.ams.emas.demo.util.Utils
import com.alibaba.ams.emas.demo.util.Utils.Companion.TAG
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    val appKey = SingleLiveData<String>().apply {
        value = ""
    }

    val appSecret = SingleLiveData<String>().apply {
        value = ""
    }

    val deviceId = SingleLiveData<String>().apply {
        value = ""
    }


    val utdid = SingleLiveData<String>().apply {
        value = ""
    }

    fun initData() {

        appKey.value = Utils.getAppMetaData(getApplication(), "com.alibaba.app.appkey")
        appSecret.value = Utils.getAppMetaData(getApplication(), "com.alibaba.app.appsecret")

        deviceId.value = PushServiceFactory.getCloudPushService().deviceId
        Log.d(TAG, "deviceId: ${deviceId.value}")
        utdid.value = PushServiceFactory.getCloudPushService().utDeviceId
        Log.d(TAG, "utdid: ${utdid.value}")
    }

}