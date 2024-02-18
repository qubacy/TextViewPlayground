package com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.job.TypingJobLauncher
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class TypingMaterialTextView : MaterialTextView {
    companion object {
        const val TAG = "CharMaterialTextView"

        const val DEFAULT_ATTR_INT_VALUE = -1
        const val DEFAULT_CHAR_TYPING_DURATION = 500L
    }

    private var mCoroutineScope: CoroutineScope? = null
    private var mCharTypingDuration: Long = DEFAULT_CHAR_TYPING_DURATION

    //private var mIsTyping: Boolean = false
    private var mTypingJob: Job? = null

    private var mFullText: String = String()
    val fullText get() = mFullText

    private var mCallback: TypingMaterialTextViewCallback? = null

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        initCustomAttrs(context, attrs)
    }

    private fun initCustomAttrs(
        context: Context, attrs: AttributeSet
    ) {
        val attrsTypedArray = context.obtainStyledAttributes(
            attrs,
            intArrayOf(R.attr.charTypingDuration)
        )

        attrsTypedArray.getInt(0, DEFAULT_ATTR_INT_VALUE).also {
            if (it == DEFAULT_ATTR_INT_VALUE) return@also

            mCharTypingDuration = it.toLong()
        }

        attrsTypedArray.recycle()
    }

    fun setCoroutineScope(coroutineScope: CoroutineScope) {
        mCoroutineScope = coroutineScope
    }

    fun setCallback(callback: TypingMaterialTextViewCallback) {
        mCallback = callback
    }

    fun stopTypingText() {
        //mIsTyping = false
        handleTypingStop()
    }

    fun setText(text: String) {
        if (mTypingJob != null) stopTypingText()

        mFullText = text
        this.text = text
    }

    fun typeText(text: String) {
        if (text.isEmpty()) return

        mFullText = text

        if (mTypingJob != null) stopTypingText()

        mTypingJob = TypingJobLauncher(
            text, this, mCharTypingDuration
        ) { mCallback?.onTextTypingFinished() }
            .run(mCoroutineScope!!)

//        val typeRunnable = fun () {
//            mIsTyping = true
//
//            typeWithAnimation(text, 1)
//        }
//
//        if (mIsTyping) {
//            stopTypingText()
//            Handler(Looper.myLooper()!!).postDelayed({ typeRunnable() }, mCharTypingDuration)
//
//            return
//        }
//
//        typeRunnable()
    }

//    private fun typeWithAnimation(text: String, length: Int) {
//        Log.d(TAG, "typeWithAnimation(): text = $text; mIsTyping = $mIsTyping;")
//
//        if (!mIsTyping) return
//
//        this.text = text.substring(0, length)
//
//        when (length) {
//            text.length - 1 -> {
//                handleTypingStop()
//
//                return
//            }
//            else -> Handler(Looper.myLooper()!!).postDelayed({
//                typeWithAnimation(text, length + 1)
//            }, mCharTypingDuration)
//        }
//    }

    private fun handleTypingStop() {
        text = mFullText
        //mIsTyping = false

        Log.d(TAG, "handleTypingStop(): job to cancel = ${mTypingJob.toString()}")

        mTypingJob?.cancel()

        mTypingJob = null
    }
}