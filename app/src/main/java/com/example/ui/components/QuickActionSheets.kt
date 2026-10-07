package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppTracker
import com.example.model.TodayMetric
import com.example.model.TodayMetricType
import com.example.model.TrackerIconType
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerDetailSheet(
    tracker: AppTracker,
    onDismiss: () -> Unit,
    onAddCalories: (Int) -> Unit,
    onToggleHabit: () -> Unit,
    onAddSleep: (Float) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("tracker_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tracker.title,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Current progress summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(tracker.iconBgLight)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Progress",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "${tracker.currentFormatted} ${tracker.targetFormatted}",
                            fontFamily = InterFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(tracker.progressColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${(tracker.progress * 100).toInt()}%",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = tracker.progressColor,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick actions based on tracker type
            when (tracker.iconType) {
                TrackerIconType.CALORIE -> {
                    Text(
                        text = "Quick Log Meal / Snack",
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onAddCalories(150); onDismiss() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+150 kcal", fontFamily = InterFontFamily)
                        }
                        OutlinedButton(
                            onClick = { onAddCalories(350); onDismiss() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+350 kcal", fontFamily = InterFontFamily)
                        }
                        Button(
                            onClick = { onAddCalories(500); onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+500", fontFamily = InterFontFamily)
                        }
                    }
                }
                TrackerIconType.HABIT -> {
                    Text(
                        text = "Habit Completion",
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onToggleHabit(); onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mark Next Habit Complete", fontFamily = InterFontFamily)
                    }
                }
                TrackerIconType.SLEEP -> {
                    Text(
                        text = "Log Sleep Duration",
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMain,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onAddSleep(0.5f); onDismiss() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+0.5 hr", fontFamily = InterFontFamily)
                        }
                        Button(
                            onClick = { onAddSleep(1.0f); onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+1.0 hr", fontFamily = InterFontFamily)
                        }
                    }
                }
                else -> {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Done", fontFamily = InterFontFamily)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAppsSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("manage_apps_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Manage Apps & Widgets",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Text(
                text = "Customize which trackers appear on your home grid.",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val availableWidgets = listOf(
                Triple("Calorie Tracker", "Active on Home Grid", true),
                Triple("Habit Tracker", "Active on Home Grid", true),
                Triple("Sleep Tracker", "Active on Home Grid", true),
                Triple("Water Hydration", "Tap to add to grid", false),
                Triple("Workout Activity", "Tap to add to grid", false)
            )

            availableWidgets.forEach { (title, subtitle, isActive) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(0.5.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = title,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextMain
                        )
                        Text(
                            text = subtitle,
                            fontFamily = InterFontFamily,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isActive) AccentPurple else Color(0xFFF2F2F7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isActive) Icons.Rounded.Check else Icons.Rounded.Add,
                            contentDescription = null,
                            tint = if (isActive) Color.White else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Preferences", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("profile_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AccentPurple.copy(alpha = 0.1f))
                    .border(1.dp, AccentPurple.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.FitnessCenter,
                    contentDescription = "Profile Avatar",
                    tint = AccentPurple,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Fitness Enthusiast",
                fontFamily = InterFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Text(
                text = "Daily Goal: 2,500 kcal • 8 hrs sleep • 10k steps",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF9F9FB))
                        .border(0.5.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("14 Days", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AccentPurple)
                        Text("Streak", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF9F9FB))
                        .border(0.5.dp, BorderColor, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("92%", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AccentGreen)
                        Text("Consistency", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close", fontFamily = InterFontFamily)
            }
        }
    }
}
