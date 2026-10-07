package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundColor
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

data class ComingSoonFeatureInfo(
    val title: String,
    val description: String
)

@Composable
fun ComingSoonAppScreen(
    appName: String,
    categoryBadge: String,
    subtitle: String,
    iconVector: ImageVector,
    themeColor: Color,
    features: List<ComingSoonFeatureInfo>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        dragOffsetY += delta
        if (dragOffsetY > 120f) {
            onClose()
        }
    }

    var isNotificationSubscribed by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("coming_soon_${appName.lowercase().replace(" ", "_")}"),
        containerColor = BackgroundColor,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .draggable(
                        state = draggableState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { dragOffsetY = 0f }
                    )
            ) {
                // Top drag handle to minimize
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
                                .background(themeColor.copy(alpha = 0.12f))
                                .border(0.5.dp, themeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = categoryBadge,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = themeColor
                            )
                        }
                        Column {
                            Text(
                                text = appName,
                                fontFamily = InterFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                            Text(
                                text = subtitle,
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
                            .background(themeColor.copy(alpha = 0.12f))
                            .border(0.5.dp, themeColor.copy(alpha = 0.35f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Coming Soon",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeColor
                        )
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 3D Glass Emblem Container
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = CircleShape,
                        ambientColor = themeColor.copy(alpha = 0.2f),
                        spotColor = themeColor.copy(alpha = 0.35f)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                themeColor.copy(alpha = 0.2f),
                                themeColor.copy(alpha = 0.35f),
                                themeColor.copy(alpha = 0.55f)
                            )
                        )
                    )
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = appName,
                    tint = themeColor,
                    modifier = Modifier.size(76.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Prominent "Coming Soon" Pill Banner
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Coming Soon",
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Smart $appName",
                fontFamily = InterFontFamily,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextMain
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We are putting the finishing touches on $appName integration. It will sync automatically with your FitHub daily dashboard and Apple Health / Health Connect.",
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Feature preview highlights
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp), ambientColor = Color(0x08000000), spotColor = Color(0x10000000))
                    .clip(RoundedCornerShape(22.dp))
                    .background(CardBackground)
                    .border(0.5.dp, BorderColor, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    features.forEach { feature ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(themeColor)
                            )
                            Column {
                                Text(
                                    text = feature.title,
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                Text(
                                    text = feature.description,
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.5.sp,
                                    color = TextMuted,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = { isNotificationSubscribed = !isNotificationSubscribed },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNotificationSubscribed) Color(0xFF10B981) else Color(0xFF0F172A)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isNotificationSubscribed) "Notification Set!" else "Notify Me When Launched",
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
