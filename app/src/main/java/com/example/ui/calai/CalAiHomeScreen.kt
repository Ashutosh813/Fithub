package com.example.ui.calai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
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
    // Tapping on ANY card toggles between "eaten" and "left"
    var showLeftMode by remember { mutableStateOf(false) }

    // Page indicator (Page 0: Calories + Protein/Carbs/Fats, Page 1: Fiber/Sugar/Sodium + Health score)
    val pagerState = rememberPagerState(pageCount = { 2 })

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
                    // Big Hero Calories Card (Tappable to toggle eaten vs left)
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
                                    text = "Today's Goal",
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
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Calories ")
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextMain)) {
                                                append("left")
                                            }
                                        },
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.5.sp,
                                        color = TextMuted
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
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Calories ")
                                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextMain)) {
                                                append("eaten")
                                            }
                                        },
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.5.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // Circular Progress Ring with Flame in Center (matching image 3)
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

                    // 3 Macro Cards Row (Protein, Carbs, Fats) - Refined font sizing & alignment
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
                            accentColor = Color(0xFFE25555),
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

                    // Health Score Card (matching screenshot 3)
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

        // "Today's Meals" Section (Replaces "Recently uploaded" - from image 3 & 4)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Meals",
                fontFamily = InterFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.4).sp,
                color = TextMain
            )
            Text(
                text = "${displayMeals.size} logged",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meals List by Category - Styled like uploaded image 3 & 4
        MealType.entries.forEach { mealType ->
            val itemsForMeal = displayMeals.filter { it.mealType == mealType }
            TodaysMealCard(
                mealType = mealType,
                items = itemsForMeal,
                onAddClick = { onAddFoodClick(mealType) },
                onDeleteClick = onDeleteFood
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

// Refined Macro Card with perfect font alignment and circle indicator (image 3)
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
    Box(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x06000000),
                spotColor = Color(0x0C000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.05f)),
                onClick = onCardClick
            )
            .padding(vertical = 16.dp, horizontal = 12.dp)
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
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
                Text(
                    text = "$title left",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$consumed",
                        fontFamily = InterFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                    Text(
                        text = "/$target$unit",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
                Text(
                    text = "$title eaten",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini circular progress ring with accent dot
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF2F3F7),
                    strokeWidth = 6.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = accentColor,
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = Color.Transparent
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
        }
    }
}

// Today's Meals Card (designed directly from uploaded image 3 & 4)
@Composable
fun TodaysMealCard(
    mealType: MealType,
    items: List<FoodItem>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val sectionCalories = items.sumOf { it.calories }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
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
                // Left: Meal Title & Flame Calories Pill Badge (matching image 4)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = mealType.displayName,
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )

                    // Peach/Orange Flame Pill Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF7ED))
                            .border(0.5.dp, Color(0xFFFFEDD5), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFEA580C),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (sectionCalories > 0) "$sectionCalories Kcal" else "0 Kcal",
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC2410C)
                            )
                        }
                    }
                }

                // Right: Overlapping circular meal thumbnails + circular `+` button (matching image 4)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-10).dp)
                ) {
                    // 3 Overlapping food plate mock visual thumbnails
                    MealThumbnailCircle(Color(0xFFFED7AA), "🥗")
                    MealThumbnailCircle(Color(0xFFBFDBFE), "🍳")
                    MealThumbnailCircle(Color(0xFFBBF7D0), "🥪")

                    Spacer(modifier = Modifier.width(16.dp))

                    // Plus button in circular capsule (matching image 4)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(2.dp, CircleShape, ambientColor = Color(0x10000000))
                            .clip(CircleShape)
                            .background(Color(0xFFF9FAFB))
                            .border(0.75.dp, Color(0x18000000), CircleShape)
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

            // Expandable items view
            AnimatedVisibility(
                visible = isExpanded && items.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF9FAFB))
                                .border(0.5.dp, BorderColor, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMain
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
                                IconButton(
                                    onClick = { onDeleteClick(item.id) },
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

@Composable
fun MealThumbnailCircle(backgroundColor: Color, emoji: String) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .shadow(2.dp, CircleShape, ambientColor = Color(0x14000000))
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 15.sp)
    }
}
