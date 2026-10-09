//package org.me.awa.ui.android.robot.level01
//
//import android.Manifest
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.assertIsEnabled
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
// * Instrumentation test for LocationPermissionScreen when ONLY coarse (approximate) location permission is granted.
// * Uses GrantPermissionRule to grant ACCESS_COARSE_LOCATION before test execution.
// */
//@RunWith(AndroidJUnit4::class)
//class LocationPermissionCoarseGrantedTest {
//
//    @get:Rule
//    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun givenOnlyCoarsePermissionGranted_robotDisplaysCoarseGrantedFineDeniedAndUpgradeButtonEnabled() {
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
//}
