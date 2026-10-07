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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
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

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("sleep_tracker_full_screen"),
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF2F2F7))
                            .clickable(onClick = onClose)
                            .testTag("close_sleep_tracker_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Close", tint = TextMain, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Sleep Score Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(24.dp), ambientColor = Color(0x0A000000), spotColor = Color(0x12000000))
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBackground)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sleep Quality Score", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextMuted)
                        Text("88 / 100", fontFamily = InterFontFamily, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        Text("Optimal Recovery", fontFamily = InterFontFamily, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AccentPurple)
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AccentPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Bedtime, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(36.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Stages Breakdown (Deep, REM, Core, Awake)
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
                    Text("Sleep Stages", fontFamily = InterFontFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    Text("Total time asleep: $sleepHours hrs (Target: 8 hrs)", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        Box(modifier = Modifier.weight(0.22f).fillMaxHeight().background(Color(0xFF322A7B))) // Deep
                        Box(modifier = Modifier.weight(0.25f).fillMaxHeight().background(AccentBlue))        // REM
                        Box(modifier = Modifier.weight(0.45f).fillMaxHeight().background(AccentPurple))      // Core
                        Box(modifier = Modifier.weight(0.08f).fillMaxHeight().background(Color(0xFFE5E5EA))) // Awake
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Deep Sleep", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            Text("1h 25m", fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        }
                        Column {
                            Text("REM Sleep", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            Text("1h 40m", fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        }
                        Column {
                            Text("Core Sleep", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            Text("3h 10m", fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        }
                        Column {
                            Text("Awake", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
                            Text("15m", fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
