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
import com.example.ui.calai.CalAiScreen
import com.example.ui.components.ManageAppsSheet
import com.example.ui.components.ProfileSheet
import com.example.ui.components.TrackerDetailSheet
import com.example.ui.habits.HabitTrackerFullScreen
import com.example.ui.screens.AppsCatalogScreen
import com.example.ui.screens.FitHubHomeScreen
import com.example.ui.screens.ProgressAnalyticsScreen
import com.example.ui.screens.UserProfileScreen
import com.example.ui.sleep.SleepTrackerFullScreen
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
                        onMetricClick = { viewModel.selectMetric(it) },
                        onManageClick = { viewModel.setManageSheetOpen(true) },
                        onProfileClick = { viewModel.setProfileOpen(true) }
                    )
                }
                NavTab.APPS -> {
                    AppsCatalogScreen(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { viewModel.selectTab(it) },
                        onBack = { viewModel.selectTab(NavTab.HOME) }
                    )
                }
                NavTab.PROGRESS -> {
                    ProgressAnalyticsScreen(
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

        // Bottom Sheets for Manage & Profile
        if (uiState.isManageSheetOpen) {
            ManageAppsSheet(
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
