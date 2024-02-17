package com.example.textviewplayground.ui.application.activity.screen_common.component.message.view._common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.NestedScrollView
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message
import com.google.android.material.textview.MaterialTextView

open class MessageView<
    TextViewType : MaterialTextView, ImageViewType : AppCompatImageView, MessageType: Message
>(
    context: Context, attrs: AttributeSet
) : NestedScrollView(context, attrs) {
    protected var mContentWrapper: ViewGroup? = null

    protected var mTextView: TextViewType? = null
    protected var mImageView: ImageViewType? = null

    protected var mMessage: MessageType? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        initContentWrapper()
    }

    protected fun initContentWrapper() {
        mContentWrapper = inflateContentWrapper()

        addView(mContentWrapper, 0)
    }

    protected fun inflateContentWrapper(): ViewGroup {
        return LayoutInflater.from(context).inflate(
            R.layout.component_message_content_wrapper, this, false) as ViewGroup
    }

    protected open fun setImage(image: Drawable) {
        if (mImageView == null) initImageView()

        mImageView!!.setImageDrawable(image)
        scrollDown()
    }

    protected fun initImageView() {
        mImageView = inflateImageView()

        mContentWrapper!!.addView(mImageView, 1)
    }

    protected open fun inflateImageView(): ImageViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_message_image, this, false) as ImageViewType
    }

    fun setMessage(message: MessageType) {
        if (message.isNull()) throw IllegalArgumentException()

        mMessage = message

        setContentWithMessage(message)
    }

    protected open fun setContentWithMessage(message: MessageType) {
        resetContent()

        message.text?.also { setText(it) }
        message.image?.also { setImage(it) }
    }

    protected fun resetContent() {
        mTextView?.text = String()
        mImageView?.setImageDrawable(null)
    }

    protected fun setText(text: String) {
        if (mTextView == null) initTextView()

        setTextContent(text)
    }

    fun getMessage(): MessageType? {
        return mMessage
    }

    protected open fun setTextContent(text: String) {
        mTextView!!.text = text
    }

    protected fun initTextView() {
        mTextView = inflateTextView()

        mContentWrapper!!.addView(mTextView, 0)
    }

    protected open fun inflateTextView(): TextViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_prev_message_text, this, false) as TextViewType
    }

    protected fun scrollDown() {
        fullScroll(View.FOCUS_DOWN)
    }
}