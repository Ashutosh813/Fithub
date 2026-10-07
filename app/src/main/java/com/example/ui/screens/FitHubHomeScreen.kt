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
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.GlowBlue
import com.example.ui.theme.GlowPeach

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
        containerColor = BackgroundColor,
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
                .background(BackgroundColor)
                .drawBehind {
                    // Exact iOS radial gradient glow at top right
                    val center = Offset(size.width * 1.05f, -size.height * 0.05f)
                    val radius = size.width * 1.25f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to GlowPeach,
                                0.25f to GlowBlue,
                                0.55f to Color.Transparent
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

                Spacer(modifier = Modifier.height(6.dp))

                // "My Apps" Section Header - Extra bold
                SectionHeader(
                    title = "My Apps",
                    actionText = "Manage >",
                    onActionClick = onManageClick
                )

                // 3 square apps per row grid with 3D tactile press cards
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

                Spacer(modifier = Modifier.height(6.dp))

                // "Today" Section Card - Dynamic to active apps, read-only
                TodayCard(
                    metrics = todayMetrics
                )

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
