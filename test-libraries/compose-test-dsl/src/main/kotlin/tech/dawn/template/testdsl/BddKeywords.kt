package tech.dawn.template.testdsl

/**
 * BDD keyword objects used as receivers for the infix DSL.
 *
 * Inspired by an Espresso DSL's sealed class approach.
 * Each keyword object reads like an English conjunction, making test steps
 * feel like natural-language descriptions:
 *
 *   Then node loginButton shouldBe visible
 *   When press loginButton
 *   And node emailField shouldNotBe enabled
 *   Given node welcomeTitle shouldBe visible
 *
 * The sealed class ensures only these four keywords exist, preventing
 * accidental misuse and keeping the DSL surface small.
 */
sealed class BddKeywords

/** Asserts the current state of the system. */
object Then : BddKeywords()

/** Describes a user action. */
object When : BddKeywords()

/** Chains additional steps after a Given, When, or Then. */
object And : BddKeywords()

/** Describes preconditions or initial context. */
object Given : BddKeywords()

/**
 * Groups multiple DSL calls into a named step for better logging and readability.
 *
 *   step("Log in with valid credentials") {
 *       When type "user@example.com" into emailField
 *       And  type "password"         into passwordField
 *       When press loginButton
 *   }
 */
fun step(name: String, block: () -> Unit) {
    println("STEP: $name")
    block()
}
