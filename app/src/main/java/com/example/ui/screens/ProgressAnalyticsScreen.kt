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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NavTab
import com.example.ui.components.FitHubBottomNav
import com.example.ui.theme.AccentGreen
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
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val calPercentages = listOf(0.75f, 0.90f, 0.65f, 0.85f, 0.52f, 0.95f, 0.80f)

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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
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
                    text = "Weekly Progress",
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
            }

            // Overview Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBackground)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Calorie Adherence",
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "Average 2,180 kcal/day (Target: 2,500 kcal)",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Weekly Bar Chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        daysOfWeek.forEachIndexed { index, day ->
                            val pct = calPercentages[index]
                            val isToday = index == 4 // Fri

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(0.45f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(pct)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isToday) AccentPurple else AccentPurple.copy(alpha = 0.35f))
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = day,
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isToday) AccentPurple else TextMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Consistency Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardBackground)
                        .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Habits Hit", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                        Text("21 / 25", fontFamily = InterFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardBackground)
                        .border(0.5.dp, BorderColor, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = AccentRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Burned", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                        Text("18,420 kcal", fontFamily = InterFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
