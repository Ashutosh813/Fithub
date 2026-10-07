package com.example.model

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snacks")
}

data class FoodItem(
    val id: String,
    val name: String,
    val mealType: MealType,
    val calories: Int,
    val protein: Int, // in grams
    val carbs: Int,   // in grams
    val fat: Int,     // in grams
    val portionDescription: String = "1 serving",
    val time: String
)

data class SampleFoodPreset(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val defaultMealType: MealType,
    val portion: String,
    val category: String
)

val sampleFoodDatabase = listOf(
    SampleFoodPreset("Grilled Chicken Salad", 380, 42, 12, 14, MealType.LUNCH, "300g bowl", "Healthy"),
    SampleFoodPreset("Avocado Toast & Egg", 340, 16, 28, 18, MealType.BREAKFAST, "2 slices", "Breakfast"),
    SampleFoodPreset("Protein Oatmeal Bowl", 410, 32, 52, 8, MealType.BREAKFAST, "1 bowl", "Breakfast"),
    SampleFoodPreset("Salmon Fillet & Quinoa", 520, 44, 38, 20, MealType.DINNER, "350g plate", "Dinner"),
    SampleFoodPreset("Paneer Tikka with Roti", 460, 26, 42, 18, MealType.DINNER, "1 plate", "Indian"),
    SampleFoodPreset("Greek Yogurt & Berries", 180, 18, 16, 4, MealType.SNACK, "200g cup", "Snacks"),
    SampleFoodPreset("Whey Protein Shake", 140, 26, 3, 2, MealType.SNACK, "1 scoop (300ml)", "Fitness"),
    SampleFoodPreset("Almonds & Walnuts", 160, 6, 6, 14, MealType.SNACK, "30g handful", "Snacks"),
    SampleFoodPreset("Chicken Rice Burrito", 620, 38, 68, 22, MealType.LUNCH, "1 wrap", "Lunch")
)

val initialFoodItems = listOf(
    FoodItem(
        id = "f1",
        name = "Oatmeal with Blueberries & Whey",
        mealType = MealType.BREAKFAST,
        calories = 420,
        protein = 28,
        carbs = 54,
        fat = 8,
        portionDescription = "1 bowl (320g)",
        time = "8:30 AM"
    ),
    FoodItem(
        id = "f2",
        name = "Grilled Chicken Breast with Brown Rice",
        mealType = MealType.LUNCH,
        calories = 580,
        protein = 48,
        carbs = 52,
        fat = 12,
        portionDescription = "1 plate (380g)",
        time = "1:15 PM"
    ),
    FoodItem(
        id = "f3",
        name = "Greek Yogurt & Honey",
        mealType = MealType.SNACK,
        calories = 190,
        protein = 18,
        carbs = 18,
        fat = 4,
        portionDescription = "200g bowl",
        time = "4:45 PM"
    ),
    FoodItem(
        id = "f4",
        name = "Boiled Eggs & Toast",
        mealType = MealType.DINNER,
        calories = 130,
        protein = 12,
        carbs = 8,
        fat = 6,
        portionDescription = "Light evening bite",
        time = "7:30 PM"
    )
)
