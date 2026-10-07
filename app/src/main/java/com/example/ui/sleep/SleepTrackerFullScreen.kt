package com.example.ui.sleep

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ThreeLevelMinimizeContainer
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun SleepTrackerFullScreen(
    sleepHours: Float,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    ThreeLevelMinimizeContainer(
        onClose = onClose,
        modifier = modifier
    ) { dragModifier ->
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("sleep_tracker_full_screen"),
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
                                    .background(AccentPurple.copy(alpha = 0.12f))
                                    .border(0.5.dp, AccentPurple.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Sleep",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple
                                )
                            }
                            Column {
                                Text(
                                    text = "Sleep Analysis",
                                    fontFamily = InterFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                Text(
                                    text = "Last night • 11:20 PM - 5:50 AM",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFF3E8FF))
                                .border(0.5.dp, Color(0xFFE9D5FF), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Bedtime,
                                    contentDescription = "Quality",
                                    tint = AccentPurple,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "88% Quality",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    // Sleep Hero Card with Progress
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Time Asleep",
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${sleepHours}h",
                                        fontFamily = InterFontFamily,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-1).sp,
                                        color = TextMain
                                    )
                                    Text(
                                        text = " / 8.0h goal",
                                        fontFamily = InterFontFamily,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextMuted,
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                }
                                Text(
                                    text = "Optimal REM & deep sleep cycles",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    color = AccentPurple
                                )
                            }

                            Box(
                                modifier = Modifier.size(86.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { 1f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color(0xFFF3E8FF),
                                    strokeWidth = 9.dp,
                                    trackColor = Color.Transparent
                                )
                                CircularProgressIndicator(
                                    progress = { (sleepHours / 8.0f).coerceIn(0f, 1f) },
                                    modifier = Modifier.fillMaxSize(),
                                    color = AccentPurple,
                                    strokeWidth = 9.dp,
                                    strokeCap = StrokeCap.Round,
                                    trackColor = Color.Transparent
                                )
                                Icon(
                                    imageVector = Icons.Rounded.Bedtime,
                                    contentDescription = null,
                                    tint = AccentPurple,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    // Sleep Stages breakdown
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
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Sleep Stages",
                                fontFamily = InterFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )

                            SleepStageRow("Deep Sleep", "1h 48m", "27%", AccentPurple)
                            SleepStageRow("REM Sleep", "2h 12m", "34%", Color(0xFF8B5CF6))
                            SleepStageRow("Light Sleep", "2h 30m", "39%", Color(0xFFA78BFA))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SleepStageRow(name: String, duration: String, percentage: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = name,
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                color = TextMain
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = duration,
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Text(
                text = "($percentage)",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = TextMuted
            )
        }
    }
}
