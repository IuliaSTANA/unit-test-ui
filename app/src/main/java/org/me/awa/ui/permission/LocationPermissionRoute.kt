package org.me.awa.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationPermissionRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LocationPermissionScreen(
        onBackClick = onBack,
        modifier = modifier
    )
}
