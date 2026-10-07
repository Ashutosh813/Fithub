package com.example.model

enum class CalAiTab(val label: String) {
    HOME("Home"),
    CAMERA("Camera"),
    PROGRESS("Progress")
}

data class AiScanResult(
    val dishName: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val confidence: Int = 96,
    val suggestedMealType: MealType = MealType.LUNCH,
    val servingSizeGrams: Int = 320,
    val portionMultiplier: Float = 1.0f
)
