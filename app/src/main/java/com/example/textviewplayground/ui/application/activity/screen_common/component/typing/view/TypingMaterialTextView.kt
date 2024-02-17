package com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import com.example.textviewplayground.R
import com.google.android.material.textview.MaterialTextView

class TypingMaterialTextView : MaterialTextView {
    companion object {
        const val TAG = "CharMaterialTextView"

        const val DEFAULT_ATTR_INT_VALUE = -1
        const val DEFAULT_CHAR_TYPING_DURATION = 500L
    }

    private var mCharTypingDuration: Long = DEFAULT_CHAR_TYPING_DURATION

    private var mIsTyping: Boolean = false

    private var mFullText: String = String()
    val fullText get() = mFullText

    private var mCallback: TypingMaterialTextViewCallback? = null

    constructor(
        context: Context, attrs: AttributeSet
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

    fun setCallback(callback: TypingMaterialTextViewCallback) {
        mCallback = callback
    }

    fun stopTypingText() {
        mIsTyping = false
    }

    fun setText(text: String) {
        if (mIsTyping) stopTypingText()

        mFullText = text
        this.text = text
    }

    fun typeText(text: String) {
        if (text.isEmpty()) return

        mFullText = text

        val typeRunnable = fun () {
            mIsTyping = true

            typeWithAnimation(text, 1)
        }

        if (mIsTyping) {
            stopTypingText()
            Handler(Looper.myLooper()!!).postDelayed({ typeRunnable() }, mCharTypingDuration)

            return
        }

        typeRunnable()
    }

    private fun typeWithAnimation(text: String, length: Int) {
        if (!mIsTyping) return

        this.text = text.substring(0, length)

        when (length) {
            text.length - 1 -> {
                handleTypingStop()

                return
            }
            else -> Handler(Looper.myLooper()!!).postDelayed({
                typeWithAnimation(text, length + 1)
            }, mCharTypingDuration)
        }
    }

    private fun handleTypingStop() {
        text = mFullText
        mIsTyping = false

        mCallback?.onTextTypingFinished()
    }
}