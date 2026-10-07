package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AiScanResult
import com.example.model.AppTracker
import com.example.model.CalAiTab
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.model.NavTab
import com.example.model.SampleFoodPreset
import com.example.model.TodayMetric
import com.example.model.TodayMetricType
import com.example.model.TrackerIconType
import com.example.model.defaultTodayMetrics
import com.example.model.defaultTrackers
import com.example.model.initialFoodItems
import com.example.model.sampleFoodDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class FitHubUiState(
    val trackers: List<AppTracker> = defaultTrackers,
    val todayMetrics: List<TodayMetric> = defaultTodayMetrics,
    val selectedTab: NavTab = NavTab.HOME,
    val isManageSheetOpen: Boolean = false,
    val selectedTracker: AppTracker? = null,
    val selectedMetric: TodayMetric? = null,
    val isProfileOpen: Boolean = false,

    // Full-screen App States (Slide up all the way from bottom)
    val isCalAiOpen: Boolean = false,
    val isHabitTrackerOpen: Boolean = false,
    val isSleepTrackerOpen: Boolean = false,

    // Cal AI State
    val calAiTab: CalAiTab = CalAiTab.HOME,
    val foodItems: List<FoodItem> = initialFoodItems,
    val calorieGoal: Int = 2500,
    val proteinGoal: Int = 160,
    val carbsGoal: Int = 220,
    val fatGoal: Int = 70,

    // Camera Scan State
    val isCameraScanning: Boolean = false,
    val currentScanResult: AiScanResult? = null,
    val isAddFoodSheetOpen: Boolean = false,
    val selectedMealTypeForAdd: MealType = MealType.LUNCH,

    // Base Tracker Metrics
    val userWaterGlasses: Int = 4,
    val userHabitsDone: Int = 3,
    val userSteps: Int = 6842,
    val userSleepHours: Float = 6.5f
) {
    val totalCaloriesConsumed: Int get() = foodItems.sumOf { it.calories }
    val totalProteinConsumed: Int get() = foodItems.sumOf { it.protein }
    val totalCarbsConsumed: Int get() = foodItems.sumOf { it.carbs }
    val totalFatConsumed: Int get() = foodItems.sumOf { it.fat }
    val remainingCalories: Int get() = (calorieGoal - totalCaloriesConsumed).coerceAtLeast(0)
    val calorieProgress: Float get() = (totalCaloriesConsumed.toFloat() / calorieGoal.toFloat()).coerceIn(0f, 1f)
}

class FitHubViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FitHubUiState())
    val uiState: StateFlow<FitHubUiState> = _uiState.asStateFlow()

    init {
        syncCaloriesWithTrackers()
    }

    fun selectTab(tab: NavTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setManageSheetOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isManageSheetOpen = isOpen) }
    }

    fun setProfileOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isProfileOpen = isOpen) }
    }

    fun selectTracker(tracker: AppTracker?) {
        if (tracker == null) {
            _uiState.update { it.copy(selectedTracker = null) }
            return
        }
        when (tracker.iconType) {
            TrackerIconType.CALORIE -> openCalAi()
            TrackerIconType.HABIT -> openHabitTracker()
            TrackerIconType.SLEEP -> openSleepTracker()
            else -> _uiState.update { it.copy(selectedTracker = tracker) }
        }
    }

    fun selectMetric(metric: TodayMetric?) {
        if (metric == null) {
            _uiState.update { it.copy(selectedMetric = null) }
            return
        }
        when (metric.type) {
            TodayMetricType.CALORIES -> openCalAi()
            TodayMetricType.HABITS -> openHabitTracker()
            TodayMetricType.WATER -> addWaterGlass(1)
            TodayMetricType.ACTIVITY -> addSteps(500)
        }
    }

    // Full-screen App transitions
    fun openCalAi(tab: CalAiTab = CalAiTab.HOME) {
        _uiState.update {
            it.copy(
                isCalAiOpen = true,
                calAiTab = tab,
                selectedTracker = null,
                selectedMetric = null
            )
        }
    }

    fun closeCalAi() {
        _uiState.update { it.copy(isCalAiOpen = false) }
    }

    fun setCalAiTab(tab: CalAiTab) {
        _uiState.update { it.copy(calAiTab = tab) }
    }

    fun openHabitTracker() {
        _uiState.update {
            it.copy(
                isHabitTrackerOpen = true,
                selectedTracker = null,
                selectedMetric = null
            )
        }
    }

    fun closeHabitTracker() {
        _uiState.update { it.copy(isHabitTrackerOpen = false) }
    }

    fun openSleepTracker() {
        _uiState.update {
            it.copy(
                isSleepTrackerOpen = true,
                selectedTracker = null,
                selectedMetric = null
            )
        }
    }

    fun closeSleepTracker() {
        _uiState.update { it.copy(isSleepTrackerOpen = false) }
    }

    // Food Management
    fun addFoodItem(
        name: String,
        mealType: MealType,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        portion: String = "1 serving"
    ) {
        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newItem = FoodItem(
            id = UUID.randomUUID().toString(),
            name = name,
            mealType = mealType,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            portionDescription = portion,
            time = currentTime
        )
        _uiState.update { state ->
            val updatedList = listOf(newItem) + state.foodItems
            state.copy(foodItems = updatedList)
        }
        syncCaloriesWithTrackers()
    }

    fun deleteFoodItem(id: String) {
        _uiState.update { state ->
            val updated = state.foodItems.filterNot { it.id == id }
            state.copy(foodItems = updated)
        }
        syncCaloriesWithTrackers()
    }

    // AI Camera Scanning Feature
    fun startCameraScan(samplePreset: SampleFoodPreset? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCameraScanning = true, currentScanResult = null) }
            delay(1400) // Simulating realistic Cal AI neural network analysis
            val preset = samplePreset ?: sampleFoodDatabase.random()
            val result = AiScanResult(
                dishName = preset.name,
                calories = preset.calories,
                protein = preset.protein,
                carbs = preset.carbs,
                fat = preset.fat,
                confidence = (94..99).random(),
                suggestedMealType = preset.defaultMealType,
                servingSizeGrams = 320,
                portionMultiplier = 1.0f
            )
            _uiState.update { it.copy(isCameraScanning = false, currentScanResult = result) }
        }
    }

    fun clearScanResult() {
        _uiState.update { it.copy(currentScanResult = null, isCameraScanning = false) }
    }

    fun updateScanPortion(multiplier: Float) {
        _uiState.update { state ->
            val current = state.currentScanResult ?: return@update state
            state.copy(currentScanResult = current.copy(portionMultiplier = multiplier))
        }
    }

    fun logCurrentScanResult() {
        val scan = _uiState.value.currentScanResult ?: return
        val multiplier = scan.portionMultiplier
        val adjustedCalories = (scan.calories * multiplier).toInt()
        val adjustedProtein = (scan.protein * multiplier).toInt()
        val adjustedCarbs = (scan.carbs * multiplier).toInt()
        val adjustedFat = (scan.fat * multiplier).toInt()

        addFoodItem(
            name = scan.dishName,
            mealType = scan.suggestedMealType,
            calories = adjustedCalories,
            protein = adjustedProtein,
            carbs = adjustedCarbs,
            fat = adjustedFat,
            portion = "${(scan.servingSizeGrams * multiplier).toInt()}g portion"
        )
        clearScanResult()
        setCalAiTab(CalAiTab.HOME)
    }

    fun setAddFoodSheetOpen(isOpen: Boolean, mealType: MealType = MealType.LUNCH) {
        _uiState.update {
            it.copy(
                isAddFoodSheetOpen = isOpen,
                selectedMealTypeForAdd = mealType
            )
        }
    }

    private fun syncCaloriesWithTrackers() {
        val state = _uiState.value
        val totalCalories = state.totalCaloriesConsumed
        val calK = if (totalCalories >= 1000) {
            String.format(Locale.US, "%.1fk", totalCalories / 1000f)
        } else {
            totalCalories.toString()
        }
        val formattedCalories = NumberFormat.getNumberInstance(Locale.US).format(totalCalories)
        val progress = (totalCalories.toFloat() / state.calorieGoal.toFloat()).coerceIn(0f, 1f)

        val updatedTrackers = state.trackers.map { tracker ->
            if (tracker.iconType == TrackerIconType.CALORIE) {
                tracker.copy(
                    currentValue = totalCalories.toFloat(),
                    currentFormatted = calK,
                    progress = progress
                )
            } else tracker
        }

        val updatedMetrics = state.todayMetrics.map { metric ->
            if (metric.type == TodayMetricType.CALORIES) {
                metric.copy(primaryValue = formattedCalories)
            } else metric
        }

        _uiState.update {
            it.copy(
                trackers = updatedTrackers,
                todayMetrics = updatedMetrics
            )
        }
    }

    fun addCalories(amount: Int) {
        addFoodItem(
            name = "Quick Meal Log",
            mealType = MealType.SNACK,
            calories = amount,
            protein = (amount * 0.08f).toInt(),
            carbs = (amount * 0.12f).toInt(),
            fat = (amount * 0.04f).toInt(),
            portion = "Quick logged"
        )
    }

    fun addWaterGlass(delta: Int = 1) {
        val newWater = (_uiState.value.userWaterGlasses + delta).coerceIn(0, 16)
        _uiState.update { state ->
            val updatedMetrics = state.todayMetrics.map { metric ->
                if (metric.type == TodayMetricType.WATER) {
                    metric.copy(primaryValue = newWater.toString())
                } else metric
            }
            state.copy(userWaterGlasses = newWater, todayMetrics = updatedMetrics)
        }
    }

    fun toggleHabitStep() {
        val current = _uiState.value.userHabitsDone
        val newHabits = if (current >= 5) 1 else current + 1
        val progress = (newHabits / 5f).coerceIn(0f, 1f)

        _uiState.update { state ->
            val updatedTrackers = state.trackers.map { tracker ->
                if (tracker.iconType == TrackerIconType.HABIT) {
                    tracker.copy(
                        currentValue = newHabits.toFloat(),
                        currentFormatted = newHabits.toString(),
                        progress = progress
                    )
                } else tracker
            }
            val updatedMetrics = state.todayMetrics.map { metric ->
                if (metric.type == TodayMetricType.HABITS) {
                    metric.copy(primaryValue = newHabits.toString())
                } else metric
            }
            state.copy(
                userHabitsDone = newHabits,
                trackers = updatedTrackers,
                todayMetrics = updatedMetrics
            )
        }
    }

    fun addSteps(delta: Int) {
        val newSteps = (_uiState.value.userSteps + delta).coerceAtLeast(0)
        val formattedSteps = NumberFormat.getNumberInstance(Locale.US).format(newSteps)

        _uiState.update { state ->
            val updatedMetrics = state.todayMetrics.map { metric ->
                if (metric.type == TodayMetricType.ACTIVITY) {
                    metric.copy(primaryValue = formattedSteps)
                } else metric
            }
            state.copy(userSteps = newSteps, todayMetrics = updatedMetrics)
        }
    }

    fun addSleep(delta: Float) {
        val newSleep = (_uiState.value.userSleepHours + delta).coerceIn(0f, 14f)
        val formattedSleep = if (newSleep % 1f == 0f) String.format(Locale.US, "%.0f", newSleep) else String.format(Locale.US, "%.1f", newSleep)
        val progress = (newSleep / 8.0f).coerceIn(0f, 1f)

        _uiState.update { state ->
            val updatedTrackers = state.trackers.map { tracker ->
                if (tracker.iconType == TrackerIconType.SLEEP) {
                    tracker.copy(
                        currentValue = newSleep,
                        currentFormatted = formattedSleep,
                        progress = progress
                    )
                } else tracker
            }
            state.copy(userSleepHours = newSleep, trackers = updatedTrackers)
        }
    }
}
