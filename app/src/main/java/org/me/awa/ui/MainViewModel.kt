package org.me.awa.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.me.awa.ui.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val navigator: AppNavigator
) : ViewModel()
