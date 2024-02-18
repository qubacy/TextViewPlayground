package com.example.textviewplayground.ui.application.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.view.active.ActiveMessageView
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.view.previous.PreviousMessageView
import kotlinx.coroutines.CoroutineScope

class MessageListAdapter(
    val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    enum class ItemType(val id: Int) {
        ACTIVE(0), PREVIOUS(1);
    }

    abstract class MessageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun setData(message: Message)
    }

    class PreviousMessageViewHolder(
        val messageView: PreviousMessageView
    ) : MessageViewHolder(messageView) {
        override fun setData(message: Message) {
            messageView.setMessage(message)
        }
    }

    class ActiveMessageViewHolder(
        val messageView: ActiveMessageView
    ) : MessageViewHolder(messageView) {
        private var mLastMessage: Message? = null

        override fun setData(message: Message) {
            messageView.setMessage(message, mLastMessage != message)

            mLastMessage = message
        }
    }

    private val mItems: MutableList<Message> = mutableListOf()
    val items: List<Message> get() = mItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            ItemType.PREVIOUS.id -> {
                val prevMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_prev_message, parent, false) as PreviousMessageView)
                    .apply { setCoroutineScope(coroutineScope) }

                PreviousMessageViewHolder(prevMessageView)
            }
            ItemType.ACTIVE.id -> {
                val activeMessageView = (LayoutInflater.from(parent.context).inflate(
                    R.layout.component_active_message, parent, false) as ActiveMessageView)
                    .apply { setCoroutineScope(coroutineScope) }

                ActiveMessageViewHolder(activeMessageView)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ItemType.ACTIVE.id else ItemType.PREVIOUS.id
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = mItems[position]

        holder.setData(message)
    }

    fun addItem(message: Message) {
        mItems.add(0, message)
        notifyItemRangeChanged(0, mItems.size - 2)
        notifyItemInserted(mItems.size - 1)
    }

    fun setItems(messages: List<Message>) {
        mItems.apply {
            clear()
            addAll(messages)
        }

        notifyDataSetChanged()
    }
}