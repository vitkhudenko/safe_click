package vit.khudenko.android.safe_click

import android.view.View

/**
 * @param action action to be invoked on a click event
 * @param debounceTimeoutMillis optional debounce timeout in milliseconds. If omitted, then 1 second is used.
 *
 * @throws IllegalArgumentException if [debounceTimeoutMillis] parameter is negative
 */
class SafeClickListener(
    private val action: (view: View) -> Unit,
    debounceTimeoutMillis: Long = DEFAULT_DEBOUNCE_TIMEOUT_MS,
) : DebouncingClickListener(debounceTimeoutMillis) {

    companion object {
        const val DEFAULT_DEBOUNCE_TIMEOUT_MS = 1000L // 1 sec by default
    }

    override fun onClickAction(v: View) = action.invoke(v)

}
