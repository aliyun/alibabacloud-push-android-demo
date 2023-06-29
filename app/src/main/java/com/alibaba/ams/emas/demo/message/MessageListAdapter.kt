package com.alibaba.ams.emas.demo.message

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.sdk.android.push.notification.CPushMessage
import com.aliyun.emas.pocdemo.R
import com.aliyun.emas.pocdemo.databinding.ItemMessageBinding

/**
 * @author allen.wy
 * @date 2023/5/19
 */
class MessageListAdapter(
    private val messageList: MutableList<CPushMessage>,
    private val context: Context
) :
    RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(context))
        return MessageListViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        if (messageList.isEmpty()) {
            return
        }
        holder.setMessage(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun addData(position: Int, message: CPushMessage) {
        messageList.add(position, message)
        notifyItemInserted(position)
    }

    class MessageListViewHolder(
        private val context: Context,
        private val binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setMessage(message: CPushMessage) {
            binding.msgTitle.text = context.getString(R.string.message_title, message.title)
            binding.msgContent.text = context.getString(R.string.message_content, message.content)
            binding.msgId.text = context.getString(R.string.message_msgId, message.messageId)
        }
    }
}