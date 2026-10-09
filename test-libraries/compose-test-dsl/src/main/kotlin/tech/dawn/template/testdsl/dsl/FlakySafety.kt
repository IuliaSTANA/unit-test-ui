package tech.dawn.template.testdsl.dsl

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * Executes a block of code repeatedly until it succeeds or a timeout is reached.
 *
 * This is used to wrap assertions and actions that might fail due to asynchronous
 * UI updates or animations (flakiness).
 *
 * @param timeout The maximum duration to keep retrying. Defaults to 3 seconds.
 * @param delay The interval between retries. Defaults to 100ms.
 * @param block The code to execute.
 * @return The result of the block if it eventually succeeds.
 * @throws Throwable The last error encountered if the timeout is reached.
 */
fun <T> flakySafely(
    timeout: Duration = 3.seconds,
    delay: Duration = Duration.parse("100ms"),
    block: () -> T
): T {
    val mark = TimeSource.Monotonic.markNow()
    var lastError: Throwable? = null

    while (mark.elapsedNow() < timeout) {
        try {
            return block()
        } catch (e: AssertionError) {
            lastError = e
        } catch (e: ComposeTimeoutException) {
            lastError = e
        }
        // Sleep briefly before retrying
        Thread.sleep(delay.inWholeMilliseconds)
    }

    throw lastError ?: RuntimeException("Timeout reached in flakySafely without specific error")
}

/**
 * Extension to wait until a node is displayed using Compose's native clock.
 */
fun SemanticsNodeInteraction.waitUntilVisible(rule: ComposeTestRule, timeoutMillis: Long = 3_000) {
    rule.waitUntil(timeoutMillis) {
        try {
            assertExists()
            true
        } catch (e: AssertionError) {
            false
        }
    }
}
