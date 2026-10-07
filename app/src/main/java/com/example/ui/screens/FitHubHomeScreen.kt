package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.AppTracker
import com.example.model.NavTab
import com.example.model.TodayMetric
import com.example.ui.components.AppTrackerCard
import com.example.ui.components.FitHubBottomNav
import com.example.ui.components.FitHubHeader
import com.example.ui.components.SectionHeader
import com.example.ui.components.TodayCard

/**
 * FitHubHomeScreen: 1-to-1 exact Kotlin Jetpack Compose implementation of the HTML/CSS template:
 * - background-color: #f8f8f8;
 * - background-image: radial-gradient(circle at top right, rgba(255, 175, 120, 0.22) 0%, rgba(160, 205, 255, 0.22) 20%, #fcfcfc 45%);
 * - Header (.header)
 * - Section Header (.section-header) "My Apps" + "Manage >"
 * - Apps Grid (.apps-grid): grid-template-columns: repeat(3, 1fr); gap: 12px; padding: 8px 20px;
 * - Today Section (.today-container): TodayCard with .today-grid: 1fr 1fr; gap: 16px;
 * - Glassmorphic Bottom Nav (.bottom-nav)
 */
@Composable
fun FitHubHomeScreen(
    trackers: List<AppTracker>,
    todayMetrics: List<TodayMetric>,
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onTrackerClick: (AppTracker) -> Unit,
    onManageClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("fithub_home_scaffold"),
        containerColor = Color(0xFFF8F8F8),
        bottomBar = {
            FitHubBottomNav(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(Color(0xFFF8F8F8))
                .drawBehind {
                    // Exact HTML CSS radial gradient: circle at top right, rgba(255, 175, 120, 0.22) 0%, rgba(160, 205, 255, 0.22) 20%, #fcfcfc 45%
                    val center = Offset(size.width * 1.0f, 0f)
                    val radius = size.width * 1.15f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.00f to Color(0x38FFAF78), // rgba(255, 175, 120, 0.22)
                                0.20f to Color(0x38A0CDFF), // rgba(160, 205, 255, 0.22)
                                0.45f to Color.Transparent
                            ),
                            center = center,
                            radius = radius
                        ),
                        center = center,
                        radius = radius
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
            ) {
                // Header (FitHub logo badge, title, subtitle, profile button)
                FitHubHeader(
                    onProfileClick = onProfileClick
                )

                // "My Apps" Section Header (.section-header)
                SectionHeader(
                    title = "My Apps",
                    actionText = "Manage >",
                    onActionClick = onManageClick
                )

                // .apps-grid: 3 square apps per row with gap 12px, padding 8px 20px
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .testTag("my_apps_grid"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    trackers.chunked(3).forEach { rowTrackers ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowTrackers.forEach { tracker ->
                                AppTrackerCard(
                                    tracker = tracker,
                                    onClick = { onTrackerClick(tracker) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowTrackers.size < 3) {
                                repeat(3 - rowTrackers.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Today Section (.today-container + .today-card)
                TodayCard(
                    metrics = todayMetrics
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
