//package org.me.awa.ui.android.robot.level01
//
//import android.Manifest
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsEnabled
//import androidx.compose.ui.test.assertIsNotEnabled
//import androidx.compose.ui.test.junit4.v2.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.rule.GrantPermissionRule
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.me.awa.ui.permission.LocationPermissionScreen
//import org.me.awa.ui.robots.LocationPermissionRobot
//import org.me.awa.ui.theme.AwaTheme
//
///**
// * Instrumentation test for LocationPermissionScreen when fine (precise) location permission is granted.
// * Uses GrantPermissionRule to grant FINE and COARSE location permissions before test execution.
// */
//@RunWith(AndroidJUnit4::class)
//class LocationPermissionFineGrantedTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun givenFineAndCoarsePermissionsGranted_robotDisplaysBothGrantedAndUpgradeButtonDisabled() {
//        GrantPermissionRule.grant(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//        composeTestRule.setContent {
//            AwaTheme {
//                LocationPermissionScreen(onBackClick = {})
//            }
//        }
//
//        val robot = LocationPermissionRobot(composeTestRule)
//
//        // Verify title
//        robot.title.assertIsDisplayed()
//
//        // Both coarse and fine statuses show GRANTED
//        composeTestRule.onNodeWithText("• Foreground Coarse (Approximate): GRANTED")
//            .assertIsDisplayed()
//        composeTestRule.onNodeWithText("• Foreground Fine (Precise / High Accuracy): GRANTED")
//            .assertIsDisplayed()
//
//        // Request button shows "Foreground Precise Granted" and is disabled
//        composeTestRule.onNodeWithText("Foreground Precise Granted")
//            .assertIsDisplayed()
//            .assertIsNotEnabled()
//    }
//
//    @Test
//    fun givenOnlyCoarsePermissionGranted_robotDisplaysCoarseGrantedFineDeniedAndUpgradeButtonEnabled() {
//        GrantPermissionRule.grant(
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        composeTestRule.setContent {
//            AwaTheme {
//                LocationPermissionScreen(onBackClick = {})
//            }
//        }
//
//        val robot = LocationPermissionRobot(composeTestRule)
//
//        // Verify title
//        robot.title.assertIsDisplayed()
//
//        // Coarse status shows GRANTED
//        composeTestRule.onNodeWithText("• Foreground Coarse (Approximate): GRANTED")
//            .assertIsDisplayed()
//
//        // Fine status shows DENIED
//        composeTestRule.onNodeWithText("• Foreground Fine (Precise / High Accuracy): DENIED")
//            .assertIsDisplayed()
//
//        // Upgrade to High Accuracy button is enabled when coarse is granted but fine is not
//        robot.upgradeHighAccuracyButton.assertIsDisplayed().assertIsEnabled()
//    }
//
//}
