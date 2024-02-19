package com.example.textviewplayground.ui.application.activity.screen_common.component.message.view.active

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message
import com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.TypingMaterialTextView
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.view._common.MessageView
import com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.TypingMaterialTextViewCallback
import com.google.android.material.imageview.ShapeableImageView

class ActiveMessageView(
    context: Context,
    attrs: AttributeSet
) : MessageView<
    TypingMaterialTextView, ShapeableImageView, Message
>(context, attrs), TypingMaterialTextViewCallback {
    companion object {
        const val TAG = "ActiveMessageView"
    }

    private var mAnimateTyping: Boolean = true

    override fun inflateTextView(): TypingMaterialTextView {
        return (LayoutInflater.from(context).inflate(
            R.layout.component_active_message_text, this, false) as TypingMaterialTextView)
            .apply {
                setCoroutineScope(mCoroutineScope!!)
                setCallback(this@ActiveMessageView)
            }
    }

    fun setMessage(message: Message, animate: Boolean) {
        mAnimateTyping = animate

        setMessage(message)
    }

    override fun setTextContent(text: String?) {
        Log.d(TAG, "setTextContent(): view = ${this.toString()}")

        if (mAnimateTyping && text != null) mTextView!!.typeText(text)
        else mTextView!!.setText(text)
    }

    override fun resetContent() {
        if (mAnimateTyping) {
            mTextView?.setText(String())
            setImage(null)

        } else super.resetContent()
    }

    override fun setContentWithMessage(message: Message) {
        if (message.text != null && mAnimateTyping) {
            message.text.also { setText(it) }
        } else super.setContentWithMessage(message)
    }

    override fun onTextTypingFinished() {
        mMessage?.image?.also {
            setImage(it)
        }
    }

    override fun onDetachedFromWindow() {
        mTextView?.stopTypingText()

        super.onDetachedFromWindow()
    }

    // TODO: looks like a chunk of shitty code. mb there is a different solution?
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mTextView?.isTyping() != true) {
            mAnimateTyping = false

            if (mTextView?.text?.toString() != mMessage?.text)
                setText(mMessage?.text)
            if (mImageView?.drawable?.toString() != mMessage?.image?.toString())
                setImage(mMessage?.image)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}