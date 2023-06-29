package com.alibaba.ams.emas.demo.ui.basic

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.ams.emas.demo.AliyunPushApp
import com.alibaba.ams.emas.demo.DemoPushIntentService
import com.alibaba.ams.emas.demo.ui.IShowDialog
import com.alibaba.ams.emas.demo.SingleLiveData
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.tool.NetworkUtils
import com.aliyun.emas.pocdemo.R
import kotlinx.coroutines.launch

class BasicViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private var preferences: SharedPreferences =
        getApplication<AliyunPushApp>().getSharedPreferences(
            "aliyun_push",
            Context.MODE_PRIVATE
        )

    private var cloudPushService: CloudPushService = PushServiceFactory.getCloudPushService()

    val pushChannelOpenedData = MutableLiveData<Boolean>().apply {
        value = false
    }

    var showInGroup = false

    var useService = false

    val logLevel = SingleLiveData<String>().apply {
        value = "DEBUG"
    }

    var showDialogCallback: IBasicShowDialog? = null

    fun initData() {
        viewModelScope.launch {
            val isShowInGroup = preferences.getBoolean("showInGroup", false)
            showInGroup = isShowInGroup
            cloudPushService.setNotificationShowInGroup(isShowInGroup)

            val isUseService = preferences.getBoolean("useService", false)
            useService = isUseService
            cloudPushService.setPushIntentService(if (isUseService) DemoPushIntentService::class.java else null)


            val level = preferences.getInt("logLevel", -1)
            logLevel.value = when (level) {
                0 -> "ERROR"
                1 -> "INFO"
                2 -> "DEBUG"
                else -> "OFF"
            }
            PushServiceFactory.getCloudPushService().setLogLevel(level)
        }

    }

    fun checkPushStatus() {
        if (!NetworkUtils.isNetworkConnected(getApplication())) {
            return
        }
        cloudPushService.checkPushChannelStatus(object : CommonCallback {
            override fun onSuccess(response: String?) {
                pushChannelOpenedData.value = true
            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
                pushChannelOpenedData.value = false
            }
        })
    }


    fun registerPush() {
        if (!NetworkUtils.isNetworkConnected(getApplication())) {
            toast(getString(R.string.network_not_connect))
            return
        }
        cloudPushService.register(getApplication(), object : CommonCallback {
            override fun onSuccess(response: String?) {
                Toast.makeText(
                    getApplication(),
                    getApplication<AliyunPushApp>().getString(R.string.register_success),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
                val message = getString(
                    R.string.common_error,
                    getString(R.string.push_register),
                    "$errorCode-$errorMessage"
                )
                showDialogCallback?.showErrorDialog(message)
            }
        })
    }


    fun openPush() {
        if (!NetworkUtils.isNetworkConnected(getApplication())) {
            toast(getString(R.string.network_not_connect))
            return
        }
        cloudPushService.turnOnPushChannel(object : CommonCallback {
            override fun onSuccess(response: String?) {
                pushChannelOpenedData.value = true
                Toast.makeText(
                    getApplication(),
                    R.string.push_channel_has_opened,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
                pushChannelOpenedData.value = false
                val message = getString(
                    R.string.common_error,
                    getString(R.string.open_push),
                    "$errorCode-$errorMessage"
                )
                showDialogCallback?.showErrorDialog(message)
            }

        })
    }

    fun closePush() {
        if (!NetworkUtils.isNetworkConnected(getApplication())) {
            toast(getString(R.string.network_not_connect))
            return
        }
        cloudPushService.turnOffPushChannel(object : CommonCallback {
            override fun onSuccess(response: String?) {
                pushChannelOpenedData.value = false
                Toast.makeText(
                    getApplication(),
                    R.string.push_channel_has_closed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
                pushChannelOpenedData.value = true
                val message = getString(
                    R.string.common_error,
                    getString(R.string.close_push),
                    "$errorCode-$errorMessage"
                )
                showDialogCallback?.showErrorDialog(message)
            }
        })
    }


    fun toggleShowInGroup(buttonView: CompoundButton, checked: Boolean) {
        showInGroup = checked
        cloudPushService.setNotificationShowInGroup(checked)
        viewModelScope.launch {
            val editor = preferences.edit()
            editor.putBoolean("showInGroup", checked)
            editor.apply()
        }
    }

    fun clearNotifications() {
        cloudPushService.clearNotifications()
    }

    fun toggleUseService(buttonView: CompoundButton, checked: Boolean) {
        useService = checked
        val service = if (checked) DemoPushIntentService::class.java else null
        cloudPushService.setPushIntentService(service)
        viewModelScope.launch {
            val editor = preferences.edit()
            editor.putBoolean("useService", checked)
            editor.apply()
        }
    }

    fun setLogLevel() {
        val index = when (val level = preferences.getInt("logLevel", -1)) {
            -1 -> 3
            else -> level
        }
        showDialogCallback?.showSetLogDialog(index)
    }

    fun saveLogLevel(level: Int) {
        logLevel.value = when(level) {
            CloudPushService.LOG_DEBUG -> "DEBUG"
            CloudPushService.LOG_INFO -> "INFO"
            CloudPushService.LOG_ERROR -> "ERROR"
            else -> "OFF"
        }
        PushServiceFactory.getCloudPushService().setLogLevel(level)
        viewModelScope.launch {
            val editor = preferences.edit()
            editor.putInt("logLevel", level)
            editor.apply()
        }

    }

    private fun toast(message: String) {
        val toast = Toast.makeText(
            getApplication(),
            message,
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun getString(resId: Int): String {
        return getApplication<AliyunPushApp>().getString(resId);
    }

    private fun getString(resId: Int, vararg args: String): String {
        return getApplication<AliyunPushApp>().getString(resId, *args)
    }

}