package tech.dawn.template.testdsl.dsl.android

import androidx.compose.ui.test.junit4.ComposeTestRule
import tech.dawn.template.testdsl.BddKeywords

/**
 * DSL extensions for waiting on [ComposeTestRule].
 *
 * Usage:
 * ```
 * And wait forResponse on composeTestRule
 * ```
 */

/** Sentinel object for the `wait` keyword. */
object WaitKeyword
val wait = WaitKeyword

/** Sentinel object for the `forResponse` keyword. */
object ForResponse
val forResponse = ForResponse

/**
 * First step of the wait chain.
 */
@Suppress("UnusedReceiverParameter")
infix fun BddKeywords.wait(ignored: ForResponse): WaitAction = WaitAction()

class WaitAction

/**
 * Second step of the wait chain.
 */
infix fun WaitAction.on(rule: ComposeTestRule) {
    rule.waitForIdle()
}
