package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight

enum class TrackerIconType {
    CALORIE,
    HABIT,
    SLEEP,
    WATER,
    WORKOUT
}

data class AppTracker(
    val id: String,
    val title: String,
    val iconType: TrackerIconType,
    val currentValue: Float,
    val targetValue: Float,
    val currentFormatted: String,
    val targetFormatted: String,
    val progress: Float,
    val progressColor: Color,
    val iconColor: Color,
    val iconBgLight: Color
)

val defaultTrackers = listOf(
    AppTracker(
        id = "calorie",
        title = "Calorie Tracker",
        iconType = TrackerIconType.CALORIE,
        currentValue = 1300f,
        targetValue = 2500f,
        currentFormatted = "1.3k",
        targetFormatted = "/ 2.5k",
        progress = 0.52f,
        progressColor = AccentPurple,
        iconColor = AccentRed,
        iconBgLight = AccentRedLight
    ),
    AppTracker(
        id = "habit",
        title = "Habit Tracker",
        iconType = TrackerIconType.HABIT,
        currentValue = 3f,
        targetValue = 5f,
        currentFormatted = "3",
        targetFormatted = "/ 5 today",
        progress = 0.60f,
        progressColor = AccentGreen,
        iconColor = AccentGreen,
        iconBgLight = AccentGreenLight
    ),
    AppTracker(
        id = "sleep",
        title = "Sleep Tracker",
        iconType = TrackerIconType.SLEEP,
        currentValue = 6.5f,
        targetValue = 8.0f,
        currentFormatted = "6.5",
        targetFormatted = "/ 8 hrs",
        progress = 0.81f,
        progressColor = AccentPurple,
        iconColor = AccentPurple,
        iconBgLight = AccentPurpleLight
    )
)
