//package org.me.awa.ui.android.robot.level01
//
//import android.Manifest
//import androidx.activity.compose.LocalActivityResultRegistryOwner
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.v2.createComposeRule
//import androidx.compose.ui.test.performClick
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.me.awa.ui.permission.LocationPermissionScreen
//import org.me.awa.ui.robots.LocationPermissionRobot
//import org.me.awa.ui.theme.AwaTheme
//import tech.dawn.template.testdsl.dsl.android.PermissionPromptRegistry
//
///**
// * Instrumentation tests for LocationPermissionScreen when permissions are initially ungranted.
// * Uses PermissionPromptRegistry to test permission request launchers without opening system dialogs.
// */
//@RunWith(AndroidJUnit4::class)
//class LocationPermissionScreenUngrantedTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun givenInitialPermissionState_robotReflectsStatusAndDisplaysTitle() {
//        var backClicked = false
//
//        composeTestRule.setContent {
//            AwaTheme {
//                LocationPermissionScreen(onBackClick = { backClicked = true })
//            }
//        }
//
//        val robot = LocationPermissionRobot(composeTestRule)
//
//        // Verify title and initial status cards
//        robot.title.assertIsDisplayed()
//        robot.coarseStatus.assertIsDisplayed()
//        robot.fineStatus.assertIsDisplayed()
//        robot.backgroundStatus.assertIsDisplayed()
//
//        // Test back navigation
//        robot.clickBack()
//        assertTrue(backClicked)
//    }
//
//    @Test
//    fun givenPermissionLauncherTriggered_dslRegistryHandlesRequestAndUpdatesUI() {
//        val registry = PermissionPromptRegistry()
//
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registry) {
//                AwaTheme {
//                    LocationPermissionScreen(onBackClick = {})
//                }
//            }
//        }
//
//        val robot = LocationPermissionRobot(composeTestRule)
//
//        // 1. Verify fine location request is not pending initially
//        assertFalse(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))
//
//        // 2. Trigger launcher contract using Robot action
//        robot.requestForegroundButton.performClick()
//
//        // 3. Verify PermissionPromptRegistry captured pending launcher request
//        assertTrue(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))
//        assertTrue(registry.hasPending(Manifest.permission.ACCESS_COARSE_LOCATION))
//
//        // 4. Simulate user granting permission in system dialog
//        registry.respondTo(Manifest.permission.ACCESS_FINE_LOCATION, true)
//
//        // 5. Verify pending request is cleared
//        assertFalse(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))
//        assertFalse(registry.hasPending(Manifest.permission.ACCESS_COARSE_LOCATION))
//    }
//}
