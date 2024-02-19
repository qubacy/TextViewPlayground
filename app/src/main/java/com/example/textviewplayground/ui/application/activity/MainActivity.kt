package com.example.textviewplayground.ui.application.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.textviewplayground.R
import com.example.textviewplayground.databinding.ActivityMainBinding
import com.example.textviewplayground.ui.application.activity.adapter.MessageListAdapter
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message

class MainActivity : AppCompatActivity() {
    @StringRes
    private var mLastMessageId: Int = 0

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MessageListAdapter
    private lateinit var mMessages: Array<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        mAdapter = MessageListAdapter(lifecycleScope)

        mBinding.messageList.apply {
            adapter = mAdapter
        }
        mBinding.buttonChange.setOnClickListener { onChangeClicked() }

        initMessages()
    }

    private fun initMessages() {
        mMessages = arrayOf(
            Message(getString(R.string.test_text_1), null),
            Message(getString(R.string.test_text_2),
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, theme)),
            Message(getString(R.string.test_text_3), null),
            Message(null,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, theme)),
            Message(getString(R.string.test_text_3),
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, theme))
        )
    }

    override fun onStart() {
        super.onStart()

        mAdapter.setItems(mMessages.asList())
    }

    private fun onChangeClicked() {
        mLastMessageId = getNextMessageTextResId(mLastMessageId)

        val message = mMessages[mLastMessageId]

        mAdapter.addItem(message)
        mBinding.messageList.scrollToPosition(0)
    }

    private fun getNextMessageTextResId(currentMessageId: Int): Int {
        return (currentMessageId + 1) % mMessages.size
    }
}