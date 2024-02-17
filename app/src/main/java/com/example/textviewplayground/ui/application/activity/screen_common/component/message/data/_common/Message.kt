package com.example.textviewplayground.ui.application.activity.screen_common.component.message.data._common

import android.graphics.drawable.Drawable

data class Message(
    val text: String? = null,
    val image: Drawable? = null
) {
    fun isNull(): Boolean {
        return (text == null && image == null)
    }
}