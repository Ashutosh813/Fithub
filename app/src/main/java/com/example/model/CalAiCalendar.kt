package com.example.model

import java.util.UUID

data class CalAiDayData(
    val dayIndex: Int,
    val dayName: String,
    val dayNumber: String,
    val isToday: Boolean,
    val isFuture: Boolean = false,
    val hasLog: Boolean,
    val calories: Int,
    val calorieGoal: Int = 2500,
    val protein: Int,
    val proteinGoal: Int = 160,
    val carbs: Int,
    val carbsGoal: Int = 220,
    val fat: Int,
    val fatGoal: Int = 70,
    val fiber: Int = 28,
    val sugar: Int = 42,
    val sodium: Int = 1680,
    val healthScore: Int = 8,
    val healthAdvice: String = "Carbs and fat are on track. You maintained healthy calorie expenditure.",
    val meals: List<FoodItem> = emptyList()
) {
    // True ONLY if calorie, protein, carbs and fat intake are all completed/reached
    val isGoalsCompleted: Boolean
        get() = !isFuture && calories >= calorieGoal && protein >= proteinGoal && carbs >= carbsGoal && fat >= fatGoal
}

// Generates 21 days: 2 weeks before present day (14 days) + current week (7 days)
// Today is at index 18 (Tue 07). Index 19 and 20 are future days (Wed 08, Thu 09).
val pastDaysCalendar: List<CalAiDayData> = run {
    val dayNames = listOf("Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue")
    val list = mutableListOf<CalAiDayData>()

    // Day numbers spanning from 2 weeks ago to end of current week
    val dayNumbers = listOf(
        "19", "20", "21", "22", "23", "24", "25", // 2 weeks ago (idx 0..6)
        "26", "27", "28", "29", "30", "01", "02", // 1 week ago (idx 7..13)
        "03", "04", "05", "06", "07", "08", "09"  // Current week (07 is Today = idx 18; 19 & 20 are future)
    )

    // Selected past days that successfully completed their target calorie, protein, fat & carb intake goals
    val completedGoalIndices = setOf(1, 3, 5, 8, 11, 13, 15, 17)

    for (i in 0 until 21) {
        val isToday = i == 18
        val isPast = i < 18
        val isFuture = i > 18
        val dayName = dayNames[i % 7]
        val dayNum = dayNumbers[i]

        val isCompleted = isPast && i in completedGoalIndices

        val calories = when {
            isFuture -> 0
            isToday -> 1320 // Ongoing today - intake incomplete until logged to target
            isCompleted -> 2520 + (i * 12) % 150
            else -> 1750 + (i * 25) % 300
        }
        val protein = when {
            isFuture -> 0
            isToday -> 92
            isCompleted -> 168 + (i * 3) % 20
            else -> 115 + (i * 4) % 25
        }
        val carbs = when {
            isFuture -> 0
            isToday -> 145
            isCompleted -> 228 + (i * 5) % 30
            else -> 170 + (i * 7) % 35
        }
        val fat = when {
            isFuture -> 0
            isToday -> 48
            isCompleted -> 72 + (i * 2) % 15
            else -> 52 + (i * 3) % 12
        }

        list.add(
            CalAiDayData(
                dayIndex = i,
                dayName = dayName,
                dayNumber = dayNum,
                isToday = isToday,
                isFuture = isFuture,
                hasLog = !isFuture,
                calories = calories,
                calorieGoal = 2500,
                protein = protein,
                proteinGoal = 160,
                carbs = carbs,
                carbsGoal = 220,
                fat = fat,
                fatGoal = 70,
                fiber = if (isFuture) 0 else 24 + (i * 2) % 15,
                sugar = if (isFuture) 0 else 38 + (i * 3) % 20,
                sodium = if (isFuture) 0 else 1620 + (i * 45) % 400,
                healthScore = if (isFuture) 0 else if (isCompleted) (8..10).random() else (6..8).random(),
                meals = if (!isFuture && !isToday) listOf(
                    FoodItem(
                        id = UUID.randomUUID().toString(),
                        name = "Oatmeal with Almonds",
                        mealType = MealType.BREAKFAST,
                        calories = 420,
                        protein = 18,
                        carbs = 58,
                        fat = 12,
                        portionDescription = "1 bowl (250g)",
                        time = "8:30 AM"
                    ),
                    FoodItem(
                        id = UUID.randomUUID().toString(),
                        name = "Grilled Chicken Rice Bowl",
                        mealType = MealType.LUNCH,
                        calories = 650,
                        protein = 48,
                        carbs = 62,
                        fat = 16,
                        portionDescription = "1 plate (400g)",
                        time = "1:15 PM"
                    )
                ) else emptyList()
            )
        )
    }

    list
}
