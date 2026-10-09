package tech.dawn.template.testdsl.annotations

/**
 * Links a test to a named scenario, providing traceability from code to documentation.
 *
 * Usage:
 *   @Scenario("LOGIN-02 - Successful login navigates to Welcome screen")
 *   fun test_login_success() { ... }
 *
 * The name should match the scenario title in the Gherkin block comment
 * at the top of the test class.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scenario(val name: String)

/**
 * Priority levels control which tests run in which CI pipeline stage.
 *
 * Priority1 → runs on every PR (fast feedback, critical paths only)
 * Priority2 → runs on merge to main / nightly (broader coverage)
 * Priority3 → runs on scheduled nightly build (full regression)
 *
 * Inspired by common risk-based testing levels.
 * Use these in your CI configuration to filter with:
 *   -Pandroid.testInstrumentationRunnerArguments.annotation=com.example.template.testdsl.annotations.Priority1
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Priority1

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Priority2

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Priority3

/**
 * Marks a test as known-flaky without deleting it.
 *
 * Flaky tests should never be silently deleted — that removes signal.
 * Mark them @Unstable with a reason and a tracking reference (ticket URL or ID).
 * CI can then optionally exclude @Unstable tests from blocking pipelines while
 * still collecting their results.
 *
 * Usage:
 *   @Unstable(reason = "Intermittent timing issue on emulators", ticket = "PROJ-1234")
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unstable(val reason: String, val ticket: String = "")

/**
 * Excludes a test from PR pipelines.
 *
 * Use for tests that are too slow or resource-intensive for fast feedback loops
 * but should still run in nightly builds.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotOnPR
