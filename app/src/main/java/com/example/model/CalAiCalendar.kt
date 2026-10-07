package com.example.model

import java.util.UUID

data class CalAiDayData(
    val dayIndex: Int,
    val dayName: String,
    val dayNumber: String,
    val isToday: Boolean,
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
)

// Generates 21 days: 2 weeks before present day (14 days) + current week (7 days)
// Today is at index 18 (Wed 07)
val pastDaysCalendar: List<CalAiDayData> = run {
    val dayNames = listOf("Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue")
    val list = mutableListOf<CalAiDayData>()

    // Day numbers spanning from 2 weeks ago to end of current week
    val dayNumbers = listOf(
        "19", "20", "21", "22", "23", "24", "25", // 2 weeks ago
        "26", "27", "28", "29", "30", "01", "02", // 1 week ago
        "03", "04", "05", "06", "07", "08", "09"  // Current week (07 is Wed, Today!)
    )

    for (i in 0 until 21) {
        val isToday = i == 18
        val isPast = i < 18
        val dayName = dayNames[i % 7]
        val dayNum = dayNumbers[i]

        val calories = when {
            isToday -> 1320
            isPast -> 1900 + (i * 27) % 600
            else -> 0
        }
        val protein = when {
            isToday -> 106
            isPast -> 130 + (i * 5) % 40
            else -> 0
        }
        val carbs = when {
            isToday -> 132
            isPast -> 180 + (i * 8) % 60
            else -> 0
        }
        val fat = when {
            isToday -> 30
            isPast -> 45 + (i * 3) % 30
            else -> 0
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
                hasLog = isPast || isToday,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                fiber = 24 + (i % 12),
                sugar = 30 + (i % 25),
                sodium = 1400 + (i * 45) % 800,
                healthScore = if (isPast) (7..9).random() else 8,
                meals = dummyMeals
            )
        )
    }
    list
}
