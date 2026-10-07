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
// Today is at index 18 (Wed 07). Index 19 and 20 are future days (Thu 08, Fri 09).
val pastDaysCalendar: List<CalAiDayData> = run {
    val dayNames = listOf("Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue")
    val list = mutableListOf<CalAiDayData>()

    // Day numbers spanning from 2 weeks ago to end of current week
    val dayNumbers = listOf(
        "19", "20", "21", "22", "23", "24", "25", // 2 weeks ago (idx 0..6)
        "26", "27", "28", "29", "30", "01", "02", // 1 week ago (idx 7..13)
        "03", "04", "05", "06", "07", "08", "09"  // Current week (07 is Wed, Today = idx 18; 19 & 20 are future)
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
            isToday -> 1320 // Ongoing today - intake incomplete until logged
            isCompleted -> 2520 + (i * 12) % 150
            else -> 1850 + (i * 25) % 400
        }
        val protein = when {
            isFuture -> 0
            isToday -> 106
            isCompleted -> 165 + (i * 2) % 15
            else -> 122 + (i * 3) % 25
        }
        val carbs = when {
            isFuture -> 0
            isToday -> 132
            isCompleted -> 225 + (i * 4) % 25
            else -> 170 + (i * 5) % 35
        }
        val fat = when {
            isFuture -> 0
            isToday -> 30
            isCompleted -> 72 + (i * 2) % 10
            else -> 45 + (i * 2) % 18
        }

        val dummyMeals = if (isPast) {
            listOf(
                FoodItem(UUID.randomUUID().toString(), "Scrambled Eggs with Avocado Toast", MealType.BREAKFAST, 460, 26, 32, 18, "2 slices + 2 eggs", "8:15 AM"),
                FoodItem(UUID.randomUUID().toString(), "Grilled Chicken Caesar Salad", MealType.LUNCH, 580, 48, 18, 22, "1 large bowl", "1:30 PM"),
                FoodItem(UUID.randomUUID().toString(), "Baked Salmon Fillet with Quinoa", MealType.DINNER, 620, 50, 44, 20, "350g plate", "7:45 PM"),
                FoodItem(UUID.randomUUID().toString(), "Greek Yogurt & Mixed Almonds", MealType.SNACK, 210, 18, 14, 8, "200g serving", "4:30 PM")
            )
        } else emptyList()

        list.add(
            CalAiDayData(
                dayIndex = i,
                dayName = dayName,
                dayNumber = dayNum,
                isToday = isToday,
                isFuture = isFuture,
                hasLog = isPast || isToday,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                fiber = if (isFuture) 0 else 24 + (i % 12),
                sugar = if (isFuture) 0 else 30 + (i % 25),
                sodium = if (isFuture) 0 else 1400 + (i * 45) % 800,
                healthScore = if (isPast) (7..9).random() else 8,
                healthAdvice = if (isCompleted) {
                    "All targets reached! Complete adherence to daily calorie, protein, carbs and healthy fat intake."
                } else {
                    "Carbs and fat are on track. Daily calorie intake was within target deficit."
                },
                meals = dummyMeals
            )
        )
    }
    list
}
