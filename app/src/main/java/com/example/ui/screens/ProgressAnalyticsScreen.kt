package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AvTimer
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
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
import com.example.model.AppTracker
import com.example.model.NavTab
import com.example.ui.components.FitHubBottomNav
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun ProgressAnalyticsScreen(
    trackers: List<AppTracker> = emptyList(),
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val activeIds = trackers.map { it.id }.toSet()
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_analytics_scaffold"),
        containerColor = BackgroundColor,
        bottomBar = {
            FitHubBottomNav(selectedTab = selectedTab, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = TextMain)
                }
                Text(
                    text = "Progress & Analytics",
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }

            Text(
                text = "Performance breakdown for all active apps on your Home dashboard.",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // DYNAMIC APP PROGRESS SECTIONS: Shows progress for each app currently in My Apps!

            // 1. Calorie Tracker Progress
            if ("calorie" in activeIds) {
                ProgressCardWrapper(
                    title = "Calorie Intake & Deficit",
                    subtitle = "Daily target: 2,500 kcal • Avg: 2,280 kcal",
                    iconVector = Icons.Rounded.LocalFireDepartment,
                    iconColor = AccentRed,
                    statusText = "On Track",
                    statusColor = AccentGreen
                ) {
                    val calValues = listOf(2250, 2480, 2150, 2400, 2350, 2200, 2100)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        daysOfWeek.forEachIndexed { idx, day ->
                            val cal = calValues[idx]
                            val fraction = (cal / 2800f).coerceIn(0.1f, 1f)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(0.42f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(fraction)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (idx == 6) AccentPurple else AccentPurple.copy(alpha = 0.25f))
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (idx == 6) AccentPurple else TextMuted
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Habit Tracker Progress
            if ("habit" in activeIds) {
                ProgressCardWrapper(
                    title = "Habits Consistency",
                    subtitle = "5 daily routines • 12 Day Active Streak",
                    iconVector = Icons.Rounded.Check,
                    iconColor = AccentGreen,
                    statusText = "85% Done",
                    statusColor = AccentGreen
                ) {
                    val habitCompletions = listOf(5, 4, 5, 5, 4, 5, 3)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEachIndexed { idx, day ->
                            val count = habitCompletions[idx]
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (count >= 4) AccentGreen.copy(alpha = 0.15f) else Color(0xFFF3F4F6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$count/5",
                                        fontFamily = InterFontFamily,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (count >= 4) AccentGreen else TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = day, fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. Sleep Tracker Progress
            if ("sleep" in activeIds) {
                ProgressCardWrapper(
                    title = "Sleep Duration & Quality",
                    subtitle = "Avg: 7.4 hrs/night • Sleep score: 88/100",
                    iconVector = Icons.Rounded.Bedtime,
                    iconColor = AccentPurple,
                    statusText = "Optimal",
                    statusColor = AccentPurple
                ) {
                    val sleepValues = listOf(7.2f, 6.8f, 7.5f, 8.1f, 7.0f, 7.8f, 6.5f)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        daysOfWeek.forEachIndexed { idx, day ->
                            val hrs = sleepValues[idx]
                            val fraction = (hrs / 9.0f).coerceIn(0.1f, 1f)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(0.42f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(fraction)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(AccentPurple.copy(alpha = 0.35f))
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = day, fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. Water Tracking Progress
            if ("water" in activeIds) {
                ProgressCardWrapper(
                    title = "Water Hydration Intake",
                    subtitle = "Target: 8 glasses daily • 4/8 glasses today",
                    iconVector = Icons.Rounded.WaterDrop,
                    iconColor = AccentBlue,
                    statusText = "Hydrating",
                    statusColor = AccentBlue
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Today's Progress", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextMuted)
                            Text("50% (1,000 / 2,000 ml)", fontFamily = InterFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentBlue)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.5f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AccentBlue,
                            trackColor = Color(0xFFE0F2FE)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Activity Steps Progress
            if ("activity" in activeIds) {
                ProgressCardWrapper(
                    title = "Steps & Movement",
                    subtitle = "Daily goal: 10,000 steps • 6,842 steps today",
                    iconVector = Icons.Rounded.DirectionsRun,
                    iconColor = AccentOrange,
                    statusText = "68% Done",
                    statusColor = AccentOrange
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Daily Activity", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextMuted)
                            Text("6.8k / 10k steps", fontFamily = InterFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentOrange)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.68f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AccentOrange,
                            trackColor = Color(0xFFFFEDD5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 6. Workout Log Progress
            if ("workout" in activeIds) {
                ProgressCardWrapper(
                    title = "Workouts & Resistance",
                    subtitle = "4 sessions this week • Total volume: 14,800 kg",
                    iconVector = Icons.Rounded.FitnessCenter,
                    iconColor = AccentPurple,
                    statusText = "Active",
                    statusColor = AccentPurple
                ) {
                    Text(
                        text = "Great consistency! You've achieved 80% of your weekly muscle hypertrophy targets.",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 7. Fasting Progress
            if ("fasting" in activeIds) {
                ProgressCardWrapper(
                    title = "Intermittent Fasting",
                    subtitle = "16:8 Protocol • 14h / 16h completed today",
                    iconVector = Icons.Rounded.AvTimer,
                    iconColor = AccentOrange,
                    statusText = "In Window",
                    statusColor = AccentOrange
                ) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        LinearProgressIndicator(
                            progress = { 14f / 16f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AccentOrange,
                            trackColor = Color(0xFFFEF3C7)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 8. Mindfulness Progress
            if ("meditation" in activeIds) {
                ProgressCardWrapper(
                    title = "Mindfulness & Breathwork",
                    subtitle = "15 / 20 mins logged today • 5 Day Streak",
                    iconVector = Icons.Rounded.SelfImprovement,
                    iconColor = AccentGreen,
                    statusText = "75% Done",
                    statusColor = AccentGreen
                ) {
                    Text(
                        text = "Consistent daily meditation has improved your resting recovery score by 12%.",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun ProgressCardWrapper(
    title: String,
    subtitle: String,
    iconVector: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    statusText: String,
    statusColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x08000000), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(iconColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(iconVector, contentDescription = title, tint = iconColor, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(text = title, fontFamily = InterFontFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        Text(text = subtitle, fontFamily = InterFontFamily, fontSize = 11.5.sp, color = TextMuted)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(text = statusText, fontFamily = InterFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            content()
        }
    }
}
