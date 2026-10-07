package com.example.ui.calai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun CalAiProgressScreen(
    totalCaloriesToday: Int,
    calorieGoal: Int,
    totalProtein: Int,
    totalCarbs: Int,
    totalFat: Int,
    modifier: Modifier = Modifier
) {
    val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
    val calValues = listOf(2250, 2480, 2150, 2400, totalCaloriesToday, 2300, 2100)

    val totalMacroGrams = (totalProtein + totalCarbs + totalFat).coerceAtLeast(1)
    val proteinPct = ((totalProtein.toFloat() / totalMacroGrams) * 100).toInt()
    val carbsPct = ((totalCarbs.toFloat() / totalMacroGrams) * 100).toInt()
    val fatPct = (100 - proteinPct - carbsPct).coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .testTag("cal_ai_progress_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Streak Banner Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x0A000000), spotColor = Color(0x12000000))
                .clip(RoundedCornerShape(24.dp))
                .background(CardBackground)
                .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(AccentOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "14 Day Streak!",
                            fontFamily = InterFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                        Text(
                            text = "Logged all meals consistently",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AccentGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "On Track",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7-Day Calorie Intake Bar Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x0A000000), spotColor = Color(0x12000000))
                .clip(RoundedCornerShape(24.dp))
                .background(CardBackground)
                .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Weekly Calorie Intake",
                            fontFamily = InterFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                        Text(
                            text = "Target: $calorieGoal kcal daily",
                            fontFamily = InterFontFamily,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Text(
                        text = "Avg: 2,280 kcal",
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentPurple
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bar chart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weekDays.forEachIndexed { idx, day ->
                        val cal = calValues[idx]
                        val heightFraction = (cal.toFloat() / (calorieGoal * 1.15f)).coerceIn(0.1f, 1f)
                        val isToday = idx == 4

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (cal >= 1000) "${cal / 1000}k" else "$cal",
                                fontFamily = InterFontFamily,
                                fontSize = 9.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(0.48f),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(heightFraction)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isToday) AccentPurple else AccentPurple.copy(alpha = 0.25f))
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = day,
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                                color = if (isToday) AccentPurple else TextMain
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Macro Ratio Breakdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x0A000000), spotColor = Color(0x12000000))
                .clip(RoundedCornerShape(24.dp))
                .background(CardBackground)
                .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Macronutrient Distribution",
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Text(
                    text = "Today's energy split ratio",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Multi-color segmented progress bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    Box(modifier = Modifier.weight(proteinPct.coerceAtLeast(1).toFloat()).fillMaxHeight().background(AccentBlue))
                    Box(modifier = Modifier.weight(carbsPct.coerceAtLeast(1).toFloat()).fillMaxHeight().background(AccentOrange))
                    Box(modifier = Modifier.weight(fatPct.coerceAtLeast(1).toFloat()).fillMaxHeight().background(AccentRed))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MacroLegendItem("Protein", "$proteinPct%", "${totalProtein}g", AccentBlue)
                    MacroLegendItem("Carbs", "$carbsPct%", "${totalCarbs}g", AccentOrange)
                    MacroLegendItem("Fats", "$fatPct%", "${totalFat}g", AccentRed)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Coaching Insights
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AccentPurple.copy(alpha = 0.08f))
                .border(0.5.dp, AccentPurple.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = AccentPurple,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = "Cal AI Smart Advice",
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentPurple
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "You're within your optimal calorie deficit for fat loss with healthy protein intake. Keep dinner under 650 kcal to reach your goal today!",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        color = TextMain,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MacroLegendItem(name: String, percentage: String, grams: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(text = name, fontFamily = InterFontFamily, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextMain)
            Text(text = "$percentage ($grams)", fontFamily = InterFontFamily, fontSize = 10.sp, color = TextMuted)
        }
    }
}
