package tech.dawn.template.testdsl.dsl.preconditions

/**
 * Strategy for setting up the initial system state and required context for a test scenario.
 *
 * One of the core principles of this template is that **tests should focus on the
 * scenario at hand**.
 *
 * A [Precondition] ensures that:
 * 1. **Focus**: The test doesn't need to manually navigate through or verify
 *    unrelated screens just to reach the system under test.
 * 2. **Context**: The app is set up with all required backend state (e.g., logged-in
 *    sessions, database records, feature flags).
 * 3. **Efficiency**: Setup logic is reusable and encapsulated outside the test body.
 *
 * As a consequence, if a scenario starts "on the Home screen", the [Precondition]
 * is responsible for bringing the app to that state directly, allowing the test to
 * verify only the behavior and state transitions relevant to that specific screen.
 */
class Precondition {

}
fun precondition(f: Precondition.() -> Unit) = f(Precondition())
