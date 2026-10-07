package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class CatalogApp(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val status: String
)

@Composable
fun AppsCatalogScreen(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val apps = listOf(
        CatalogApp("Calorie Tracker", "AI meal logging & macronutrient breakdown", Icons.Rounded.LocalFireDepartment, AccentRed, "Active"),
        CatalogApp("Habit Tracker", "Streaks, daily check-ins & reminders", Icons.Rounded.Check, AccentGreen, "Active"),
        CatalogApp("Sleep Tracker", "Sleep cycles, bedtime alarms & recovery", Icons.Rounded.Bedtime, AccentPurple, "Active"),
        CatalogApp("Hydration Tracker", "Smart water logging with intake goals", Icons.Rounded.WaterDrop, AccentBlue, "Available"),
        CatalogApp("Activity & Steps", "Pedometer, distance & cadence metrics", Icons.Rounded.DirectionsRun, AccentOrange, "Available"),
        CatalogApp("Workout Log", "Sets, reps, weights and gym routines", Icons.Rounded.FitnessCenter, AccentPurple, "Available"),
        CatalogApp("Mindfulness & Rest", "Guided breathwork and meditation timers", Icons.Rounded.SelfImprovement, AccentGreen, "Available")
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("apps_catalog_scaffold"),
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
                .padding(horizontal = 20.dp)
        ) {
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
                    text = "Fitness Apps",
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
            }

            Text(
                text = "Discover all integrated AI fitness and wellness modules.",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(apps) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardBackground)
                            .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
                            .clickable { /* Module detail */ }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(app.color.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(app.icon, contentDescription = app.title, tint = app.color, modifier = Modifier.size(22.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = app.title,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextMain
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = app.description,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                color = TextMuted,
                                lineHeight = 16.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (app.status == "Active") AccentPurple.copy(alpha = 0.1f) else Color(0xFFF2F2F7))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = app.status,
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (app.status == "Active") AccentPurple else TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
