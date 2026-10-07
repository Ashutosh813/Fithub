package com.example.ui.calai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalAiDayData
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CalAiHomeScreen(
    foodItems: List<FoodItem>,
    totalCalories: Int,
    calorieGoal: Int,
    totalProtein: Int,
    proteinGoal: Int,
    totalCarbs: Int,
    carbsGoal: Int,
    totalFat: Int,
    fatGoal: Int,
    currentDayData: CalAiDayData,
    onOpenScanner: () -> Unit,
    onAddFoodClick: (MealType) -> Unit,
    onDeleteFood: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Mode: false = "eaten" (consumed), true = "left" (remaining)
    var showLeftMode by remember { mutableStateOf(false) }

    // Page 0: Calories + Protein/Carbs/Fats, Page 1: Fiber/Sugar/Sodium + Health score
    val pagerState = rememberPagerState(pageCount = { 2 })

    // Food item pending deletion confirmation
    var foodItemToDelete by remember { mutableStateOf<FoodItem?>(null) }

    // Resolve stats: if viewing Today, use live sums; otherwise use selected past day's dummy stats
    val displayCalories = if (currentDayData.isToday) totalCalories else currentDayData.calories
    val displayProtein = if (currentDayData.isToday) totalProtein else currentDayData.protein
    val displayCarbs = if (currentDayData.isToday) totalCarbs else currentDayData.carbs
    val displayFat = if (currentDayData.isToday) totalFat else currentDayData.fat
    val displayMeals = if (currentDayData.isToday) foodItems else currentDayData.meals

    val remainingCalories = (calorieGoal - displayCalories).coerceAtLeast(0)
    val remainingProtein = (proteinGoal - displayProtein).coerceAtLeast(0)
    val remainingCarbs = (carbsGoal - displayCarbs).coerceAtLeast(0)
    val remainingFat = (fatGoal - displayFat).coerceAtLeast(0)

    val calorieProgress = (displayCalories.toFloat() / calorieGoal.toFloat()).coerceIn(0f, 1f)
    val proteinProgress = (displayProtein.toFloat() / proteinGoal.toFloat()).coerceIn(0f, 1f)
    val carbsProgress = (displayCarbs.toFloat() / carbsGoal.toFloat()).coerceIn(0f, 1f)
    val fatProgress = (displayFat.toFloat() / fatGoal.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .testTag("cal_ai_home_content")
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        // Nutrition Cards Horizontal Pager (Page 1: Calories & Macros, Page 2: Micro nutrients & Health score)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            if (page == 0) {
                // PAGE 1: Big Hero Calories Card + 3 Macro Cards (Protein, Carbs, Fats)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Big Hero Calories Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(26.dp),
                                ambientColor = Color(0x08000000),
                                spotColor = Color(0x12000000)
                            )
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(26.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.05f)),
                                onClick = { showLeftMode = !showLeftMode }
                            )
                            .padding(horizontal = 26.dp, vertical = 22.dp)
                            .testTag("calorie_hero_card")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentDayData.isToday) "Today's Goal" else "${currentDayData.dayName}'s Goal",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMuted,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )

                                if (showLeftMode) {
                                    Text(
                                        text = NumberFormat.getNumberInstance(Locale.US).format(remainingCalories),
                                        fontFamily = InterFontFamily,
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-1).sp,
                                        color = TextMain
                                    )
                                    // Full black text for "Calories left"
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Calories ")
                                            withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                                                append("left")
                                            }
                                        },
                                        fontFamily = InterFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111115)
                                    )
                                } else {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = NumberFormat.getNumberInstance(Locale.US).format(displayCalories),
                                            fontFamily = InterFontFamily,
                                            fontSize = 38.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = (-1).sp,
                                            color = TextMain
                                        )
                                        Text(
                                            text = "/$calorieGoal",
                                            fontFamily = InterFontFamily,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextMuted,
                                            modifier = Modifier.padding(bottom = 5.dp, start = 4.dp)
                                        )
                                    }
                                    // Full black text for "Calories eaten"
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Calories ")
                                            withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                                                append("eaten")
                                            }
                                        },
                                        fontFamily = InterFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111115)
                                    )
                                }
                            }

                            // Circular Progress Ring with Flame in Center
                            Box(
                                modifier = Modifier.size(96.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFFF2F3F7),
                                    strokeWidth = 11.dp,
                                    trackColor = Color.Transparent
                                )
                                CircularProgressIndicator(
                                    progress = { calorieProgress },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF111115),
                                    strokeWidth = 11.dp,
                                    strokeCap = StrokeCap.Round,
                                    trackColor = Color.Transparent
                                )
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF6F7FA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = Color(0xFF111115),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3 Macro Cards Row (Protein, Carbs, Fats) - Full Black "eaten" Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RefinedMacroCard(
                            title = "Protein",
                            consumed = displayProtein,
                            target = proteinGoal,
                            remaining = remainingProtein,
                            progress = proteinProgress,
                            showLeftMode = showLeftMode,
                            accentColor = Color(0xFFFF453A),
                            trackColor = Color(0xFFFFECEC),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        RefinedMacroCard(
                            title = "Carbs",
                            consumed = displayCarbs,
                            target = carbsGoal,
                            remaining = remainingCarbs,
                            progress = carbsProgress,
                            showLeftMode = showLeftMode,
                            accentColor = Color(0xFF8B5CF6),
                            trackColor = Color(0xFFF3E8FF),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        RefinedMacroCard(
                            title = "Fat",
                            consumed = displayFat,
                            target = fatGoal,
                            remaining = remainingFat,
                            progress = fatProgress,
                            showLeftMode = showLeftMode,
                            accentColor = Color(0xFF10B981),
                            trackColor = Color(0xFFD1FAE5),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                // PAGE 2: Fiber, Sugar, Sodium + Health Score Card
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RefinedMacroCard(
                            title = "Fiber",
                            consumed = currentDayData.fiber,
                            target = 38,
                            remaining = (38 - currentDayData.fiber).coerceAtLeast(0),
                            progress = (currentDayData.fiber / 38f).coerceIn(0f, 1f),
                            showLeftMode = showLeftMode,
                            accentColor = Color(0xFF8B5CF6),
                            trackColor = Color(0xFFF3E8FF),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        RefinedMacroCard(
                            title = "Sugar",
                            consumed = currentDayData.sugar,
                            target = 65,
                            remaining = (65 - currentDayData.sugar).coerceAtLeast(0),
                            progress = (currentDayData.sugar / 65f).coerceIn(0f, 1f),
                            showLeftMode = showLeftMode,
                            accentColor = Color(0xFFEC4899),
                            trackColor = Color(0xFFFCE7F3),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        RefinedMacroCard(
                            title = "Sodium",
                            consumed = currentDayData.sodium,
                            target = 2300,
                            remaining = (2300 - currentDayData.sodium).coerceAtLeast(0),
                            progress = (currentDayData.sodium / 2300f).coerceIn(0f, 1f),
                            showLeftMode = showLeftMode,
                            unit = "mg",
                            accentColor = Color(0xFF0EA5E9),
                            trackColor = Color(0xFFE0F2FE),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Health Score Card (Clean, no card inside card)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(26.dp),
                                ambientColor = Color(0x08000000),
                                spotColor = Color(0x12000000)
                            )
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(26.dp))
                            .padding(22.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Health score",
                                    fontFamily = InterFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                Text(
                                    text = "${currentDayData.healthScore}/10",
                                    fontFamily = InterFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { currentDayData.healthScore / 10f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF111115),
                                trackColor = Color(0xFFF2F3F7)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentDayData.healthAdvice,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                color = TextMuted,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pager Dots Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (isSelected) 7.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF111115) else Color(0xFFD1D1D6))
                        .clickable {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Meals Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (currentDayData.isToday) "Today's Meals" else "${currentDayData.dayName}'s Meals",
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.4).sp,
                    color = TextMain
                )
                if (!currentDayData.isToday) {
                    Text(
                        text = "Past log • Read only",
                        fontFamily = InterFontFamily,
                        fontSize = 11.5.sp,
                        color = TextMuted
                    )
                }
            }
            Text(
                text = "${displayMeals.size} logged",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meals List by Category
        MealType.entries.forEach { mealType ->
            val itemsForMeal = displayMeals.filter { it.mealType == mealType }
            TodaysMealCard(
                mealType = mealType,
                items = itemsForMeal,
                isEditable = currentDayData.isToday,
                onAddClick = { onAddFoodClick(mealType) },
                onDeleteClick = { item -> foodItemToDelete = item }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(36.dp))
    }

    // Confirmation Dialog before deleting any item
    if (foodItemToDelete != null) {
        val item = foodItemToDelete!!
        AlertDialog(
            onDismissRequest = { foodItemToDelete = null },
            title = {
                Text(
                    text = "Delete Food Item?",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextMain
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${item.name}\" (${item.calories} kcal) from your log?",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteFood(item.id)
                        foodItemToDelete = null
                    }
                ) {
                    Text(
                        "Delete",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { foodItemToDelete = null }) {
                    Text(
                        "Cancel",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

// Refined Macro Card with full black "eaten/left" labels
@Composable
fun RefinedMacroCard(
    title: String,
    consumed: Int,
    target: Int,
    remaining: Int,
    progress: Float,
    showLeftMode: Boolean,
    unit: String = "g",
    accentColor: Color,
    trackColor: Color,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLargeNumber = consumed >= 1000 || target >= 1000 || remaining >= 1000
    val valFontSize = if (isLargeNumber) 13.5.sp else 16.5.sp
    val targetFontSize = if (isLargeNumber) 9.5.sp else 11.sp

    Box(
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x06000000),
                spotColor = Color(0x0C000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.05f)),
                onClick = onCardClick
            )
            .padding(vertical = 15.dp, horizontal = 10.dp)
            .testTag("macro_card_${title.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Value display
            if (showLeftMode) {
                Text(
                    text = "$remaining$unit",
                    fontFamily = InterFontFamily,
                    fontSize = valFontSize,
                    fontWeight = FontWeight.Black,
                    color = TextMain,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                // Full black color for "$title left"
                Text(
                    text = "$title left",
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111115),
                    lineHeight = 13.sp
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = valFontSize,
                                color = TextMain
                            )
                        ) {
                            append("$consumed")
                        }
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = targetFontSize,
                                color = TextMuted
                            )
                        ) {
                            append("/$target$unit")
                        }
                    },
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                // Full black color for "$title eaten" (as requested by user!)
                Text(
                    text = "$title eaten",
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111115),
                    lineHeight = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mini Circular Macro Ring
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.End),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = trackColor,
                    strokeWidth = 5.5.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = accentColor,
                    strokeWidth = 5.5.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = Color.Transparent
                )
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
        }
    }
}

// Today's Meals Card: clicking reveals items with light lightning green food titles!
@Composable
fun TodaysMealCard(
    mealType: MealType,
    items: List<FoodItem>,
    isEditable: Boolean,
    onAddClick: () -> Unit,
    onDeleteClick: (FoodItem) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val sectionCalories = items.sumOf { it.calories }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x06000000),
                spotColor = Color(0x0E000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(22.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(horizontal = 18.dp, vertical = 16.dp)
            .testTag("todays_meal_card_${mealType.name.lowercase()}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Meal Title & Flame Calories indicator
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = mealType.displayName,
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFEA580C),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (sectionCalories > 0) "$sectionCalories kcal" else "0 kcal",
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFC2410C)
                        )
                    }
                }

                // Right: Food emoji badges + circular add button (only if isEditable)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Small food badges
                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                        MealThumbnailCircle(Color(0xFFFED7AA), "🥗")
                        MealThumbnailCircle(Color(0xFFBFDBFE), "🍳")
                        MealThumbnailCircle(Color(0xFFBBF7D0), "🥪")
                    }

                    // Only show "+" button if on PRESENT DAY (not past dates)
                    if (isEditable) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3F4F6))
                                .clickable(onClick = onAddClick)
                                .testTag("add_meal_${mealType.name.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add Food",
                                tint = Color(0xFF111115),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Expandable items view: Food title in LIGHT LIGHTNING GREEN (Color(0xFF10B981) / Color(0xFF059669))
            AnimatedVisibility(
                visible = isExpanded && items.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        if (index > 0) {
                            HorizontalDivider(
                                color = Color(0x0C000000),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                // Light lightning green food name!
                                Text(
                                    text = item.name,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    text = "${item.portionDescription} • P: ${item.protein}g C: ${item.carbs}g F: ${item.fat}g",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "${item.calories} kcal",
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                // Delete button with confirmation
                                if (isEditable) {
                                    IconButton(
                                        onClick = { onDeleteClick(item) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "Delete",
                                            tint = TextMuted,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MealThumbnailCircle(backgroundColor: Color, emoji: String) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 13.sp)
    }
}
