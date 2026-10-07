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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.components.ThreeLevelMinimizeContainer
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
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

    var habitToDelete by remember { mutableStateOf<HabitItem?>(null) }
    val completedCount = habits.count { it.isCompleted }

    ThreeLevelMinimizeContainer(
        onClose = onClose,
        modifier = modifier
    ) { dragModifier ->
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("habit_tracker_full_screen"),
            containerColor = BackgroundColor,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .statusBarsPadding()
                        .then(dragModifier)
                ) {
                    // Top handle bar - drag down or tap minimizes
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onClose)
                            .padding(top = 10.dp, bottom = 8.dp),
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
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
                                    text = "Habit Tracker",
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

                        // Streak Pill
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
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Streak",
                                    tint = AccentOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "18 Days",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Summary Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color(0x06000000),
                                spotColor = Color(0x10000000)
                            )
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = "Today's Progress",
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${((completedCount.toFloat() / habits.size.toFloat()) * 100).toInt()}% Done",
                                fontFamily = InterFontFamily,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFF2F2F7))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(completedCount.toFloat() / habits.size.toFloat())
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AccentGreen)
                                )
                            }
                        }
                    }
                }

                items(habits, key = { it.id }) { habit ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color(0x06000000),
                                spotColor = Color(0x0C000000)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
                            .clickable {
                                val index = habits.indexOfFirst { it.id == habit.id }
                                if (index != -1) {
                                    val current = habits[index]
                                    habits[index] = current.copy(isCompleted = !current.isCompleted)
                                    onHabitToggle()
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (habit.isCompleted) AccentGreen else Color(0xFFF2F2F7))
                                        .border(
                                            width = if (habit.isCompleted) 0.dp else 1.dp,
                                            color = if (habit.isCompleted) Color.Transparent else Color(0x18000000),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (habit.isCompleted) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = "Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = habit.title,
                                        fontFamily = InterFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain
                                    )
                                    Text(
                                        text = "${habit.category} • ${habit.streak} day streak",
                                        fontFamily = InterFontFamily,
                                        fontSize = 11.5.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            // Delete habit button with confirmation
                            IconButton(
                                onClick = { habitToDelete = habit },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "Delete Habit",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog before deleting habit
    if (habitToDelete != null) {
        val h = habitToDelete!!
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = {
                Text(
                    text = "Delete Habit?",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextMain
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${h.title}\"?",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        habits.removeAll { it.id == h.id }
                        habitToDelete = null
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
                TextButton(onClick = { habitToDelete = null }) {
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
