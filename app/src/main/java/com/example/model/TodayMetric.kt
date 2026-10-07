package com.example.model

enum class TodayMetricType {
    CALORIES,
    HABITS,
    SLEEP,
    WATER,
    ACTIVITY,
    WORKOUT,
    FASTING,
    MEDITATION
}

data class TodayMetric(
    val id: String,
    val title: String,
    val type: TodayMetricType,
    val primaryValue: String,
    val secondaryValue: String,
    val isPrimaryBold: Boolean = true,
    val isSecondaryBold: Boolean = false
)

// Default matches the initial 3 apps in My Apps: Calorie Tracker, Habit Tracker, Sleep Tracker
// If an app is not present in My Apps, its metric is NOT shown in Today card
val defaultTodayMetrics = listOf(
    TodayMetric(
        id = "today_calories",
        title = "Calories",
        type = TodayMetricType.CALORIES,
        primaryValue = "1,320",
        secondaryValue = "/ 2,500 kcal",
        isPrimaryBold = true,
        isSecondaryBold = false
    ),
    TodayMetric(
        id = "today_habits",
        title = "Habits",
        type = TodayMetricType.HABITS,
        primaryValue = "3",
        secondaryValue = "/ 5 completed",
        isPrimaryBold = true,
        isSecondaryBold = false
    ),
    TodayMetric(
        id = "today_sleep",
        title = "Sleep",
        type = TodayMetricType.SLEEP,
        primaryValue = "6.5",
        secondaryValue = "/ 8 hrs",
        isPrimaryBold = true,
        isSecondaryBold = false
    )
)
