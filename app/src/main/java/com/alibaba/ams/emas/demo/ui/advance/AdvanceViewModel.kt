package com.alibaba.ams.emas.demo.ui.advance

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.ams.emas.demo.AliyunPushApp
import com.alibaba.ams.emas.demo.SingleLiveData
import com.alibaba.ams.emas.demo.ui.IShowDialog
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.aliyun.emas.pocdemo.R
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException

class AdvanceViewModel(application: Application) : AndroidViewModel(application) {

    private val kBoundAccountKey = "bound_account"
    private val kAccountTagListKey = "account_tag_list"
    private val kAliasTagListKey = "alias_tag_list"
    private val kBoundPhoneKey = "bound_phone"

    val preferences: SharedPreferences = getApplication<AliyunPushApp>().getSharedPreferences(
        "aliyun_push",
        Context.MODE_PRIVATE
    )

    val boundAccount = SingleLiveData<String?>()
    val aliasList = SingleLiveData<String?>()
    val deviceTagList = SingleLiveData<String?>()

    val accountTagList = SingleLiveData<MutableList<String>?>()
    val aliasTagList = SingleLiveData<MutableList<String>?>()

    val boundDeviceTag = SingleLiveData<String?>()
    val boundAccountTag = SingleLiveData<String?>()
    val boundAliasTag = SingleLiveData<String?>()
    val addedAlias = SingleLiveData<String?>()
    val boundPhone = SingleLiveData<String?>()

    var showDialogCallback: IShowDialog? = null

    private fun getDeviceTagList() {

        PushServiceFactory.getCloudPushService()
            .listTags(CloudPushService.DEVICE_TARGET, object : CommonCallback {
                override fun onSuccess(response: String?) {
                    if (TextUtils.isEmpty(response)) {
                        return
                    }
                    deviceTagList.value = response
                }

                override fun onFailed(errorCode: String?, errorMessage: String?) {
                }

            })
    }

    private fun getAliasList() {
        PushServiceFactory.getCloudPushService().listAliases(object : CommonCallback {
            override fun onSuccess(response: String?) {
                if (TextUtils.isEmpty(response)) {
                    return
                }
                aliasList.value = response
            }

            override fun onFailed(errorCode: String?, errorMessage: String?) {
            }

        })
    }

    fun initData() {
        getDeviceTagList()
        getAliasList()
        viewModelScope.launch {
            boundAccount.value = preferences.getString(kBoundAccountKey, null)

            val accountTagListStr = preferences.getString(kAccountTagListKey, null)
            accountTagList.value = accountTagListStr.toArrayList()

            val aliasTagListStr = preferences.getString(kAliasTagListKey, null)
            aliasTagList.value = aliasTagListStr.toArrayList()

            boundPhone.value = preferences.getString(kBoundPhoneKey, null)
        }
    }

    private fun String?.toArrayList(): MutableList<String> {
        if (this == null) {
            return mutableListOf()
        }
        try {
            val array = JSONArray(this)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
            return list
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun bindAccount(account: String) {
        PushServiceFactory.getCloudPushService().bindAccount(account, object : CommonCallback {
            override fun onSuccess(response: String?) {
                boundAccount.value = account

                viewModelScope.launch {
                    val editor = preferences.edit()
                    editor.putString(kBoundAccountKey, account)
                    editor.apply()
                }

            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val bindAccount = getString(R.string.bind_account)
                showDialogCallback?.showErrorDialog(
                    getString(
                        R.string.common_error,
                        bindAccount,
                        "$errorCode - $errorMsg"
                    )
                )
            }

        })
    }

    fun unbindAccount() {
        PushServiceFactory.getCloudPushService().unbindAccount(object : CommonCallback {
            override fun onSuccess(response: String?) {
                boundAccount.value = ""
                viewModelScope.launch {
                    val editor = preferences.edit()
                    editor.putString(kBoundAccountKey, null)
                    editor.apply()
                }
            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val unbindAccount = getString(R.string.unbind_account)
                showDialogCallback?.showErrorDialog(
                    getString(
                        R.string.common_error,
                        unbindAccount,
                        "$errorCode - $errorMsg"
                    )
                )
            }
        })
    }

    fun bindTag(target: Int, tag: String, alias: String?) {
        PushServiceFactory.getCloudPushService().bindTag(
            target,
            arrayOf(tag),
            alias,
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    when (target) {
                        CloudPushService.DEVICE_TARGET -> boundDeviceTag.value = tag
                        CloudPushService.ACCOUNT_TARGET -> {
                            boundAccountTag.value = tag

                            viewModelScope.launch {
                                val list = accountTagList.value
                                list?.let {
                                    it.add(tag)
                                    val array = JSONArray()
                                    repeat(it.size) { index ->
                                        array.put(list[index])
                                    }
                                    val editor = preferences.edit()
                                    editor.putString(kAccountTagListKey, array.toString())
                                    editor.apply()
                                }
                            }

                        }
                        CloudPushService.ALIAS_TARGET -> {
                            boundAliasTag.value = "$tag|$alias"
                            viewModelScope.launch {
                                val list = aliasTagList.value
                                list?.let {
                                    it.add(tag)
                                    val array = JSONArray()
                                    repeat(it.size) { index ->
                                        array.put(list[index])
                                    }
                                    val editor = preferences.edit()
                                    editor.putString(kAliasTagListKey, array.toString())
                                    editor.apply()
                                }
                            }

                        }
                    }
                }

                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    val msg = when (target) {
                        CloudPushService.ACCOUNT_TARGET -> getString(R.string.add_account_tag)
                        CloudPushService.ALIAS_TARGET -> getString(R.string.add_alias_tag)
                        else -> getString(R.string.add_device_tag)
                    }
                    showDialogCallback?.showErrorDialog(
                        getString(
                            R.string.common_error,
                            msg,
                            "$errorCode - $errorMsg"
                        )
                    )
                }

            })
    }

    fun addAlias(alias: String) {
        PushServiceFactory.getCloudPushService().addAlias(
            alias,
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    addedAlias.value = alias
                }

                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    val addAlias = getString(R.string.add_alias)
                    showDialogCallback?.showErrorDialog(
                        getString(
                            R.string.common_error,
                            addAlias,
                            "$errorCode - $errorMsg"
                        )
                    )
                }
            })
    }

    fun bindPhoneNumber(phone: String) {
        PushServiceFactory.getCloudPushService().bindPhoneNumber(phone, object : CommonCallback {
            override fun onSuccess(response: String?) {
                boundPhone.value = phone

                viewModelScope.launch {
                    val editor = preferences.edit()
                    editor.putString(kBoundPhoneKey, phone)
                    editor.apply()
                }
            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val bindPhone = getString(R.string.bind_phone)
                showDialogCallback?.showErrorDialog(
                    getString(
                        R.string.common_error,
                        bindPhone,
                        "$errorCode - $errorMsg"
                    )
                )
            }
        })
    }


    private fun getString(resId: Int, vararg args: String): String {
        return getApplication<AliyunPushApp>().getString(resId, *args)
    }

    private fun getString(resId: Int): String {
        return getApplication<AliyunPushApp>().getString(resId)
    }

    fun removeAlias(alias: String, callback: CommonCallback) {
        PushServiceFactory.getCloudPushService().removeAlias(
            alias,
            callback
        )
    }

    fun removeDeviceTag(tag: String, callback: CommonCallback) {
        PushServiceFactory.getCloudPushService().unbindTag(
            CloudPushService.DEVICE_TARGET,
            arrayOf(tag),
            null,
            callback
        )
    }

    fun removeAccountTag(tag: String, callback: CommonCallback) {
        PushServiceFactory.getCloudPushService().unbindTag(
            CloudPushService.ACCOUNT_TARGET,
            arrayOf(tag),
            null,
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    viewModelScope.launch {
                        val list = accountTagList.value
                        list?.let {
                            it.remove(tag)
                            val array = JSONArray()
                            repeat(it.size) { index ->
                                array.put(list[index])
                            }
                            val editor = preferences.edit()
                            editor.putString(kAccountTagListKey, array.toString())
                            editor.apply()
                        }
                    }

                    callback.onSuccess(response)
                }


                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    callback.onFailed(errorCode, errorMsg)
                }
            }
        )
    }

    fun removeAliasTag(tag: String, alias: String, callback: CommonCallback) {
        PushServiceFactory.getCloudPushService().unbindTag(
            CloudPushService.ALIAS_TARGET,
            arrayOf(tag),
            alias,
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    viewModelScope.launch {
                        val list = aliasTagList.value
                        list?.let {
                            it.remove(tag)
                            val array = JSONArray()
                            repeat(it.size) { index ->
                                array.put(list[index])
                            }
                            val editor = preferences.edit()
                            editor.putString(kAliasTagListKey, array.toString())
                            editor.apply()
                        }
                    }
                    callback.onSuccess(response)
                }


                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    callback.onFailed(errorCode, errorMsg)
                }
            }
        )
    }

    fun unbindPhone() {
        PushServiceFactory.getCloudPushService().unbindPhoneNumber(
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    boundPhone.value = ""
                    viewModelScope.launch {
                        val editor = preferences.edit()
                        editor.putString(kBoundPhoneKey, null)
                        editor.apply()
                    }

                }


                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    val unbindPhone = getString(R.string.unbind_phone)
                    showDialogCallback?.showErrorDialog(
                        getString(
                            R.string.common_error,
                            unbindPhone,
                            "$errorCode - $errorMsg"
                        )
                    )
                }
            }
        )
    }

}