package com.example.ui.calai

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiScanResult
import com.example.model.CalAiTab
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.model.SampleFoodPreset
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

data class CalendarDayItem(
    val dayName: String,
    val dayNumber: String,
    val isToday: Boolean = false
)

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
    BackHandler { onClose() }

    val daysOfWeek = remember {
        listOf(
            CalendarDayItem("Fri", "02"),
            CalendarDayItem("Sat", "03"),
            CalendarDayItem("Sun", "04"),
            CalendarDayItem("Mon", "05"),
            CalendarDayItem("Tue", "06"),
            CalendarDayItem("Wed", "07", isToday = true),
            CalendarDayItem("Thu", "08")
        )
    }
    var selectedDayIndex by remember { mutableIntStateOf(5) } // Wed 07

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("cal_ai_full_screen"),
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
            ) {
                // Top drag handle indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFD1D1D6))
                    )
                }

                // Clean Header Row (No "Cal AI" text, has Close & Streak Pill)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Close button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF2F2F7))
                            .clickable(onClick = onClose)
                            .testTag("close_cal_ai_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Close",
                            tint = TextMain,
                            modifier = Modifier.size(24.dp)
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

                // Days of the Week Calendar Row (Matching the 4 screenshots)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    daysOfWeek.forEachIndexed { index, day ->
                        val isSelected = index == selectedDayIndex
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedDayIndex = index }
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = day.dayName,
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TextMain else TextMuted
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(2.dp, Color(0xFF111115), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.dayNumber,
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFAFAFA))
                                        .border(1.dp, Color(0xFFE5E7EB), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.dayNumber,
                                        fontFamily = InterFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Clean Bottom Bar: Home on left, ONLY round Camera button in center, Analytics on right
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
                            onOpenScanner = { onTabSelected(CalAiTab.CAMERA) },
                            onAddFoodClick = onOpenAddFood,
                            onDeleteFood = onDeleteFood
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
            // Home Tab (renamed from Dashboard)
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
                    .shadow(elevation = 6.dp, shape = CircleShape, ambientColor = Color(0x14000000), spotColor = Color(0x28000000))
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

            // Analytics Tab
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
                    contentDescription = "Analytics",
                    tint = if (selectedTab == CalAiTab.PROGRESS) Color(0xFF111115) else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Analytics",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    fontWeight = if (selectedTab == CalAiTab.PROGRESS) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedTab == CalAiTab.PROGRESS) Color(0xFF111115) else TextMuted
                )
            }
        }
    }
}
