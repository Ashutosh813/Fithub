package com.example.ui.water

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ThreeLevelMinimizeContainer
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun WaterTrackingFullScreen(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var isNotificationSubscribed by remember { mutableStateOf(false) }

    ThreeLevelMinimizeContainer(
        onClose = onClose,
        modifier = modifier
    ) { dragModifier ->
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("water_tracking_full_screen"),
            containerColor = BackgroundColor,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .statusBarsPadding()
                        .then(dragModifier)
                ) {
                    // Top drag handle - dragging down or tapping minimizes
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
                                    .background(AccentBlue.copy(alpha = 0.12f))
                                    .border(0.5.dp, AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Water",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                            }
                            Column {
                                Text(
                                    text = "Water Tracking",
                                    fontFamily = InterFontFamily,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                Text(
                                    text = "Modular hydration engine",
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        // Coming Soon Pill Badge
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF))
                                .border(0.5.dp, Color(0xFFDBEAFE), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = AccentBlue,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Coming Soon",
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentBlue
                                )
                            }
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
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Hero Coming Soon Graphic
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = Color(0x10007AFF),
                            spotColor = Color(0x28007AFF)
                        )
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF))
                        .border(1.5.dp, Color(0xFFBFDBFE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WaterDrop,
                        contentDescription = "Water Tracking",
                        tint = AccentBlue,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Water Tracking is Coming Soon",
                    fontFamily = InterFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = TextMain,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A smart, adaptive hydration experience designed to sync your water intake with weather, workouts, and sleep.",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Feature Highlights Card
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
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "What's in the works",
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )

                        WaterFeatureItem(
                            icon = "💧",
                            title = "Smart Hydration Target",
                            desc = "Dynamically adjusts daily volume based on your activity and weather."
                        )

                        WaterFeatureItem(
                            icon = "⚡",
                            title = "Quick Logging Widget",
                            desc = "Single-tap volume presets (+250ml, +500ml, bottle refill) from Home."
                        )

                        WaterFeatureItem(
                            icon = "📊",
                            title = "Electrolyte & Fluid Balance",
                            desc = "Detailed breakdown alongside sodium and workout sweat loss metrics."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Notify Me CTA Button
                Button(
                    onClick = { isNotificationSubscribed = !isNotificationSubscribed },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isNotificationSubscribed) Color(0xFFF2F2F7) else TextMain
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = if (isNotificationSubscribed) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsNone,
                        contentDescription = null,
                        tint = if (isNotificationSubscribed) AccentBlue else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isNotificationSubscribed) "You're on the early list! ✓" else "Notify Me When Ready",
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNotificationSubscribed) TextMain else Color.White
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Back / Close button
                Text(
                    text = "Close to Home",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    modifier = Modifier
                        .clickable(onClick = onClose)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WaterFeatureItem(icon: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 20.sp)
        Column {
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextMain
            )
            Text(
                text = desc,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}
