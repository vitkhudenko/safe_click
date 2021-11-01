package vit.khudenko.android.safe_click

import android.os.SystemClock
import android.view.View
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class UnitTests {

    private var elapsedRealtimeMillis: Long = 0

    @Before
    fun setUp() {
        mockkStatic(SystemClock::class)
        every { SystemClock.elapsedRealtime() } answers {
            require(elapsedRealtimeMillis > 0)
            elapsedRealtimeMillis
        }
    }

    @After
    fun tearDown() {
        elapsedRealtimeMillis = 0
    }

    @Test
    fun `setSafeClickListener - a single click should be handled`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }

        var timesHandledClick = 0
        view.setSafeClickListener {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)

        assertEquals(1, timesHandledClick)
    }

    @Test
    fun `setSafeClickListener - second click should be handled if time passes the debounce timeout`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }

        var timesHandledClick = 0
        view.setSafeClickListener {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)
        advanceElapsedRealtime(SafeClickListener.DEFAULT_DEBOUNCE_TIMEOUT_MS)
        clickListenerSlot.captured.onClick(view)

        assertEquals(2, timesHandledClick)
    }

    @Test
    fun `setSafeClickListener - second click should not be handled if time does not pass the debounce timeout`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }

        var timesHandledClick = 0
        view.setSafeClickListener {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)
        advanceElapsedRealtime(SafeClickListener.DEFAULT_DEBOUNCE_TIMEOUT_MS.dec())
        clickListenerSlot.captured.onClick(view)

        assertEquals(1, timesHandledClick)
    }

    @Test
    fun `setSafeClickListener with explicit debounceTimeoutMillis - a single click should be handled`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }
        val debounceTimeoutMillis = 500L
        var timesHandledClick = 0
        view.setSafeClickListener(debounceTimeoutMillis) {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)

        assertEquals(1, timesHandledClick)
    }

    @Test
    fun `setSafeClickListener with explicit debounceTimeoutMillis - second click should be handled if time passes the debounce timeout`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }
        val debounceTimeoutMillis = 500L
        var timesHandledClick = 0
        view.setSafeClickListener(debounceTimeoutMillis) {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)
        advanceElapsedRealtime(debounceTimeoutMillis)
        clickListenerSlot.captured.onClick(view)

        assertEquals(2, timesHandledClick)
    }

    @Test
    fun `setSafeClickListener with explicit debounceTimeoutMillis - second click should not be handled if time does not pass the debounce timeout`() {
        val clickListenerSlot = slot<View.OnClickListener>()
        val view = mockk<View> {
            every { setOnClickListener(capture(clickListenerSlot)) } just Runs
        }
        val debounceTimeoutMillis = 500L
        var timesHandledClick = 0
        view.setSafeClickListener(debounceTimeoutMillis) {
            timesHandledClick += 1
        }

        setupElapsedRealtime()
        clickListenerSlot.captured.onClick(view)
        advanceElapsedRealtime(debounceTimeoutMillis.dec())
        clickListenerSlot.captured.onClick(view)

        assertEquals(1, timesHandledClick)
    }

    @Test
    fun `SafeClickListener - second click should be handled if listener has been reset even if time does not pass the debounce timeout`() {
        var timesHandledClick = 0
        val listener = SafeClickListener(action = { timesHandledClick += 1 })

        setupElapsedRealtime()
        listener.onClick(mockk())
        advanceElapsedRealtime(SafeClickListener.DEFAULT_DEBOUNCE_TIMEOUT_MS.dec())
        listener.reset()
        listener.onClick(mockk())

        assertEquals(2, timesHandledClick)
    }

    @Test
    fun `negative debounceTimeoutMillis should cause validation exception`() {
        val view = mockk<View>()
        assertThrows(
            "debounceTimeoutMillis parameter can not be negative",
            IllegalArgumentException::class.java
        ) {
            view.setSafeClickListener(debounceTimeoutMillis = -1) {}
        }
    }

    private fun setupElapsedRealtime() {
        elapsedRealtimeMillis = System.currentTimeMillis()
    }

    private fun advanceElapsedRealtime(advanceMillis: Long) {
        elapsedRealtimeMillis += advanceMillis
    }
}