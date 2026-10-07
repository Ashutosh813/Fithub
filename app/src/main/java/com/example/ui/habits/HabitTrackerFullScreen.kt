package com.example.ui.habits

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

data class HabitItem(
    val id: String,
    val title: String,
    val streak: Int,
    val isCompleted: Boolean,
    val category: String
)

@Composable
fun HabitTrackerFullScreen(
    onClose: () -> Unit,
    onHabitToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val habits = remember {
        mutableStateListOf(
            HabitItem("h1", "Morning Hydration (500ml)", 18, true, "Health"),
            HabitItem("h2", "30 Min Daily Workout", 12, true, "Fitness"),
            HabitItem("h3", "10,000 Steps Target", 8, true, "Activity"),
            HabitItem("h4", "Read 15 Pages of Book", 5, false, "Mind"),
            HabitItem("h5", "No Sugar After 8 PM", 14, false, "Nutrition")
        )
    }

    val completedCount = habits.count { it.isCompleted }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("habit_tracker_full_screen"),
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
            ) {
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentGreen.copy(alpha = 0.12f))
                                .border(0.5.dp, AccentGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Habits",
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentGreen
                            )
                        }
                        Column {
                            Text(
                                text = "Daily Routine",
                                fontFamily = InterFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = "$completedCount of ${habits.size} completed today",
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF2F2F7))
                            .clickable(onClick = onClose)
                            .testTag("close_habit_tracker_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Close", tint = TextMain, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Streak Summary
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
                        Column {
                            Text("Consistency Score", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextMuted)
                            Text("${(completedCount.toFloat() / habits.size * 100).toInt()}%", fontFamily = InterFontFamily, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AccentGreen)
                            Text("Current habit streak: 12 days", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AccentGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Check, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(28.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Today's Checklist",
                    fontFamily = InterFontFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
            }

            items(habits) { habit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(18.dp), ambientColor = Color(0x06000000), spotColor = Color(0x0A000000))
                        .clip(RoundedCornerShape(18.dp))
                        .background(CardBackground)
                        .border(0.5.dp, BorderColor, RoundedCornerShape(18.dp))
                        .clickable {
                            val idx = habits.indexOf(habit)
                            if (idx != -1) {
                                habits[idx] = habit.copy(isCompleted = !habit.isCompleted)
                                onHabitToggle()
                            }
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Checkbox circular indicator
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (habit.isCompleted) AccentGreen else Color(0xFFF2F2F7))
                                .border(1.dp, if (habit.isCompleted) AccentGreen else Color(0x1F000000), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (habit.isCompleted) {
                                Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }

                        Column {
                            Text(
                                text = habit.title,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMain
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${habit.streak} days streak",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    color = AccentOrange,
                                    fontWeight = FontWeight.Medium
                                )
                                Text("• ${habit.category}", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
