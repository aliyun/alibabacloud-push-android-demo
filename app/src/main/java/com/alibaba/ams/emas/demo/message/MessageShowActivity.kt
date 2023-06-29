package com.alibaba.ams.emas.demo.message

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.aliyun.emas.pocdemo.R
import com.aliyun.emas.pocdemo.databinding.ActivityMessageShowBinding

/**
 * @author allen.wy
 * @date 2023/5/19
 */
class MessageShowActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMessageShowBinding

    private val messageList: MutableList<CPushMessage> = mutableListOf()
    private lateinit var listAdapter: MessageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageShowBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.messageToolbar.title = getString(R.string.message_list)
        setSupportActionBar(binding.messageToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//添加默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true)

        intent?.let {
            var pushMessage: CPushMessage? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pushMessage = intent.getParcelableExtra("aliyun_message", CPushMessage::class.java)
            } else {
                val extra: Parcelable? = intent.getParcelableExtra("aliyun_message")
                extra?.let {
                    pushMessage = extra as CPushMessage
                }
            }
            pushMessage?.let {
                messageList.add(pushMessage!!)
            }
        }

        binding.messageList.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL,false)
        listAdapter = MessageListAdapter(messageList, this)
        binding.messageList.adapter = listAdapter
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            var pushMessage: CPushMessage? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pushMessage = intent.getParcelableExtra("aliyun_message", CPushMessage::class.java)
            } else {
                val extra: Parcelable? = intent.getParcelableExtra("aliyun_message")
                extra?.let {
                    pushMessage = extra as CPushMessage
                }
            }
            pushMessage?.let {
                listAdapter.addData(0, pushMessage!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}