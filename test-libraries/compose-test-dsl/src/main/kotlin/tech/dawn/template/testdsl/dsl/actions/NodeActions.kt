package tech.dawn.template.testdsl.dsl.actions

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import tech.dawn.template.testdsl.BddKeywords
import tech.dawn.template.testdsl.dsl.flakySafely

// ---------------------------------------------------------------------------
// Click / tap
// ---------------------------------------------------------------------------

infix fun BddKeywords.press(node: SemanticsNodeInteraction): SemanticsNodeInteraction =
    flakySafely { 
        node.performClick()
        node
    }

// ---------------------------------------------------------------------------
// Text input — two-step infix chain
//
//   When type "hello@example.com" into emailField
//        ^^^^                     ^^^^
// ---------------------------------------------------------------------------

/**
 * Intermediate holder for the two-step text-input chain.
 *
 *   When type "value" into emailField
 */
data class TextInput(val text: String, val keyword: BddKeywords)

/**
 * First step: captures the text to type.
 *
 *   When type "value" into emailField
 *        ^^^^
 */
infix fun BddKeywords.type(text: String): TextInput = TextInput(text, this)

/**
 * Second step: types the captured text into the target node.
 *
 *   When type "value" into emailField
 *                     ^^^^
 */
infix fun TextInput.into(node: SemanticsNodeInteraction): Unit =
    flakySafely { node.performTextInput(text) }

// ---------------------------------------------------------------------------
// Text replacement (clears first, then types)
// ---------------------------------------------------------------------------

/**
 * Intermediate holder for the two-step text-replace chain.
 *
 *   When replace "new@example.com" in emailField
 */
data class TextReplacement(val text: String, val keyword: BddKeywords)

infix fun BddKeywords.replace(text: String): TextReplacement = TextReplacement(text, this)

infix fun TextReplacement.`in`(node: SemanticsNodeInteraction): Unit =
    flakySafely { node.performTextReplacement(text) }

// ---------------------------------------------------------------------------
// Clear field
// ---------------------------------------------------------------------------

/**
 * Clears all text from the given node.
 *
 *   When clear emailField
 *        ^^^^^
 */
infix fun BddKeywords.clear(node: SemanticsNodeInteraction): Unit =
    flakySafely { node.performTextClearance() }

// ---------------------------------------------------------------------------
// Scroll
// ---------------------------------------------------------------------------

/**
 * Scrolls the given node into the visible viewport.
 *
 *   When scrollTo loginButton
 *        ^^^^^^^^
 */
infix fun BddKeywords.scrollTo(node: SemanticsNodeInteraction): SemanticsNodeInteraction =
    flakySafely { node.performScrollTo() }

// ---------------------------------------------------------------------------
// Accessibility / Focus Actions
// ---------------------------------------------------------------------------

/**
 * Requests focus for the given node.
 *
 *   When focusOn emailField
 *        ^^^^^^^
 */
infix fun BddKeywords.focusOn(node: SemanticsNodeInteraction): Unit =
    flakySafely { node.performSemanticsAction(SemanticsActions.RequestFocus) }
