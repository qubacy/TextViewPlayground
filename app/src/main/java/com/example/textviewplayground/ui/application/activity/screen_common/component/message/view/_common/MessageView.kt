package com.example.textviewplayground.ui.application.activity.screen_common.component.message.view._common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope

open class MessageView<
    TextViewType : MaterialTextView, ImageViewType : AppCompatImageView, MessageType: Message
>(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {
    interface ElementType {
        val id: Int
    }

    enum class StandardElementType(override val id: Int) : ElementType {
        TEXT(0), IMAGE(1);
    }

    companion object {
        const val TAG = "MessageView"
    }

    protected var mCoroutineScope: CoroutineScope? = null

    protected var mTextView: TextViewType? = null
    protected var mImageView: ImageViewType? = null

    protected var mMessage: MessageType? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun setCoroutineScope(coroutineScope: CoroutineScope) {
        mCoroutineScope = coroutineScope
    }

    protected open fun setImage(image: Drawable?) {
        if (mImageView == null) initImageView()

        mImageView!!.setImageDrawable(image)
    }

    protected fun initImageView() {
        mImageView = inflateImageView()

        addElementView(mImageView!!, StandardElementType.IMAGE)
    }

    protected open fun inflateImageView(): ImageViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_message_image, this, false) as ImageViewType
    }

    fun setMessage(message: MessageType) {
        if (message.isNull()) throw IllegalArgumentException()

        mMessage = message

        resetContent()
        setContentWithMessage(message)
    }

    /**
     * The order of calling set...() methods matters!
     */
    protected open fun setContentWithMessage(message: MessageType) {
        message.text?.also { setText(it) } ?: setText(String())
        setImage(message.image)
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

        addElementView(mTextView!!, StandardElementType.TEXT)
    }

    protected open fun inflateTextView(): TextViewType {
        return LayoutInflater.from(context).inflate(
            R.layout.component_prev_message_text, this, false) as TextViewType
    }

    /**
     * A return value designates a processing success status;
     */
    protected open fun addElementView(elementView: View, elementType: ElementType): Boolean {
        when (elementType) {
            StandardElementType.TEXT -> addTextView(elementView as TextViewType)
            StandardElementType.IMAGE -> addImageView(elementView as ImageViewType)
            else -> return false
        }

        return true
    }

    protected fun addTextView(textView: TextViewType) {
        if (mImageView != null) removeViewAt(0)

        addView(textView, 0)

        if (mImageView != null) addImageView(mImageView!!)
    }

    protected fun addImageView(imageView: ImageViewType) {
        val viewIndex = if (mTextView != null) 1 else 0

        addView(imageView, viewIndex)
    }
}