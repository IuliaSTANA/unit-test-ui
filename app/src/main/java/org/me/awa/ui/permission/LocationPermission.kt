package org.me.awa.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.me.awa.R
import org.me.awa.ui.theme.AwaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    // Permission states
    var hasFineLocation by remember {
        mutableStateOf(isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION))
    }
    var hasCoarseLocation by remember {
        mutableStateOf(isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION))
    }
    var hasBackgroundLocation by remember {
        mutableStateOf(isPermissionGranted(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION))
    }

    // Dialog state controllers
    var showForegroundRationale by remember { mutableStateOf(false) }
    var showHighAccuracyRationale by remember { mutableStateOf(false) }
    var showBackgroundRationale by remember { mutableStateOf(false) }
    var showSettingsRedirect by remember { mutableStateOf(false) }
    @StringRes var settingsDialogMessageResId by remember { mutableStateOf<Int?>(null) }

    // Helper to refresh permission statuses
    fun updatePermissionStatuses() {
        hasFineLocation = isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)
        hasCoarseLocation = isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        hasBackgroundLocation =
            isPermissionGranted(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    // Launcher for Foreground Location (Fine + Coarse)
    val foregroundPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        updatePermissionStatuses()
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (!fineGranted && coarseGranted) {
            // User granted Approximate but not Precise (High Accuracy)
            showHighAccuracyRationale = true
        } else if (!fineGranted) {
            // Both denied. Check if permanently denied
            val shouldShowFine = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } ?: false
            val shouldShowCoarse = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } ?: false

            if (!shouldShowFine && !shouldShowCoarse) {
                // System prompt will no longer be shown (permanently denied)
                settingsDialogMessageResId = R.string.location_permanently_denied_message
                showSettingsRedirect = true
            }
        }
    }

    // Launcher for High Accuracy (Fine) Location
    val fineLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        updatePermissionStatuses()
        if (!isGranted) {
            val shouldShowFine = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } ?: false

            if (!shouldShowFine) {
                settingsDialogMessageResId = R.string.high_accuracy_disabled_message
                showSettingsRedirect = true
            }
        }
    }

    // Launcher for Background Location
    val backgroundPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        updatePermissionStatuses()
        if (!isGranted) {
            val shouldShowBg = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } ?: false

            if (!shouldShowBg) {
                settingsDialogMessageResId = R.string.background_location_disabled_message
                showSettingsRedirect = true
            }
        }
    }

    // Function to trigger foreground location request with proper pre-checking
    fun requestForegroundLocation() {
        if (hasFineLocation && hasCoarseLocation) return

        val shouldShowFineRationale = activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } ?: false

        if (shouldShowFineRationale) {
            showForegroundRationale = true
        } else {
            foregroundPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Function to trigger background location request (MUST have foreground location first)
    fun requestBackgroundLocation() {
        if (!hasCoarseLocation && !hasFineLocation) {
            // Must ask for foreground permission first on Android 11+
            requestForegroundLocation()
            return
        }

        // Show mandatory in-app rationale before asking for background location
        showBackgroundRationale = true
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.permission_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back_button)
                        )
                    }
                },
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.location_permission_rationale_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.permission_status_header),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(
                            R.string.permission_status_coarse,
                            stringResource(if (hasCoarseLocation) R.string.permission_status_granted else R.string.permission_status_denied)
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.permission_status_fine,
                            stringResource(if (hasFineLocation) R.string.permission_status_granted else R.string.permission_status_denied)
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.permission_status_background,
                            stringResource(if (hasBackgroundLocation) R.string.permission_status_granted else R.string.permission_status_denied)
                        )
                    )
                }
            }

            // Primary Action Buttons
            Button(
                onClick = { requestForegroundLocation() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !hasFineLocation
            ) {
                Text(
                    stringResource(
                        if (hasFineLocation) R.string.btn_foreground_precise_granted
                        else R.string.btn_request_foreground_location
                    )
                )
            }

            Button(
                onClick = {
                    if (hasCoarseLocation && !hasFineLocation) {
                        showHighAccuracyRationale = true
                    } else {
                        fineLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = hasCoarseLocation && !hasFineLocation
            ) {
                Text(stringResource(R.string.btn_upgrade_high_accuracy))
            }

            Button(
                onClick = { requestBackgroundLocation() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !hasBackgroundLocation
            ) {
                Text(
                    stringResource(
                        if (hasBackgroundLocation) R.string.btn_background_location_granted
                        else R.string.btn_request_background_location
                    )
                )
            }

            OutlinedButton(
                onClick = {
                    openAppSettings(context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_open_app_settings))
            }

            // --- EXPLANATION BOXES FOR RATIONALE & SYSTEM SETTINGS ---

            // 1. Foreground Location Rationale
            if (showForegroundRationale) {
                ExplanationBox(
                    title = stringResource(R.string.dialog_foreground_rationale_title),
                    description = stringResource(R.string.dialog_foreground_rationale_message),
                    confirmButton = {
                        ConfirmButton(
                            label = stringResource(R.string.btn_grant_permission),
                            onClick = {
                                showForegroundRationale = false
                                foregroundPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        )
                    },
                    dismissButton = {
                        DismissButton(
                            label = stringResource(R.string.btn_not_now),
                            onClick = { showForegroundRationale = false }
                        )
                    }
                )
            }

            // 2. High Accuracy (Precise Location) Rationale
            if (showHighAccuracyRationale) {
                ExplanationBox(
                    title = stringResource(R.string.dialog_high_accuracy_rationale_title),
                    description = stringResource(R.string.dialog_high_accuracy_rationale_message),
                    confirmButton = {
                        ConfirmButton(
                            label = stringResource(R.string.btn_enable_high_accuracy),
                            onClick = {
                                showHighAccuracyRationale = false
                                fineLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        )
                    },
                    dismissButton = {
                        DismissButton(
                            label = stringResource(R.string.btn_keep_approximate),
                            onClick = { showHighAccuracyRationale = false }
                        )
                    }
                )
            }

            // 3. Background Location Rationale
            if (showBackgroundRationale) {
                ExplanationBox(
                    title = stringResource(R.string.dialog_background_rationale_title),
                    description = stringResource(R.string.dialog_background_rationale_message),
                    confirmButton = {
                        ConfirmButton(
                            label = stringResource(R.string.btn_proceed_to_settings),
                            onClick = {
                                showBackgroundRationale = false
                                backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            }
                        )
                    },
                    dismissButton = {
                        DismissButton(
                            label = stringResource(R.string.btn_cancel),
                            onClick = { showBackgroundRationale = false }
                        )
                    }
                )
            }

            // 4. System Settings Redirect
            if (showSettingsRedirect && settingsDialogMessageResId != null) {
                val detailMessage = stringResource(settingsDialogMessageResId!!)
                ExplanationBox(
                    title = stringResource(R.string.dialog_settings_redirect_title),
                    description = stringResource(
                        R.string.dialog_settings_redirect_message,
                        detailMessage
                    ),
                    confirmButton = {
                        ConfirmButton(
                            label = stringResource(R.string.btn_open_settings),
                            onClick = {
                                showSettingsRedirect = false
                                openAppSettings(context)
                            }
                        )
                    },
                    dismissButton = {
                        DismissButton(
                            label = stringResource(R.string.btn_cancel),
                            onClick = { showSettingsRedirect = false }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ExplanationBox(
    title: String,
    description: String,
    confirmButton: @Composable RowScope.() -> Unit,
    dismissButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                confirmButton()
                dismissButton()
            }
        }
    }
}

@Composable
private fun RowScope.ConfirmButton(label: String, onClick: () -> Unit) =
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
    ) {
        Text(label)
    }

@Composable
private fun RowScope.DismissButton(label: String, onClick: () -> Unit) =
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
    ) {
        Text(label)
    }

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun LocationPermissionScreenPreview() {
    AwaTheme {
        LocationPermissionScreen({})
    }
}