package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AiScanResult
import com.example.model.AppTracker
import com.example.model.CalAiDayData
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
import com.example.model.pastDaysCalendar
import com.example.model.sampleFoodDatabase
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueLight
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
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

    // Full-screen App States
    val isCalAiOpen: Boolean = false,
    val isHabitTrackerOpen: Boolean = false,
    val isSleepTrackerOpen: Boolean = false,
    val isWaterTrackerOpen: Boolean = false,
    val isComingSoonOpen: Boolean = false,
    val activeComingSoonApp: AppTracker? = null,

    // Cal AI Calendar & State
    val selectedDayIndex: Int = 18, // 18 is Today Wed 07
    val calendarDays: List<CalAiDayData> = pastDaysCalendar,
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
    val userSleepHours: Float = 6.5f,
    val userWorkoutsDone: Int = 1,
    val userFastingHours: Float = 14f,
    val userMeditationMinutes: Int = 15
) {
    val currentSelectedDay: CalAiDayData
        get() = calendarDays.getOrNull(selectedDayIndex) ?: calendarDays[18]

    // If viewing Today (day 18), use active live meals; otherwise use the past day's dummy meals
    val displayedFoodItems: List<FoodItem>
        get() = if (selectedDayIndex == 18) foodItems else currentSelectedDay.meals

    val totalCaloriesConsumed: Int
        get() = if (selectedDayIndex == 18) foodItems.sumOf { it.calories } else currentSelectedDay.calories

    val totalProteinConsumed: Int
        get() = if (selectedDayIndex == 18) foodItems.sumOf { it.protein } else currentSelectedDay.protein

    val totalCarbsConsumed: Int
        get() = if (selectedDayIndex == 18) foodItems.sumOf { it.carbs } else currentSelectedDay.carbs

    val totalFatConsumed: Int
        get() = if (selectedDayIndex == 18) foodItems.sumOf { it.fat } else currentSelectedDay.fat

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

    fun selectCalendarDay(index: Int) {
        val targetDay = _uiState.value.calendarDays.getOrNull(index)
        if (targetDay != null && !targetDay.isFuture) {
            _uiState.update { it.copy(selectedDayIndex = index.coerceIn(0, it.calendarDays.size - 1)) }
        }
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
            TrackerIconType.WATER -> openWaterTracker()
            else -> openComingSoonApp(tracker)
        }
    }

    // Do NOT open trackers when clicking Today card stats (display only)
    fun selectMetric(metric: TodayMetric?) {
        _uiState.update { it.copy(selectedMetric = metric) }
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

    fun openWaterTracker() {
        _uiState.update {
            it.copy(
                isWaterTrackerOpen = true,
                selectedTracker = null,
                selectedMetric = null
            )
        }
    }

    fun closeWaterTracker() {
        _uiState.update { it.copy(isWaterTrackerOpen = false) }
    }

    fun openComingSoonApp(tracker: AppTracker) {
        _uiState.update {
            it.copy(
                isComingSoonOpen = true,
                activeComingSoonApp = tracker,
                selectedTracker = null,
                selectedMetric = null
            )
        }
    }

    fun closeComingSoonApp() {
        _uiState.update {
            it.copy(
                isComingSoonOpen = false,
                activeComingSoonApp = null
            )
        }
    }

    // Toggle apps in My Apps and dynamically synchronize Today Card metrics
    fun toggleAppInMyApps(appId: String, isEnabled: Boolean) {
        _uiState.update { state ->
            val currentTrackers = state.trackers.toMutableList()
            val appExists = currentTrackers.any { it.id == appId }

            if (isEnabled && !appExists) {
                when (appId) {
                    "water" -> currentTrackers.add(
                        AppTracker(
                            id = "water",
                            title = "Water Tracking",
                            iconType = TrackerIconType.WATER,
                            currentValue = state.userWaterGlasses.toFloat(),
                            targetValue = 8f,
                            currentFormatted = "${state.userWaterGlasses}",
                            targetFormatted = "/ 8 glasses",
                            progress = (state.userWaterGlasses / 8f).coerceIn(0f, 1f),
                            progressColor = AccentBlue,
                            iconColor = AccentBlue,
                            iconBgLight = AccentBlueLight
                        )
                    )
                    "activity" -> currentTrackers.add(
                        AppTracker(
                            id = "activity",
                            title = "Activity Steps",
                            iconType = TrackerIconType.WORKOUT,
                            currentValue = state.userSteps.toFloat(),
                            targetValue = 10000f,
                            currentFormatted = "6.8k",
                            targetFormatted = "/ 10k steps",
                            progress = 0.68f,
                            progressColor = AccentOrange,
                            iconColor = AccentOrange,
                            iconBgLight = AccentOrangeLight
                        )
                    )
                    "workout" -> currentTrackers.add(
                        AppTracker(
                            id = "workout",
                            title = "Workout Log",
                            iconType = TrackerIconType.WORKOUT,
                            currentValue = 1f,
                            targetValue = 1f,
                            currentFormatted = "1",
                            targetFormatted = "/ 1 session",
                            progress = 1.0f,
                            progressColor = AccentPurple,
                            iconColor = AccentPurple,
                            iconBgLight = AccentPurpleLight
                        )
                    )
                    "fasting" -> currentTrackers.add(
                        AppTracker(
                            id = "fasting",
                            title = "Intermittent Fasting",
                            iconType = TrackerIconType.FASTING,
                            currentValue = 14f,
                            targetValue = 16f,
                            currentFormatted = "14",
                            targetFormatted = "/ 16 hrs",
                            progress = 0.875f,
                            progressColor = AccentOrange,
                            iconColor = AccentOrange,
                            iconBgLight = AccentOrangeLight
                        )
                    )
                    "meditation" -> currentTrackers.add(
                        AppTracker(
                            id = "meditation",
                            title = "Mindfulness & Rest",
                            iconType = TrackerIconType.MEDITATION,
                            currentValue = 15f,
                            targetValue = 20f,
                            currentFormatted = "15",
                            targetFormatted = "/ 20 mins",
                            progress = 0.75f,
                            progressColor = AccentGreen,
                            iconColor = AccentGreen,
                            iconBgLight = AccentGreenLight
                        )
                    )
                    "calorie" -> defaultTrackers.firstOrNull { it.id == "calorie" }?.let { currentTrackers.add(it) }
                    "habit" -> defaultTrackers.firstOrNull { it.id == "habit" }?.let { currentTrackers.add(it) }
                    "sleep" -> defaultTrackers.firstOrNull { it.id == "sleep" }?.let { currentTrackers.add(it) }
                }
            } else if (!isEnabled && appExists) {
                currentTrackers.removeAll { it.id == appId }
            }

            // Sync Today Metrics strictly from the currently active apps in My Apps!
            val activeIds = currentTrackers.map { it.id }.toSet()
            val updatedTodayMetrics = mutableListOf<TodayMetric>()

            if ("calorie" in activeIds) {
                val totalCalories = state.foodItems.sumOf { it.calories }
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_calories",
                        title = "Calories",
                        type = TodayMetricType.CALORIES,
                        primaryValue = NumberFormat.getNumberInstance(Locale.US).format(totalCalories),
                        secondaryValue = "/ ${state.calorieGoal} kcal"
                    )
                )
            }
            if ("habit" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_habits",
                        title = "Habits",
                        type = TodayMetricType.HABITS,
                        primaryValue = state.userHabitsDone.toString(),
                        secondaryValue = "/ 5 completed"
                    )
                )
            }
            if ("sleep" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_sleep",
                        title = "Sleep",
                        type = TodayMetricType.SLEEP,
                        primaryValue = if (state.userSleepHours % 1f == 0f) String.format(Locale.US, "%.0f", state.userSleepHours) else String.format(Locale.US, "%.1f", state.userSleepHours),
                        secondaryValue = "/ 8 hrs"
                    )
                )
            }
            if ("water" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_water",
                        title = "Water",
                        type = TodayMetricType.WATER,
                        primaryValue = state.userWaterGlasses.toString(),
                        secondaryValue = "/ 8 glasses"
                    )
                )
            }
            if ("activity" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_activity",
                        title = "Activity",
                        type = TodayMetricType.ACTIVITY,
                        primaryValue = NumberFormat.getNumberInstance(Locale.US).format(state.userSteps),
                        secondaryValue = "steps",
                        isSecondaryBold = true
                    )
                )
            }
            if ("workout" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_workout",
                        title = "Workouts",
                        type = TodayMetricType.WORKOUT,
                        primaryValue = state.userWorkoutsDone.toString(),
                        secondaryValue = "/ 1 session completed"
                    )
                )
            }
            if ("fasting" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_fasting",
                        title = "Fasting",
                        type = TodayMetricType.FASTING,
                        primaryValue = "${state.userFastingHours.toInt()}h",
                        secondaryValue = "/ 16h target"
                    )
                )
            }
            if ("meditation" in activeIds) {
                updatedTodayMetrics.add(
                    TodayMetric(
                        id = "today_meditation",
                        title = "Mindfulness",
                        type = TodayMetricType.MEDITATION,
                        primaryValue = "${state.userMeditationMinutes}m",
                        secondaryValue = "/ 20m target"
                    )
                )
            }

            state.copy(
                trackers = currentTrackers,
                todayMetrics = updatedTodayMetrics
            )
        }
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
            delay(1400)
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
