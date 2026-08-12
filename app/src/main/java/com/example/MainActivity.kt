package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.viewmodel.AppTab
import com.example.data.viewmodel.MainViewModel
import com.example.ui.components.AshBottomNav
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AgentsScreen
import com.example.ui.screens.ImageStudioScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.AshFindesTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AshFindesTheme {
                val selectedTab by viewModel.selectedTab.collectAsState()
                val showAdminPanel by viewModel.showAdminPanel.collectAsState()
                val activeSessionId by viewModel.activeSessionId.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    bottomBar = {
                        // Only show bottom nav if not in admin panel and no active chat is full screen
                        if (!showAdminPanel && activeSessionId == null) {
                            AshBottomNav(
                                selectedTab = selectedTab,
                                onTabSelected = { tab -> viewModel.selectTab(tab) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showAdminPanel) {
                            AdminPanelScreen(
                                viewModel = viewModel,
                                onCloseAdmin = { viewModel.toggleAdminPanel(false) }
                            )
                        } else {
                            when (selectedTab) {
                                AppTab.SEARCH -> SearchScreen(
                                    viewModel = viewModel,
                                    onNavigateTab = { tab -> viewModel.selectTab(tab) },
                                    onOpenAdmin = { viewModel.toggleAdminPanel(true) }
                                )
                                AppTab.STUDIO -> ImageStudioScreen(
                                    viewModel = viewModel
                                )
                                AppTab.AGENTS -> AgentsScreen(
                                    viewModel = viewModel
                                )
                                AppTab.ACCOUNT -> AccountScreen(
                                    viewModel = viewModel,
                                    onOpenAdmin = { viewModel.toggleAdminPanel(true) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
