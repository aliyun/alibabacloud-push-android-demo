package com.alibaba.ams.emas.demo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.alibaba.sdk.android.push.AndroidPopupActivity
import com.aliyun.emas.pocdemo.R

/**
 * @author allen.wy
 * @date 2023/5/19
 */
class ThirdPushPopupActivity: AndroidPopupActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_third_push_popup)
    }
    override fun onSysNoticeOpened(title: String?, summary: String?, extraMap: MutableMap<String, String>?) {

    }

    override fun onNotPushData(intent: Intent?) {
        super.onNotPushData(intent)
    }

    override fun onParseFailed(intent: Intent?) {
        super.onParseFailed(intent)
    }
}