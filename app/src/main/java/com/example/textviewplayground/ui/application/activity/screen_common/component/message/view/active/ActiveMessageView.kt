package com.example.textviewplayground.ui.application.activity.screen_common.component.message.view.active

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.textviewplayground.R
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common.Message
import com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.TypingMaterialTextView
import com.example.textviewplayground.ui.application.activity.screen_common.component.message.view._common.MessageView
import com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.TypingMaterialTextViewCallback

class ActiveMessageView(
    context: Context, attrs: AttributeSet
) : MessageView<
    TypingMaterialTextView, AppCompatImageView, Message
>(context, attrs), TextWatcher, TypingMaterialTextViewCallback {
    override fun inflateTextView(): TypingMaterialTextView {
        return (LayoutInflater.from(context).inflate(
            R.layout.component_active_message_text, this, false) as TypingMaterialTextView)
            .apply {
                addTextChangedListener(this@ActiveMessageView)
                setCallback(this@ActiveMessageView)
            }
    }

    override fun setTextContent(text: String) {
        mTextView!!.typeText(text)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        // todo: can be optimized:
        scrollDown()
    }

    override fun setContentWithMessage(message: Message) {
        message.text?.also { setText(it) }
    }

    override fun onTextTypingFinished() {
        mMessage?.image?.also {
            setImage(it)
        }
    }
}