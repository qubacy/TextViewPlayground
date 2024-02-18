package com.example.textviewplayground.ui.application.activity.screen_common.component.typing.view.job

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingJobLauncher(
    private val mText: String,
    private val mTextView: TextView,
    private val mCharTypingDuration: Long,
    private val mEndAction: (() -> Unit)? = null
) {
    companion object {
        const val TAG = "TypingJobLauncher"
    }

    fun run(coroutineScope: CoroutineScope): Job {
        return coroutineScope.launch(Dispatchers.IO) {
            for (i in 1..mText.length) {
                Log.d(TAG, "typeWithAnimation(): job = ${coroutineContext.job.toString()};" +
                        " text = ${mText.substring(0, 5)}; curLength = $i;")

                withContext(Dispatchers.Main) {
                    coroutineContext.job.ensureActive()

                    mTextView.text = mText.substring(0, i)
                }

                delay(mCharTypingDuration)
            }

            // todo: is it ok to invoke it
            //  only on success?
            withContext(Dispatchers.Main) { mEndAction?.invoke() }
        }
    }
}