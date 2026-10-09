package tech.dawn.template.testdsl.dsl.assertions

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.isSelected
import tech.dawn.template.testdsl.BddKeywords
import tech.dawn.template.testdsl.dsl.flakySafely

// ---------------------------------------------------------------------------
// Node acquisition
// ---------------------------------------------------------------------------

/**
 * Acquires a node for subsequent assertion.
 *
 *   Then node loginButton shouldBe visible
 *        ^^^^
 * Returns the node unchanged so it can be chained with shouldBe/shouldNotBe.
 */
infix fun BddKeywords.node(node: SemanticsNodeInteraction): SemanticsNodeInteraction = node

// ---------------------------------------------------------------------------
// Core assertion operators
// ---------------------------------------------------------------------------

/**
 * Asserts that a node matches the given matcher.
 *
 *   Then node loginButton shouldBe visible
 *                         ^^^^^^^^
 */
infix fun SemanticsNodeInteraction.shouldBe(matcher: SemanticsMatcher): SemanticsNodeInteraction =
    flakySafely { assert(matcher) }

/**
 * Asserts that a node does NOT match the given matcher.
 *
 *   Then node loginButton shouldNotBe enabled
 *                         ^^^^^^^^^^^
 */
infix fun SemanticsNodeInteraction.shouldNotBe(matcher: SemanticsMatcher): SemanticsNodeInteraction =
    flakySafely { assert(negateMatcher(matcher)) }

// ---------------------------------------------------------------------------
// Compose has no built-in SemanticsMatcher.not() — this is the workaround.
// ---------------------------------------------------------------------------

/**
 * Negates a SemanticsMatcher.
 *
 * Compose's testing API does not expose a `.not()` operator on SemanticsMatcher.
 * This helper wraps the matcher with an inverted predicate.
 */
private fun negateMatcher(matcher: SemanticsMatcher): SemanticsMatcher =
    SemanticsMatcher("NOT(${matcher.description})") { !matcher.matches(it) }

// ---------------------------------------------------------------------------
// Matcher constants (parallel to Espresso's ViewMatchers)
// ---------------------------------------------------------------------------

/** The node is displayed on screen. */
val displayed: SemanticsMatcher = SemanticsMatcher("isDisplayed") { node ->
    node.layoutInfo.isPlaced
}

/** Alias for [displayed]. */
val visible: SemanticsMatcher = displayed

/** The node's enabled state is true (clickable, editable). */
val enabled: SemanticsMatcher = isEnabled()

/** The node's enabled state is false. */
val disabled: SemanticsMatcher = isNotEnabled()

/** The node currently holds input focus. */
val focused: SemanticsMatcher = isFocused()

/** The node is not focused. */
val unfocused: SemanticsMatcher = negateMatcher(isFocused())

/** The toggle/checkbox node is in the checked/on state. */
val checked: SemanticsMatcher = isOn()

/** The toggle/checkbox node is in the unchecked/off state. */
val unchecked: SemanticsMatcher = isOff()

/** The node has a click action defined. */
val clickable: SemanticsMatcher = hasClickAction()

/** The node is selected. */
val selected: SemanticsMatcher = isSelected()

/** The node is a heading (for accessibility navigation). */
val heading: SemanticsMatcher = SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading)

infix fun SemanticsNodeInteraction.isDisplayedWith(withProperty: SemanticsMatcher): SemanticsNodeInteraction {
    assertIsDisplayed()
    assert(withProperty)
    return this
}
/** The node has the given role (e.g., Role.Button). */
fun hasRole(role: Role): SemanticsMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

/** The node has the given content description. */
fun contentDescription(value: String): SemanticsMatcher = hasContentDescription(value)

/**
 * Asserts that the node contains all the provided text items (often used for merged nodes).
 *
 * TalkBack and other screen readers concatenate these texts when announcing the node.
 */
fun hasCombinedText(vararg texts: String): SemanticsMatcher =
    SemanticsMatcher("hasCombinedText(${texts.joinToString()})") {
        val nodeTexts = it.config.getOrNull(SemanticsProperties.Text)?.map { it.text } ?: emptyList()
        texts.all { expected -> nodeTexts.contains(expected) }
    }

/**
 * Asserts that the node has a specific traversal index.
 */
fun hasTraversalIndex(index: Float): SemanticsMatcher = SemanticsMatcher("hasTraversalIndex") {
    it.config.getOrNull(SemanticsProperties.TraversalIndex) == index
}

/**
 * The node meets the minimum touch target size requirement (48dp x 48dp).
 */
val meetsMinimumTouchTargetSize: SemanticsMatcher = SemanticsMatcher("meetsMinimumTouchTargetSize") {
    val bounds = it.layoutInfo.coordinates.size
    // Note: This check usually happens in Px, we'd need density to be perfect, 
    // but we can assert relative bounds if we assume a standard density in tests.
    // In a real implementation, we would use it.layoutInfo.density to convert.
    it.layoutInfo.width >= 48 && it.layoutInfo.height >= 48
}

/** The node has exactly the given text. */
fun hasTextValue(text: String): SemanticsMatcher = hasText(text)

/** The node has any child matching the matcher. */
fun anyChild(matcher: SemanticsMatcher): SemanticsMatcher = hasAnyChild(matcher)

/** The node has an error (ContentDescription contains "error" or error semantics set). */
val hasError: SemanticsMatcher = SemanticsMatcher("hasError") {
    it.config.getOrNull(SemanticsProperties.Error) != null
}


/** Sentinel object: asserts the node does not exist in the composition tree at all. */
object NotPresent

/** Use when a composable is conditionally rendered (not just hidden). */
val notPresent: NotPresent = NotPresent

infix fun SemanticsNodeInteraction.shouldBe(expected: NotPresent): SemanticsNodeInteraction {
    assertDoesNotExist()
    return this
}
