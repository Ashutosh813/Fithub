package com.example.ui.calai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.rounded.BakeryDining
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DinnerDining
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material.icons.rounded.LocalDining
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodItem
import com.example.model.MealType
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.ProgressTrackColor
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

    val remainingCalories = (calorieGoal - totalCalories).coerceAtLeast(0)
    val remainingProtein = (proteinGoal - totalProtein).coerceAtLeast(0)
    val remainingCarbs = (carbsGoal - totalCarbs).coerceAtLeast(0)
    val remainingFat = (fatGoal - totalFat).coerceAtLeast(0)

    val calorieProgress = (totalCalories.toFloat() / calorieGoal.toFloat()).coerceIn(0f, 1f)
    val proteinProgress = (totalProtein.toFloat() / proteinGoal.toFloat()).coerceIn(0f, 1f)
    val carbsProgress = (totalCarbs.toFloat() / carbsGoal.toFloat()).coerceIn(0f, 1f)
    val fatProgress = (totalFat.toFloat() / fatGoal.toFloat()).coerceIn(0f, 1f)

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
                                shape = RoundedCornerShape(28.dp),
                                ambientColor = Color(0x08000000),
                                spotColor = Color(0x10000000)
                            )
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(28.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = Color.Black.copy(alpha = 0.05f)),
                                onClick = { showLeftMode = !showLeftMode }
                            )
                            .padding(horizontal = 28.dp, vertical = 24.dp)
                            .testTag("calorie_hero_card")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                if (showLeftMode) {
                                    Text(
                                        text = NumberFormat.getNumberInstance(Locale.US).format(remainingCalories),
                                        fontFamily = InterFontFamily,
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.Bold,
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
                                        fontSize = 14.sp,
                                        color = TextMuted
                                    )
                                } else {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = NumberFormat.getNumberInstance(Locale.US).format(totalCalories),
                                            fontFamily = InterFontFamily,
                                            fontSize = 42.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = (-1).sp,
                                            color = TextMain
                                        )
                                        Text(
                                            text = "/$calorieGoal",
                                            fontFamily = InterFontFamily,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextMuted,
                                            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
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
                                        fontSize = 14.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // Circular Progress Ring with Flame in Center
                            Box(
                                modifier = Modifier.size(108.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFFF2F3F7),
                                    strokeWidth = 12.dp,
                                    trackColor = Color.Transparent
                                )
                                CircularProgressIndicator(
                                    progress = { calorieProgress },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFF1C1C1E),
                                    strokeWidth = 12.dp,
                                    strokeCap = StrokeCap.Round,
                                    trackColor = Color.Transparent
                                )
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF6F7FB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = Color(0xFF1C1C1E),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3 Macro Cards Row (Protein, Carbs, Fats)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MacroNutrientCard(
                            title = "Protein",
                            consumed = totalProtein,
                            target = proteinGoal,
                            remaining = remainingProtein,
                            unit = "g",
                            progress = proteinProgress,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.DinnerDining,
                            iconTint = Color(0xFFE25555),
                            iconBg = Color(0xFFFFECEC),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        MacroNutrientCard(
                            title = "Carbs",
                            consumed = totalCarbs,
                            target = carbsGoal,
                            remaining = remainingCarbs,
                            unit = "g",
                            progress = carbsProgress,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.BakeryDining,
                            iconTint = Color(0xFFD97706),
                            iconBg = Color(0xFFFEF3C7),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        MacroNutrientCard(
                            title = "Fats",
                            consumed = totalFat,
                            target = fatGoal,
                            remaining = remainingFat,
                            unit = "g",
                            progress = fatProgress,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.Eco,
                            iconTint = Color(0xFF3B82F6),
                            iconBg = Color(0xFFEFF6FF),
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
                    // 3 Secondary Micro Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MacroNutrientCard(
                            title = "Fiber",
                            consumed = 24,
                            target = 38,
                            remaining = 14,
                            unit = "g",
                            progress = 24f / 38f,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.Spa,
                            iconTint = Color(0xFF8B5CF6),
                            iconBg = Color(0xFFF3E8FF),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        MacroNutrientCard(
                            title = "Sugar",
                            consumed = 32,
                            target = 65,
                            remaining = 33,
                            unit = "g",
                            progress = 32f / 65f,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.Egg,
                            iconTint = Color(0xFFEC4899),
                            iconBg = Color(0xFFFCE7F3),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                        MacroNutrientCard(
                            title = "Sodium",
                            consumed = 1250,
                            target = 2300,
                            remaining = 1050,
                            unit = "mg",
                            progress = 1250f / 2300f,
                            showLeftMode = showLeftMode,
                            icon = Icons.Rounded.WaterDrop,
                            iconTint = Color(0xFF0EA5E9),
                            iconBg = Color(0xFFE0F2FE),
                            onCardClick = { showLeftMode = !showLeftMode },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Health Score Card (matching screenshot 3 & 4)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(28.dp),
                                ambientColor = Color(0x08000000),
                                spotColor = Color(0x10000000)
                            )
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(28.dp))
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
                                    text = "8/10",
                                    fontFamily = InterFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { 0.8f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF111115),
                                trackColor = Color(0xFFF2F3F7)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Carbs and fat are on track. You're maintaining a steady calorie deficit which supports clean fat loss and optimal recovery.",
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

        // Pager Dots Indicator (matching screenshots)
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

        // "Recently uploaded" / Today's Meals Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently uploaded",
                fontFamily = InterFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp,
                color = TextMain
            )
            Text(
                text = "${foodItems.size} items",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meals List by Category
        MealType.entries.forEach { mealType ->
            val itemsForMeal = foodItems.filter { it.mealType == mealType }
            CleanMealSectionCard(
                mealType = mealType,
                items = itemsForMeal,
                onAddClick = { onAddFoodClick(mealType) },
                onDeleteClick = onDeleteFood
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun MacroNutrientCard(
    title: String,
    consumed: Int,
    target: Int,
    remaining: Int,
    unit: String,
    progress: Float,
    showLeftMode: Boolean,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
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
            .padding(vertical = 16.dp, horizontal = 10.dp)
            .testTag("macro_card_${title.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (showLeftMode) {
                Text(
                    text = "$remaining$unit",
                    fontFamily = InterFontFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Text(
                    text = buildAnnotatedString {
                        append("$title ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextMain)) {
                            append("left")
                        }
                    },
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$consumed",
                        fontFamily = InterFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "/$target$unit",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        append("$title ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextMain)) {
                            append("eaten")
                        }
                    },
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mini circular progress ring with icon inside
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF2F3F7),
                    strokeWidth = 7.dp,
                    trackColor = Color.Transparent
                )
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF2C2C2E),
                    strokeWidth = 7.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = Color.Transparent
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CleanMealSectionCard(
    mealType: MealType,
    items: List<FoodItem>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit
) {
    val sectionCalories = items.sumOf { it.calories }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x06000000),
                spotColor = Color(0x0D000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(22.dp))
            .padding(18.dp)
            .testTag("meal_section_${mealType.name.lowercase()}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = mealType.displayName,
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "$sectionCalories kcal logged",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFF2F3F7))
                        .clickable(onClick = onAddClick)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("add_to_${mealType.name.lowercase()}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add Food",
                            tint = TextMain,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Add",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMain
                        )
                    }
                }
            }

            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items.forEach { item ->
                        CleanFoodItemRow(item = item, onDelete = { onDeleteClick(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun CleanFoodItemRow(
    item: FoodItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9FAFB))
            .border(0.5.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${item.portionDescription} • ${item.time}",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CleanMacroPill(text = "P: ${item.protein}g")
                CleanMacroPill(text = "C: ${item.carbs}g")
                CleanMacroPill(text = "F: ${item.fat}g")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${item.calories} kcal",
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete Food",
                    tint = TextMuted.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CleanMacroPill(text: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White)
            .border(0.5.dp, BorderColor, CircleShape)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontFamily = InterFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4B5563)
        )
    }
}
