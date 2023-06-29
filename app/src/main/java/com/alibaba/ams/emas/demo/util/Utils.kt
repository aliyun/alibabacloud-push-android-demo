package com.alibaba.ams.emas.demo.util

import android.content.Context
import android.content.pm.PackageManager

/**
 * @author allen.wy
 * @date 2023/5/16
 */
class Utils {
    companion object {
        const val TAG = "AliyunPush"

        fun getAppMetaData(context: Context, key: String): String? {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val data = applicationInfo.run {
                val hasKey = metaData?.containsKey(key)
                if (hasKey == true) metaData.get(key).toString() else null
            }
            return data
        }
    }
}