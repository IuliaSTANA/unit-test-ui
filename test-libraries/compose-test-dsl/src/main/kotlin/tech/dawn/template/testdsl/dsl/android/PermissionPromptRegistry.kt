package tech.dawn.template.testdsl.dsl.android

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import tech.dawn.template.testdsl.Given
import tech.dawn.template.testdsl.Then
import tech.dawn.template.testdsl.When

/**
 * A fake [ActivityResultRegistry] and [ActivityResultRegistryOwner] designed for JVM (Robolectric) tests.
 *
 * In a headless JVM environment, there is no "System" to catch the `launch()` call and send a result back.
 * This registry solves that by allowing tests to:
 * 1. **Auto-respond**: Pre-configure a result for a specific permission (Auto-pilot).
 * 2. **Manual interaction**: Hold the request until the test explicitly grants or denies it,
 *    simulating a user interacting with a system dialog.
 *
 * Setup:
 * ```
 * val registry = PermissionPromptRegistry()
 * CompositionLocalProvider(LocalActivityResultRegistryOwner provides registry) { ... }
 * ```
 *
 * Non-DSL usage:
 * ```
 * // verify a permission request is pending
 * registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION)
 * // grant a permission:
 * registry.respondTo(Manifest.permission.ACCESS_FINE_LOCATION, true)
 * ```
 *
 * DSL Usage (When/Then - Manual):
 * ```
 * Then permissionPrompt registry shouldAskFor Manifest.permission.POST_NOTIFICATIONS
 * When permissionPrompt registry denies Manifest.permission.POST_NOTIFICATIONS
 * ```
 *
 * DSL Usage (Given - Auto-pilot):
 * ```
 * Given permission Manifest.permission.POST_NOTIFICATIONS isDeniedBy registry
 * ```
 */
class PermissionPromptRegistry : ActivityResultRegistry(), ActivityResultRegistryOwner {
    override fun getActivityResultRegistry(): ActivityResultRegistry = this

    /** Stores callbacks for requests that haven't been responded to yet. */
    private val pendingRequests = mutableMapOf<String, (Boolean) -> Unit>()

    /** Stores pre-configured responses for specific permissions. */
    private val autoResponses = mutableMapOf<String, Boolean>()

    @Suppress("UNCHECKED_CAST")
    override fun <I, O> onLaunch(
        requestCode: Int,
        contract: ActivityResultContract<I, O>,
        input: I,
        options: ActivityOptionsCompat?
    ) {
        val permissions = when (input) {
            is String -> listOf(input)
            is Array<*> -> input.filterIsInstance<String>()
            is Collection<*> -> input.filterIsInstance<String>()
            else -> emptyList()
        }

        if (permissions.isEmpty()) return

        val isMultiple = input is Array<*> || input is Collection<*>

        val allAutoResponded = permissions.all { autoResponses.containsKey(it) }

        if (allAutoResponded) {
            val resultMap = permissions.associateWith { autoResponses[it] ?: false }
            resultMap.forEach { (perm, granted) ->
                if (granted) {
                    grantSystemPermission(perm)
                    if (perm == "android.permission.ACCESS_FINE_LOCATION") {
                        grantSystemPermission("android.permission.ACCESS_COARSE_LOCATION")
                    }
                }
            }
            val result: O = if (isMultiple) {
                resultMap as O
            } else {
                (resultMap[permissions.first()] ?: false) as O
            }
            dispatchResult(requestCode, result)
        } else {
            val responseState = mutableMapOf<String, Boolean>()
            permissions.forEach { perm ->
                pendingRequests[perm] = { granted ->
                    responseState[perm] = granted
                    if (granted) {
                        grantSystemPermission(perm)
                        if (perm == "android.permission.ACCESS_FINE_LOCATION") {
                            grantSystemPermission("android.permission.ACCESS_COARSE_LOCATION")
                        }
                    }

                    // Resolve any remaining pending permissions in this launch batch
                    permissions.forEach { p ->
                        if (!responseState.containsKey(p)) {
                            val pGranted =
                                if (perm == "android.permission.ACCESS_FINE_LOCATION" && p == "android.permission.ACCESS_COARSE_LOCATION" && granted) {
                                    true
                                } else {
                                    autoResponses[p] ?: granted
                                }
                            responseState[p] = pGranted
                            if (pGranted) {
                                grantSystemPermission(p)
                            }
                            pendingRequests.remove(p)
                        }
                    }

                    val resultMap = permissions.associateWith { p -> responseState[p] ?: false }
                    val result: O = if (isMultiple) {
                        resultMap as O
                    } else {
                        (responseState[permissions.first()] ?: granted) as O
                    }
                    dispatchResult(requestCode, result)
                }
            }
        }
    }

    /** Pre-configures an automatic response for a permission. */
    fun setAutoResponse(permission: String, granted: Boolean) {
        autoResponses[permission] = granted
    }

    /** Responds to a pending request, unblocking the app logic. */
    fun respondTo(permission: String, granted: Boolean) {
        val callback = pendingRequests.remove(permission)
            ?: throw IllegalStateException("No pending request for $permission. Ensure the app has actually triggered the prompt.")
        callback(granted)
    }

    /** Checks if a permission prompt is currently "visible" (pending). */
    fun hasPending(permission: String): Boolean = pendingRequests.containsKey(permission)

    /**
     * Explicitly grant the permission via the shadowApp so that this registry covers the scenario
     * where the SUT is using `ContextCompat.checkSelfPermission` to check the permission state,
     * i.s.o. using the boolean returned directly by the callback.
     * */
    private fun grantSystemPermission(permission: String) {
        val app = try {
            androidx.test.core.app.ApplicationProvider.getApplicationContext<android.app.Application>()
        } catch (_: Throwable) {
            null
        } ?: return

        val isRobolectric = try {
            Class.forName("org.robolectric.Shadows")
            android.os.Build.FINGERPRINT == "robolectric"
        } catch (_: Throwable) {
            false
        }

        if (isRobolectric) {
            grantRobolectricPermission(app, permission)
        } else {
            try {
                val instrumentation =
                    androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
                instrumentation.uiAutomation.grantRuntimePermission(app.packageName, permission)
            } catch (_: Throwable) {
                // Instrumentation grant failed or not running under Instrumentation
            }
        }
    }

    private fun grantRobolectricPermission(app: android.app.Application, permission: String) {
        try {
            val shadowApp = org.robolectric.Shadows.shadowOf(app)
            shadowApp.grantPermissions(permission)
        } catch (_: Throwable) {
            // Robolectric grant failed
        }
    }
}

// DSL Extensions
infix fun Given.permission(permission: String) = PermissionAutoPilot(permission)

class PermissionAutoPilot(val permission: String)

infix fun PermissionAutoPilot.isDeniedBy(registry: PermissionPromptRegistry) {
    registry.setAutoResponse(permission, false)
}

infix fun PermissionAutoPilot.isGrantedBy(registry: PermissionPromptRegistry) {
    registry.setAutoResponse(permission, true)
}

infix fun Then.permissionCheck(registry: PermissionPromptRegistry) = PermissionCheck(registry)

class PermissionCheck(val registry: PermissionPromptRegistry)

infix fun PermissionCheck.shouldAskFor(permission: String) {
    assert(registry.hasPending(permission)) { "Expected request for $permission but none found" }
}

infix fun PermissionCheck.doesNotAskFor(permission: String) {
    assert(!registry.hasPending(permission)) { "Expected *no* request for $permission but one request was found" }
}

infix fun When.permissionCheck(registry: PermissionPromptRegistry) = PermissionAction(registry)

class PermissionAction(val registry: PermissionPromptRegistry)

infix fun PermissionAction.denies(permission: String) {
    registry.respondTo(permission, false)
}

infix fun PermissionAction.grants(permission: String) {
    registry.respondTo(permission, true)
}
