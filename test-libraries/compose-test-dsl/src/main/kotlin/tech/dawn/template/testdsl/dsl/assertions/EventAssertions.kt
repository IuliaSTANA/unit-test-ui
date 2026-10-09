package tech.dawn.template.testdsl.dsl.assertions

import tech.dawn.template.testdsl.CallCheck
import tech.dawn.template.testdsl.BddKeywords

/**
 * DSL extension to assert on [CallCheck] events.
 *
 * Usage:
 * ```
 * Then event myCallback was called
 * ```
 */
infix fun BddKeywords.event(check: CallCheck): CallCheck = check

/**
 * The second part of the `was called` chain.
 */
infix fun CallCheck.was(ignored: Called): Unit {
    assert(this.wasCalled) { "Expected event to be triggered, but it wasn't." }
}

/** Sentinel object for the `was called` DSL. */
object Called
val called = Called
