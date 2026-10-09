package tech.dawn.template.testdsl.dsl.assertions

import androidx.compose.ui.test.SemanticsNodeInteraction
import tech.dawn.template.testdsl.util.TraversalDebugHelper

/**
 * Extension to print the traversal order from within the test DSL.
 *
 * Example:
 * Then node root printTraversalOrder
 */
val SemanticsNodeInteraction.printTraversalOrder: SemanticsNodeInteraction
    get() {
        TraversalDebugHelper.printTraversalOrder(this)
        return this
    }
