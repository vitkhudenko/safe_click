package vit.khudenko.android.safe_click

import android.os.SystemClock
import android.view.View

/**
 * Base class for building click listeners that use click event debouncing. Child classes should implement [onClickAction] method.
 *
 * @param debounceTimeoutMillis debounce timeout in milliseconds.
 *
 * @throws IllegalArgumentException if [debounceTimeoutMillis] parameter is negative
 */
abstract class DebouncingClickListener(
    private val debounceTimeoutMillis: Long
) : View.OnClickListener {

    init {
        require(debounceTimeoutMillis >= 0) {
            "debounceTimeoutMillis parameter can not be negative"
        }
    }

    private var lastClickTimestamp: Long = 0

    override fun onClick(v: View) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTimestamp < debounceTimeoutMillis) {
            return
        }
        lastClickTimestamp = now
        onClickAction(v)
    }

    abstract fun onClickAction(v: View)

    fun reset() {
        lastClickTimestamp = 0
    }
}
