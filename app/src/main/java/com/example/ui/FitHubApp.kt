package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavTab
import com.example.model.TrackerIconType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AvTimer
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Spa
import com.example.ui.calai.CalAiScreen
import com.example.ui.components.ManageAppsSheet
import com.example.ui.components.ProfileSheet
import com.example.ui.habits.HabitTrackerFullScreen
import com.example.ui.screens.AppsCatalogScreen
import com.example.ui.screens.ComingSoonAppScreen
import com.example.ui.screens.ComingSoonFeatureInfo
import com.example.ui.screens.FitHubHomeScreen
import com.example.ui.screens.ProgressAnalyticsScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.sleep.SleepTrackerFullScreen
import com.example.ui.water.WaterTrackingFullScreen
import com.example.viewmodel.FitHubViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitHubApp(
    viewModel: FitHubViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        // Base FitHub App Experience (Home, Apps, Progress, Profile)
        AnimatedContent(
            targetState = uiState.selectedTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "tab_navigation",
            modifier = Modifier.fillMaxSize()
        ) { currentTab ->
            when (currentTab) {
                NavTab.HOME -> {
                    FitHubHomeScreen(
                        trackers = uiState.trackers,
                        todayMetrics = uiState.todayMetrics,
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        onTrackerClick = { viewModel.selectTracker(it) },
                        onManageClick = { viewModel.setManageSheetOpen(true) },
                        onProfileClick = { viewModel.setProfileOpen(true) }
                    )
                }
                NavTab.APPS -> {
                    AppsCatalogScreen(
                        activeTrackerIds = uiState.trackers.map { it.id }.toSet(),
                        onToggleApp = { id, enabled -> viewModel.toggleAppInMyApps(id, enabled) },
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        onBack = { viewModel.selectTab(NavTab.HOME) }
                    )
                }
                NavTab.PROGRESS -> {
                    ProgressAnalyticsScreen(
                        trackers = uiState.trackers,
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        onBack = { viewModel.selectTab(NavTab.HOME) }
                    )
                }
                NavTab.PROFILE -> {
                    UserProfileScreen(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        onBack = { viewModel.selectTab(NavTab.HOME) }
                    )
                }
            }
        }

        // Full-screen Cal AI (Slides up all the way from the bottom!)
        AnimatedVisibility(
            visible = uiState.isCalAiOpen,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            CalAiScreen(
                currentTab = uiState.calAiTab,
                onTabSelected = { viewModel.setCalAiTab(it) },
                foodItems = uiState.foodItems,
                totalCalories = uiState.totalCaloriesConsumed,
                calorieGoal = uiState.calorieGoal,
                totalProtein = uiState.totalProteinConsumed,
                proteinGoal = uiState.proteinGoal,
                totalCarbs = uiState.totalCarbsConsumed,
                carbsGoal = uiState.carbsGoal,
                totalFat = uiState.totalFatConsumed,
                fatGoal = uiState.fatGoal,
                currentSelectedDay = uiState.currentSelectedDay,
                calendarDays = uiState.calendarDays,
                selectedDayIndex = uiState.selectedDayIndex,
                onSelectDay = { viewModel.selectCalendarDay(it) },
                isCameraScanning = uiState.isCameraScanning,
                scanResult = uiState.currentScanResult,
                isAddFoodSheetOpen = uiState.isAddFoodSheetOpen,
                selectedMealTypeForAdd = uiState.selectedMealTypeForAdd,
                onClose = { viewModel.closeCalAi() },
                onTriggerScan = { viewModel.startCameraScan(it) },
                onPortionChange = { viewModel.updateScanPortion(it) },
                onLogScan = { viewModel.logCurrentScanResult() },
                onDismissScan = { viewModel.clearScanResult() },
                onOpenAddFood = { viewModel.setAddFoodSheetOpen(true, it) },
                onCloseAddFood = { viewModel.setAddFoodSheetOpen(false) },
                onAddFood = { name, mealType, cals, p, c, f, portion ->
                    viewModel.addFoodItem(name, mealType, cals, p, c, f, portion)
                },
                onDeleteFood = { viewModel.deleteFoodItem(it) }
            )
        }

        // Full-screen Habit Tracker (Slides up all the way from bottom!)
        AnimatedVisibility(
            visible = uiState.isHabitTrackerOpen,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            HabitTrackerFullScreen(
                onClose = { viewModel.closeHabitTracker() },
                onHabitToggle = { viewModel.toggleHabitStep() }
            )
        }

        // Full-screen Sleep Tracker (Slides up all the way from bottom!)
        AnimatedVisibility(
            visible = uiState.isSleepTrackerOpen,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            SleepTrackerFullScreen(
                sleepHours = uiState.userSleepHours,
                onClose = { viewModel.closeSleepTracker() }
            )
        }

        // Full-screen Water Tracking with "Coming Soon" (Slides up from bottom!)
        AnimatedVisibility(
            visible = uiState.isWaterTrackerOpen,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            WaterTrackingFullScreen(
                onClose = { viewModel.closeWaterTracker() }
            )
        }

        // Full-screen Coming Soon for unbuilt modules (Slides up from bottom!)
        AnimatedVisibility(
            visible = uiState.isComingSoonOpen && uiState.activeComingSoonApp != null,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            uiState.activeComingSoonApp?.let { app ->
                val (category, icon, features) = when (app.iconType) {
                    TrackerIconType.WORKOUT -> Triple(
                        "Activity",
                        if (app.id == "activity") Icons.Rounded.DirectionsRun else Icons.Rounded.FitnessCenter,
                        listOf(
                            ComingSoonFeatureInfo("Pedometer & Step Goals", "Automatic step detection, distance calculations and cadence analytics."),
                            ComingSoonFeatureInfo("Active Calorie Burn", "Metabolic equivalent estimation integrated with heart rate sensors."),
                            ComingSoonFeatureInfo("Health Connect Sync", "Two-way live synchronization with Wear OS and fitness wearables.")
                        )
                    )
                    TrackerIconType.FASTING -> Triple(
                        "Fasting",
                        Icons.Rounded.AvTimer,
                        listOf(
                            ComingSoonFeatureInfo("Circadian Rhythm Window", "16:8, 18:6, 20:4, and custom circadian fasting timer presets."),
                            ComingSoonFeatureInfo("Autophagy & Ketosis Stages", "Visual indicators for metabolic state progression throughout fasts."),
                            ComingSoonFeatureInfo("Gentle Fasting Alerts", "Helpful milestone notifications to stay on track comfortably.")
                        )
                    )
                    TrackerIconType.MEDITATION -> Triple(
                        "Mindfulness",
                        Icons.Rounded.SelfImprovement,
                        listOf(
                            ComingSoonFeatureInfo("Box Breathing Exercises", "Science-backed visual pacing for immediate stress reduction."),
                            ComingSoonFeatureInfo("Daily Mindful Streaks", "Build lifelong daily meditation and intentional reflection habits."),
                            ComingSoonFeatureInfo("Ambient Soundscapes", "Calming binaural beats and nature audio tracks for focus and sleep.")
                        )
                    )
                    else -> Triple(
                        "Wellness",
                        Icons.Rounded.Spa,
                        listOf(
                            ComingSoonFeatureInfo("Automated Daily Logging", "Seamless tracking of daily metrics and milestones."),
                            ComingSoonFeatureInfo("Intelligent AI Analytics", "Personalized insights and tips to reach your fitness goals."),
                            ComingSoonFeatureInfo("FitHub Dashboard Sync", "Live updates on your Home grid and Today overview.")
                        )
                    )
                }

                ComingSoonAppScreen(
                    appName = app.title,
                    categoryBadge = category,
                    subtitle = "Modular AI Fitness Feature",
                    iconVector = icon,
                    themeColor = app.progressColor,
                    features = features,
                    onClose = { viewModel.closeComingSoonApp() }
                )
            }
        }

        // Bottom Sheets for Manage & Profile
        if (uiState.isManageSheetOpen) {
            ManageAppsSheet(
                activeTrackerIds = uiState.trackers.map { it.id }.toSet(),
                onToggleApp = { id, enabled -> viewModel.toggleAppInMyApps(id, enabled) },
                onDismiss = { viewModel.setManageSheetOpen(false) }
            )
        }

        if (uiState.isProfileOpen) {
            ProfileSheet(
                onDismiss = { viewModel.setProfileOpen(false) }
            )
        }
    }
}
