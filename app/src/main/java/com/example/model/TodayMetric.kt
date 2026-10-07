package com.example.model

enum class TodayMetricType {
    CALORIES,
    WATER,
    HABITS,
    ACTIVITY
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
        id = "today_water",
        title = "Water",
        type = TodayMetricType.WATER,
        primaryValue = "4",
        secondaryValue = "/ 8 glasses",
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
        id = "today_activity",
        title = "Activity",
        type = TodayMetricType.ACTIVITY,
        primaryValue = "6,842",
        secondaryValue = "steps",
        isPrimaryBold = true,
        isSecondaryBold = true
    )
)
