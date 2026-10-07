package com.example.ui.calai

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiScanResult
import com.example.model.CalAiDayData
import com.example.model.CalAiTab
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.model.SampleFoodPreset
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalAiScreen(
    currentTab: CalAiTab,
    onTabSelected: (CalAiTab) -> Unit,
    foodItems: List<FoodItem>,
    totalCalories: Int,
    calorieGoal: Int,
    totalProtein: Int,
    proteinGoal: Int,
    totalCarbs: Int,
    carbsGoal: Int,
    totalFat: Int,
    fatGoal: Int,
    currentSelectedDay: CalAiDayData,
    calendarDays: List<CalAiDayData>,
    selectedDayIndex: Int,
    onSelectDay: (Int) -> Unit,
    isCameraScanning: Boolean,
    scanResult: AiScanResult?,
    isAddFoodSheetOpen: Boolean,
    selectedMealTypeForAdd: MealType,
    onClose: () -> Unit,
    onTriggerScan: (SampleFoodPreset?) -> Unit,
    onPortionChange: (Float) -> Unit,
    onLogScan: () -> Unit,
    onDismissScan: () -> Unit,
    onOpenAddFood: (MealType) -> Unit,
    onCloseAddFood: () -> Unit,
    onAddFood: (name: String, mealType: MealType, calories: Int, protein: Int, carbs: Int, fat: Int, portion: String) -> Unit,
    onDeleteFood: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Hierarchical back handling: if in Camera or Progress or dialog, back returns to previous page within Cal AI!
    BackHandler {
        when {
            isAddFoodSheetOpen -> onCloseAddFood()
            scanResult != null -> onDismissScan()
            currentTab != CalAiTab.HOME -> onTabSelected(CalAiTab.HOME)
            else -> onClose() // Only returns to FitHub Home if already on Home tab!
        }
    }

    // Physical finger-tracking pull-to-minimize behavior (identical to Manage Sheet / Bottom Sheet!)
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val animatedOffsetY by animateFloatAsState(
        targetValue = dragOffsetY,
        animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow),
        label = "pull_to_minimize"
    )

    val draggableState = rememberDraggableState { delta ->
        dragOffsetY = (dragOffsetY + delta).coerceAtLeast(0f)
    }

    // Scroll to current selected day initially
    val calendarListState = rememberLazyListState()
    LaunchedEffect(Unit) {
        if (selectedDayIndex > 3) {
            calendarListState.scrollToItem(selectedDayIndex - 3)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(0, animatedOffsetY.roundToInt()) }
            .testTag("cal_ai_full_screen"),
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .draggable(
                        state = draggableState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            if (dragOffsetY > 200f || velocity > 900f) {
                                onClose()
                            } else {
                                dragOffsetY = 0f
                            }
                        }
                    )
            ) {
                // Top drag handle - physical pull-down follows user's hand
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClose)
                        .padding(top = 10.dp, bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFC7C7CC))
                    )
                }

                when (currentTab) {
                    CalAiTab.HOME -> {
                        // Header Row on Home (Date title without writing "Today", streak badge on right)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${currentSelectedDay.dayName}, Oct ${currentSelectedDay.dayNumber}",
                                    fontFamily = InterFontFamily,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextMain
                                )
                                Text(
                                    text = if (currentSelectedDay.isToday) "Daily nutrition & macro intake" else "Past log • Read only",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TextMuted
                                )
                            }

                            // Streak Badge Pill (🔥 14)
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF7ED))
                                    .border(0.5.dp, Color(0xFFFFEDD5), CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = AccentOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "14",
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Scrollable 21-Day Calendar Pills Strip - ONLY shown on HOME tab
                        LazyRow(
                            state = calendarListState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                        ) {
                            itemsIndexed(calendarDays) { index, day ->
                                val isSelected = index == selectedDayIndex
                                val isFuture = day.isFuture

                                // Tick mark is shown whenever calorie intake goal is completed (reached)
                                val dayCalories = if (day.isToday) totalCalories else day.calories
                                val dayGoal = if (day.isToday) calorieGoal else day.calorieGoal
                                val isCalorieIntakeComplete = !isFuture && (dayCalories >= dayGoal)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier
                                        .alpha(if (isFuture) 0.38f else 1f)
                                        .clip(RoundedCornerShape(26.dp))
                                        .background(
                                            when {
                                                isSelected -> Color(0xFFF3F4F6)
                                                isFuture -> Color(0xFFF9FAFB)
                                                else -> Color.White
                                            }
                                        )
                                        // Present day is distinguished with light outline (NO "Today" text added)
                                        .border(
                                            width = when {
                                                isSelected -> 1.5.dp
                                                day.isToday -> 1.5.dp
                                                else -> 0.5.dp
                                            },
                                            color = when {
                                                isSelected -> Color(0xFF111115)
                                                day.isToday -> Color(0xFF111115).copy(alpha = 0.38f)
                                                isFuture -> Color(0x0C000000)
                                                else -> Color(0x18000000)
                                            },
                                            shape = RoundedCornerShape(26.dp)
                                        )
                                        .clickable(enabled = !isFuture) { onSelectDay(index) }
                                        .padding(horizontal = 10.dp, vertical = 10.dp)
                                        .testTag("calendar_day_$index")
                                ) {
                                    Text(
                                        text = day.dayName,
                                        fontFamily = InterFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected || day.isToday) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isFuture -> Color(0xFFB0B0B8)
                                            isSelected -> TextMain
                                            else -> TextMuted
                                        }
                                    )

                                    // Date circle with tick badge if calorie intake completed
                                    Box(
                                        modifier = Modifier.size(36.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isSelected -> Color(0xFF111115)
                                                        isFuture -> Color(0xFFF3F4F6)
                                                        else -> Color(0xFFF9FAFB)
                                                    }
                                                )
                                                .border(
                                                    width = if (day.isToday && !isSelected) 1.5.dp else 0.5.dp,
                                                    color = if (day.isToday && !isSelected) Color(0xFF111115).copy(alpha = 0.32f) else Color(0x14000000),
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day.dayNumber,
                                                fontFamily = InterFontFamily,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isSelected -> Color.White
                                                    isFuture -> Color(0xFFB0B0B8)
                                                    else -> TextMain
                                                }
                                            )
                                        }

                                        // Tick mark shown whenever calorie intake goal is completed
                                        if (isCalorieIntakeComplete && !isFuture) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .size(13.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF10B981))
                                                    .border(1.dp, Color.White, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Check,
                                                    contentDescription = "Calorie Goal Complete",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(9.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    CalAiTab.PROGRESS -> {
                        // Header on Progress tab - NO calendar strip!
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Progress",
                                    fontFamily = InterFontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = (-0.4).sp,
                                    color = TextMain
                                )
                                Text(
                                    text = "Weekly intake & macronutrient trends",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TextMuted
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF7ED))
                                    .border(0.5.dp, Color(0xFFFFEDD5), CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = AccentOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "14",
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain
                                    )
                                }
                            }
                        }
                    }
                    CalAiTab.CAMERA -> {
                        // Camera tab handles its own full-screen viewfinder
                    }
                }
            }
        },
        bottomBar = {
            CleanCalBottomBar(
                selectedTab = currentTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "cal_ai_tab_content",
                modifier = Modifier.fillMaxSize()
            ) { tab ->
                when (tab) {
                    CalAiTab.HOME -> {
                        CalAiHomeScreen(
                            foodItems = foodItems,
                            totalCalories = totalCalories,
                            calorieGoal = calorieGoal,
                            totalProtein = totalProtein,
                            proteinGoal = proteinGoal,
                            totalCarbs = totalCarbs,
                            carbsGoal = carbsGoal,
                            totalFat = totalFat,
                            fatGoal = fatGoal,
                            currentDayData = currentSelectedDay,
                            onOpenScanner = { onTabSelected(CalAiTab.CAMERA) },
                            onAddFoodClick = {
                                if (currentSelectedDay.isToday) onOpenAddFood(it)
                            },
                            onDeleteFood = {
                                if (currentSelectedDay.isToday) onDeleteFood(it)
                            }
                        )
                    }
                    CalAiTab.CAMERA -> {
                        CalAiCameraScreen(
                            isScanning = isCameraScanning,
                            scanResult = scanResult,
                            onTriggerScan = onTriggerScan,
                            onPortionChange = onPortionChange,
                            onLogScan = onLogScan,
                            onDismissScan = onDismissScan
                        )
                    }
                    CalAiTab.PROGRESS -> {
                        CalAiProgressScreen(
                            totalCaloriesToday = totalCalories,
                            calorieGoal = calorieGoal,
                            totalProtein = totalProtein,
                            totalCarbs = totalCarbs,
                            totalFat = totalFat
                        )
                    }
                }
            }

            // Add Food Dialog / Sheet
            if (isAddFoodSheetOpen) {
                CalAiAddFoodSheet(
                    targetMealType = selectedMealTypeForAdd,
                    onDismiss = onCloseAddFood,
                    onAddFood = onAddFood
                )
            }
        }
    }
}

@Composable
fun CleanCalBottomBar(
    selectedTab: CalAiTab,
    onTabSelected: (CalAiTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val navShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = navShape,
                ambientColor = Color(0x0F000000),
                spotColor = Color(0x18000000)
            )
            .clip(navShape)
            .background(Color.White)
            .border(0.5.dp, BorderColor, navShape)
            .navigationBarsPadding()
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .testTag("clean_cal_bottom_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.1f)),
                        onClick = { onTabSelected(CalAiTab.HOME) }
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home",
                    tint = if (selectedTab == CalAiTab.HOME) Color(0xFF111115) else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Home",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == CalAiTab.HOME) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedTab == CalAiTab.HOME) Color(0xFF111115) else TextMuted
                )
            }

            // Round Circular Camera Button (ONLY camera icon, no text)
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = Color(0x1E000000), spotColor = Color(0x32000000))
                    .clip(CircleShape)
                    .background(Color(0xFF111115))
                    .clickable { onTabSelected(CalAiTab.CAMERA) }
                    .testTag("round_camera_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Progress Tab (Replaced Analytics with Progress)
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.1f)),
                        onClick = { onTabSelected(CalAiTab.PROGRESS) }
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.QueryStats,
                    contentDescription = "Progress",
                    tint = if (selectedTab == CalAiTab.PROGRESS) Color(0xFF111115) else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Progress",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == CalAiTab.PROGRESS) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedTab == CalAiTab.PROGRESS) Color(0xFF111115) else TextMuted
                )
            }
        }
    }
}
