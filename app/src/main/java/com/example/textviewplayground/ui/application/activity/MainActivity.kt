package com.example.textviewplayground.ui.application.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import com.example.textviewplayground.R
import com.example.textviewplayground.databinding.ActivityMainBinding
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message

class MainActivity : AppCompatActivity() {
    companion object {
        const val DEFAULT_ACTIVE_MESSAGE_PERCENT = 0.3f
        const val DEFAULT_PREV_MESSAGE_PERCENT = 0.2f
    }

    @StringRes
    private var mLastMessageId: Int = 0

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mMessages: Array<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        mBinding.root.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setMessagesMaxHeightInPercent(
                        DEFAULT_ACTIVE_MESSAGE_PERCENT, DEFAULT_PREV_MESSAGE_PERCENT
                    )
                }
            }
        )
        mBinding.buttonChange.setOnClickListener { onChangeClicked() }

        initMessages()
    }

    private fun initMessages() {
        mMessages = arrayOf(
            Message(getString(R.string.test_text_1), null),
            Message(getString(R.string.test_text_2),
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_background, theme)),
            Message(getString(R.string.test_text_3), null),
        )
    }

    private fun onChangeClicked() {
        mBinding.activeMessage.root.getMessage()?.also {
            setPrevMessage(it)
        }

        mLastMessageId = getNextMessageTextResId(mLastMessageId)

        setActiveMessage(mMessages[mLastMessageId])
    }

    private fun getNextMessageTextResId(currentMessageId: Int): Int {
        return (currentMessageId + 1) % mMessages.size
    }

    private fun setActiveMessage(message: Message) {
        mBinding.activeMessage.root.setMessage(message)
    }

    private fun setPrevMessage(message: Message) {
        if (mBinding.prevMessage.root.visibility == View.GONE)
            mBinding.prevMessage.root.visibility = View.VISIBLE

        mBinding.prevMessage.root.setMessage(message)
    }

    private fun setMessagesMaxHeightInPercent(
        activeMessagePercent: Float, prevMessagePercent: Float
    ) {
        val constraintSet = ConstraintSet().apply {
            clone(mBinding.root)
        }

        val screenContentHeight = mBinding.root.height -
                mBinding.root.paddingTop - mBinding.root.paddingBottom -
                mBinding.prevMessage.root.marginBottom

        val prevMessageMaxHeight = (screenContentHeight * prevMessagePercent).toInt()
        val activeMessageMaxHeight = (screenContentHeight * activeMessagePercent).toInt()

        constraintSet.constrainMaxHeight(R.id.prev_message, prevMessageMaxHeight)
        constraintSet.constrainMaxHeight(R.id.active_message, activeMessageMaxHeight)

        constraintSet.applyTo(mBinding.root)
    }

    override fun onStart() {
        super.onStart()

        setActiveMessage(mMessages[mLastMessageId])
    }
}