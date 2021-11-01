package vit.khudenko.android.safe_click

import android.view.View

/**
 * Sets a specially crafted click listener, which skips click events that are followed by
 * the newer click events within the 1 second timeout.
 *
 * @param action action to be invoked on a click event
 */
fun View.setSafeClickListener(action: (view: View) -> Unit) = this.setOnClickListener(SafeClickListener(action))

/**
 * Sets a specially crafted click listener, which skips click events that are followed by
 * the newer click events within the given timeout [debounceTimeoutMillis].
 *
 * @param debounceTimeoutMillis debounce timeout in milliseconds
 * @param action action to be invoked on a click event
 *
 * @throws IllegalArgumentException if [debounceTimeoutMillis] parameter is negative
 */
fun View.setSafeClickListener(debounceTimeoutMillis: Long, action: (view: View) -> Unit) = this.setOnClickListener(
    SafeClickListener(action, debounceTimeoutMillis)
)
