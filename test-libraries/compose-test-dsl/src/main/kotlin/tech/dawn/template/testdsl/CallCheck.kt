package tech.dawn.template.testdsl

/**
 * A simple utility to verify if a callback or a block of code was executed.
 *
 * Usage:
 * ```
 * val check = CallCheck()
 * someApi.setCallback { check() }
 * // ...
 * assert(check.wasCalled)
 * ```
 */
class CallCheck(var wasCalled: Boolean = false) {

    operator fun invoke() {
        wasCalled = true
    }
}